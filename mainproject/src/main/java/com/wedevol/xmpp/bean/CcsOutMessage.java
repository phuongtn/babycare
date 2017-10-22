package com.wedevol.xmpp.bean;

import java.util.Map;

/**
 * Represents an outgoing message to FCM CCS
 */
public class CcsOutMessage {

	// Sender registration ID
	private String to;
	// Condition that determines the message target
	private String condition;
	// Unique id for this message
	private String messageId;
	// Identifies a group of messages
	private String collapseKey;
	// Priority of the message
	private String priority;
	// Flag to wake client devices
	private Boolean contentAvailable;
	// Time to live
	private Integer timeToLive;
	// Flag to request confirmation of message delivery
	private Boolean deliveryReceiptRequested;
	// Test request without sending a message
	private Boolean dryRun;
	// Payload data. A String in JSON format
	private Map<String, String> dataPayload;
	// Payload notification. A String in JSON format
	private Map<String, String> notificationPayload;

	public CcsOutMessage(String to, String messageId, Map<String, String> dataPayload) {
		this.to = to;
		this.messageId = messageId;
		this.dataPayload = dataPayload;
	}

	public String getTo() {
		return to;
	}

	public CcsOutMessage setTo(String to) {
		this.to = to;
		return this;
	}

	public String getCondition() {
		return condition;
	}

	public CcsOutMessage setCondition(String condition) {
		this.condition = condition;
		return this;
	}

	public String getMessageId() {
		return messageId;
	}

	public CcsOutMessage setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getCollapseKey() {
		return collapseKey;
	}

	public CcsOutMessage setCollapseKey(String collapseKey) {
		this.collapseKey = collapseKey;
		return this;
	}

	public String getPriority() {
		return priority;
	}

	public CcsOutMessage setPriority(String priority) {
		this.priority = priority;
		return this;
	}

	public Boolean isContentAvailable() {
		return contentAvailable;
	}

	public CcsOutMessage setContentAvailable(Boolean contentAvailable) {
		this.contentAvailable = contentAvailable;
		return this;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public CcsOutMessage setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
		return this;
	}

	public Boolean isDeliveryReceiptRequested() {
		return deliveryReceiptRequested;
	}

	public CcsOutMessage setDeliveryReceiptRequested(Boolean deliveryReceiptRequested) {
		this.deliveryReceiptRequested = deliveryReceiptRequested;
		return this;
	}

	public Boolean isDryRun() {
		return dryRun;
	}

	public CcsOutMessage setDryRun(Boolean dryRun) {
		this.dryRun = dryRun;
		return this;
	}

	public Map<String, String> getDataPayload() {
		return dataPayload;
	}

	public CcsOutMessage setDataPayload(Map<String, String> dataPayload) {
		this.dataPayload = dataPayload;
		return this;
	}

	public Map<String, String> getNotificationPayload() {
		return notificationPayload;
	}

	public CcsOutMessage setNotificationPayload(Map<String, String> notificationPayload) {
		this.notificationPayload = notificationPayload;
		return this;
	}

}