package com.wedevol.xmpp.util;

import com.babycare.model.PushMessage;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.MessageHelper;

public class PushMessageFactory {
	public static CcsOutMessage createCcsOutMessage(PushMessage pushMessage) {
		return new CcsOutMessage(pushMessage.getToRegId(), pushMessage.getMessageId(), pushMessage.getPayLoad());
	}

	public static String createMessagePayLoad(CcsOutMessage ccsOutMessage) {
		return MessageHelper.createJsonOutMessage(ccsOutMessage);
	}
	
	public static String createMessagePayLoad(PushMessage pushMessage) {
		CcsOutMessage ccsOutMessage = createCcsOutMessage(pushMessage);
		return MessageHelper.createJsonOutMessage(ccsOutMessage);
	}
}
