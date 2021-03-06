package com.wedevol.xmpp.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.xmlpull.v1.XmlPullParser;

import com.babycare.events.ChangeEvent;
import com.babycare.events.FCMReconnectSuccessEvent;
import com.babycare.events.UnRecoverablePushMessage;
import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.service.PayloadProcessor;
import com.wedevol.xmpp.util.Util;

/**
 * Sample Smack implementation of a client for FCM Cloud Connection Server. Most
 * of it has been taken more or less verbatim from Google's documentation:
 * https://firebase.google.com/docs/cloud-messaging/xmpp-server-ref
 */
@Configurable
public class CcsClient implements PacketListener, ApplicationEventPublisherAware {

	public static final Logger logger = Logger.getLogger(CcsClient.class.getName());

	private static CcsClient sInstance = null;
	private XMPPConnection connection;
	private ConnectionConfiguration config;
	private String mApiKey = null;
	private String mProjectId = null;
	private boolean mDebuggable = false;
	private String fcmServerUsername = null;
	private AtomicBoolean connectStatus = new AtomicBoolean(false);
	private AtomicBoolean loginStatus = new AtomicBoolean(false);
	private AtomicBoolean connectingStatus = new AtomicBoolean(false); 
	private ApplicationEventPublisher publisher;

	public static CcsClient getInstance() {
		if (sInstance == null) {
			throw new IllegalStateException("You have to prepare the client first");
		}
		return sInstance;
	}

	public static CcsClient prepareClient(String projectId, String apiKey, boolean debuggable) {
		synchronized (CcsClient.class) {
			if (sInstance == null) {
				sInstance = new CcsClient(projectId, apiKey, debuggable);
				try {
					sInstance.connect();
					sInstance.setConnectSuccess(true);
				} catch (XMPPException e) {
					sInstance.setConnectSuccess(false);
					e.printStackTrace();
				}
			}
		}
		return sInstance;
	}

	private CcsClient(String projectId, String apiKey, boolean debuggable) {
		this();
		mApiKey = apiKey;
		mProjectId = projectId;
		mDebuggable = debuggable;
		fcmServerUsername = mProjectId + "@" + Util.FCM_SERVER_CONNECTION;
	}

	private CcsClient() {
		// Add GcmPacketExtension
		ProviderManager.getInstance().addExtensionProvider(Util.FCM_ELEMENT_NAME, Util.FCM_NAMESPACE,
				new PacketExtensionProvider() {

					@Override
					public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
						String json = parser.nextText();
						GcmPacketExtension packet = new GcmPacketExtension(json);
						return packet;
					}
				});
	}

	/**
	 * Connects to FCM Cloud Connection Server using the supplied credentials
	 */
	public void connect() throws XMPPException {
		config = getConnectionConfiguration();
		connection = getXMPPConnection(config);
		boolean tryConenct = tryConnect(connection);
		if (tryConenct) {
			connection.addConnectionListener(connectionListener);
			addPacketListener(connection);
			doLogin();
		}
	}

	@Scheduled(fixedRate = 600000L)
	// 10 mins
	public void reconnect() {
		logger.log(Level.INFO, "PHUONG Try to reconnect");
		if (!connectingStatus.get() && (!connectStatus.get() || !loginStatus.get())) {
			try {
				connect();
			} catch (XMPPException xmppException) {
				logger.log(Level.INFO, "reconnect " + xmppException.toString());
			}
			if (connectStatus.get() && loginStatus.get()) {
				publisher.publishEvent(new ChangeEvent(new FCMReconnectSuccessEvent()));
			}
		}
		// Try to connect again using exponential back-off!
	}

	/**
	 * Handles incoming messages
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void processPacket(Packet packet) {
		logger.log(Level.INFO, "Received: " + packet.toXML());
		Message incomingMessage = (Message) packet;
		GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage.getExtension(Util.FCM_NAMESPACE);
		String json = gcmPacket.getJson();
		try {
			Map<String, Object> jsonMap = (Map<String, Object>) JSONValue.parseWithException(json);
			Object messageType = jsonMap.get("message_type");

			if (messageType == null) {
				CcsInMessage inMessage = MessageHelper.createCcsInMessage(jsonMap);
				handleUpstreamMessage(inMessage); // normal upstream message
				return;
			}

			switch (messageType.toString()) {
			case "ack":
				logger.log(Level.INFO, "Handle handleAckReceipt from " + jsonMap.get("from"));
				handleAckReceipt(jsonMap);
				break;
			case "nack":
				logger.log(Level.INFO, "Handle handleNackReceipt");
				handleNackReceipt(jsonMap);
				break;
			case "receipt":
				logger.log(Level.INFO, "Handle handleDeliveryReceipt");
				handleDeliveryReceipt(jsonMap);
				break;
			case "control":
				logger.log(Level.INFO, "Handle handleControlMessage");
				handleControlMessage(jsonMap);
				break;
			default:
				logger.log(Level.INFO, "Received unknown FCM message type: " + messageType.toString());
			}
		} catch (ParseException e) {
			logger.log(Level.INFO, "Error parsing JSON: " + json, e.getMessage());
		}

	}

	/**
	 * Handles an upstream message from a device client through FCM
	 */
	private void handleUpstreamMessage(CcsInMessage inMessage) {
		final String action = inMessage.getDataPayload().get(Util.PAYLOAD_ATTRIBUTE_ACTION);
		if (action != null) {
			PayloadProcessor processor = ProcessorFactory.getProcessor(action);
			processor.handleMessage(inMessage);
		}

		// Send ACK to FCM
		String ack = MessageHelper.createJsonAck(inMessage.getFrom(), inMessage.getMessageId());
		send(ack);
	}

	/**
	 * Handles an ACK message from FCM
	 */
	private void handleAckReceipt(Map<String, Object> jsonMap) {
		// TODO: handle the ACK in the proper way
	}

	/**
	 * Handles a NACK message from FCM
	 */
	private void handleNackReceipt(Map<String, Object> jsonMap) {
		String errorCode = (String) jsonMap.get("error");

		if (errorCode == null) {
			logger.log(Level.INFO, "Received null FCM Error Code");
			return;
		}

		switch (errorCode) {
		case "INVALID_JSON":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "BAD_REGISTRATION":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "DEVICE_UNREGISTERED":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "BAD_ACK":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "SERVICE_UNAVAILABLE":
			handleServerFailure(jsonMap);
			break;
		case "INTERNAL_SERVER_ERROR":
			handleServerFailure(jsonMap);
			break;
		case "DEVICE_MESSAGE_RATE_EXCEEDED":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "TOPICS_MESSAGE_RATE_EXCEEDED":
			handleUnrecoverableFailure(jsonMap);
			break;
		case "CONNECTION_DRAINING":
			handleConnectionDrainingFailure();
			break;
		default:
			logger.log(Level.INFO, "Received unknown FCM Error Code: " + errorCode);
		}
	}

	/**
	 * Handles a Delivery Receipt message from FCM (when a device confirms that it
	 * received a particular message)
	 */
	private void handleDeliveryReceipt(Map<String, Object> jsonMap) {
		// TODO: handle the delivery receipt
	}

	/**
	 * Handles a Control message from FCM
	 */
	private void handleControlMessage(Map<String, Object> jsonMap) {
		// TODO: handle the control message
		String controlType = (String) jsonMap.get("control_type");

		if (controlType.equals("CONNECTION_DRAINING")) {
			handleConnectionDrainingFailure();
		} else {
			logger.log(Level.INFO, "Received unknown FCM Control message: " + controlType);
		}
	}

	private void handleServerFailure(Map<String, Object> jsonMap) {
		// TODO: Resend the message
		logger.log(Level.INFO, "Server error: " + jsonMap.get("error") + " -> " + jsonMap.get("error_description"));

	}

	private void handleUnrecoverableFailure(Map<String, Object> jsonMap) {
		// TODO: handle the unrecoverable failure
		// logger.log(Level.INFO, "Unrecoverable error: " + jsonMap.get("error") + " ->
		// " + jsonMap.get("error_description"));
		logger.log(Level.INFO, "Unrecoverable error: " + jsonMap.get("error") + " -> " + jsonMap.get("message_id"));
		publisher.publishEvent(new ChangeEvent(new UnRecoverablePushMessage(jsonMap.get("message_id").toString())));
	}

	private void handleConnectionDrainingFailure() {
		// TODO: handle the connection draining failure. Force reconnect?
		logger.log(Level.INFO, "FCM Connection is draining! Initiating reconnection ...");
	}

	/**
	 * Sends a downstream message to FCM
	 */
	public void send(String jsonRequest) {
		// TODO: Resend the message using exponential back-off!
		Packet request = new GcmPacketExtension(jsonRequest).toPacket();
		connection.sendPacket(request);
	}

	/**
	 * Sends a message to multiple recipients (list). Kind of like the old HTTP
	 * message with the list of regIds in the "registration_ids" field.
	 */
	public void sendBroadcast(CcsOutMessage outMessage, List<String> recipients) {
		Map<String, Object> map = MessageHelper.createAttributeMap(outMessage);
		for (String toRegId : recipients) {
			String messageId = Util.getUniqueMessageId();
			map.put("message_id", messageId);
			map.put("to", toRegId);
			String jsonRequest = MessageHelper.createJsonMessage(map);
			send(jsonRequest);
		}
	}

	public boolean isConnectSuccess() {
		return connectStatus.get();
	}

	public void setConnectSuccess(boolean isConnected) {
		this.connectStatus.set(isConnected);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		publisher = applicationEventPublisher;
	}

	public ConnectionConfiguration getConnectionConfiguration() {
		ConnectionConfiguration config = new ConnectionConfiguration(Util.FCM_SERVER, Util.FCM_PORT);
		config.setSecurityMode(SecurityMode.enabled);
		config.setReconnectionAllowed(true);
		config.setRosterLoadedAtLogin(false);
		config.setSendPresence(false);
		config.setSocketFactory(SSLSocketFactory.getDefault());
		// Launch a window with info about packets sent and received
		config.setDebuggerEnabled(mDebuggable);
		return config;
	}

	public XMPPConnection getXMPPConnection(ConnectionConfiguration config) {
		return new XMPPConnection(config);
	}

	private boolean tryConnect(XMPPConnection connection) {
		try {
			connectingStatus.set(true);
			connection.connect();
			connectStatus.set(true);
		} catch (XMPPException xmppException) {
			connectingStatus.set(false);
			connectStatus.set(false);
		}
		return connectStatus.get();
	}
	

	private ConnectionListener connectionListener = new ConnectionListener() {
		@Override
		public void reconnectionSuccessful() {
			logger.log(Level.INFO, "Reconnection successful ...");
			connectingStatus.set(false);
			connectStatus.set(true);
			publisher.publishEvent(new ChangeEvent(new FCMReconnectSuccessEvent()));
			// TODO: handle the reconnecting successful
		}

		@Override
		public void reconnectionFailed(Exception e) {
			logger.log(Level.INFO, "Reconnection failed: ", e.getMessage());
			connectingStatus.set(false);
			connectStatus.set(false);
			// TODO: handle the reconnection failed
		}

		@Override
		public void reconnectingIn(int seconds) {
			connectingStatus.set(true);
			logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
			// TODO: handle the reconnecting in
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			connectStatus.set(false);
			logger.log(Level.INFO, "Connection closed on error");
			// TODO: handle the connection closed on error
		}

		@Override
		public void connectionClosed() {
			logger.log(Level.INFO, "Connection closed");
			// TODO: handle the connection closed
		}
	};
	
	private void addPacketListener(XMPPConnection connection) {
		// Handle incoming packets (the class implements the PacketListener)
		connection.addPacketListener(this, new PacketTypeFilter(Message.class));

		// Log all outgoing packets
		connection.addPacketInterceptor(new PacketInterceptor() {
			@Override
			public void interceptPacket(Packet packet) {
				logger.log(Level.INFO, "Sent: {0}", packet.toXML());
			}
		}, new PacketTypeFilter(Message.class));
	}
	
	private boolean doLogin() {
		try {
			connection.login(fcmServerUsername, mApiKey);
			loginStatus.set(true);
			logger.log(Level.INFO, "Logged in: " + fcmServerUsername + " Successfully");
		} catch (XMPPException xmppException) {
			logger.log(Level.INFO, "Cannot Logged in: " + fcmServerUsername);
			loginStatus.set(false);
		}
		return loginStatus.get();
	}
}
