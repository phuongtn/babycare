package com.wedevol.xmpp.util;

import java.util.Map;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.MessageHelper;

public class PushMessageFactory {
	public static String createMessagePayLoad(CcsOutMessage ccsOutMessage) {
		return MessageHelper.createJsonOutMessage(ccsOutMessage);
	}

	public static CcsOutMessage createSimpleCCsOutMessage(String to, String messageId, Map<String, String> dataPayload) {
		return new CcsOutMessage(to, messageId, dataPayload);
	}
}
