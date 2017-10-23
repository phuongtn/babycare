package com.babycare.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.util.PushMessageFactory;
import com.wedevol.xmpp.util.Util;


@RestController(value = "pushMessageController")
@RequestMapping("pushmessage")
public class PushMessageCotroller {
	@Autowired
	@Qualifier("CcsClient")
	private CcsClient ccsClient;

	@PostMapping(value = "/send", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<Void> send(@RequestBody CcsOutMessage pushMessage) {
		String messageId = Util.getUniqueMessageId();
		pushMessage.setMessageId(messageId);
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "This is a sample message");
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_ACTION, "ACCOUNT_UPDATED");
		CcsOutMessage ccsOutMessage = PushMessageFactory.createSimpleCCsOutMessage(pushMessage.getTo(), messageId, dataPayload);
		String messagePayload = PushMessageFactory.createMessagePayLoad(ccsOutMessage);
		ccsClient.send(messagePayload);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
