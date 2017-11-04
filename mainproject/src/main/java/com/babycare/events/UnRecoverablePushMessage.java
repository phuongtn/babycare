package com.babycare.events;

import java.io.Serializable;

import com.babycare.model.payload.PushMessage;

public class UnRecoverablePushMessage extends PushMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnRecoverablePushMessage() {
	}

	public UnRecoverablePushMessage(String messageId) {
		super(messageId);
	}

	public String getMessageId() {
		return messageId;
	}

}
