package com.babycare.model;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.MessageHelper;

public class PushMessageFactory {
	public static CcsOutMessage createCcsOutMessage(PushMessage pushMessage) {
		return new CcsOutMessage(pushMessage.getToRegId(), pushMessage.getMessageId(), pushMessage.getPayLoad());
	}

	public static String createMessagePayLoad(CcsOutMessage ccsOutMessage) {
		return MessageHelper.createJsonOutMessage(ccsOutMessage);
	}
}
