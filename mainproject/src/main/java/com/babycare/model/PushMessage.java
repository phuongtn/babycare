package com.babycare.model;

import java.util.Map;

public class PushMessage {
	private String messageId;
	private Map<String, String> payLoad;
	private String toRegId;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getToRegId() {
		return toRegId;
	}

	public void setToRegId(String toRegId) {
		this.toRegId = toRegId;
	}

	public Map<String, String> getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(Map<String, String> payLoad) {
		this.payLoad = payLoad;
	}

	public PushMessage() {

	}
}
