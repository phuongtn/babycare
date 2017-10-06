/*package com.wedevol.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.util.Util;

*//**
 * Entry Point class for the XMPP Server in dev mode for debugging and testing
 * purposes
 *//*
public class EntryPoint {
	public static void main(String[] args) {
		final String SERVER_KEY = "AAAAsOSJJw4:APA91bHpRhVGMBVUpP1mJZCqn0VttNvP2OrN59-4geiDAo2T7h-S16JMf4VwMsQ26Ngw5z0zMryFIrFYzGZaj6-5xP_GX7_g7l4qoYdUmhzDvAcZQNjGKCdP-12qoLVpXgNqDFcNWCVY";
		final String SENDER_KEY = "759748437774";
	
		final String fcmProjectSenderId = args[0];
		final String fcmServerKey = args[1];

		final String fcmProjectSenderId = SENDER_KEY;
		final String fcmServerKey = SERVER_KEY;
		
		//final String toRegId = args[2];

		CcsClient ccsClient = CcsClient.prepareClient(fcmProjectSenderId, fcmServerKey, false);

		try {
			ccsClient.connect();
		} catch (XMPPException e) {
			e.printStackTrace();
		}

		//Send a sample downstream message to a device
		String messageId = Util.getUniqueMessageId();
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "This is the simple sample message tran phuong");
		//dataPayload.put("topic", "news");
		String toRegId = "cDApQh8YfwE:APA91bF5cJVK83aEocjq089k9XeEBfoT-HPe_BlHKzwL-6cjRIuwpywGbu1P0ZwXn_OVhzIG789kE-LMZwHPSObBWOyctMmAapoua5OaZoyaaAVzpFowBO0m4EFK8fLx2g9w9arsXik8";
		CcsOutMessage message = new CcsOutMessage(toRegId, messageId, dataPayload);
		String jsonRequest = MessageHelper.createJsonOutMessage(message);
		ccsClient.send(jsonRequest);
	}
}
*/