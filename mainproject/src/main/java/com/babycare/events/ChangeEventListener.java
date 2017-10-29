package com.babycare.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import com.babycare.model.BaseModel;
import com.babycare.model.MessageConstant;
import com.babycare.model.ResultList;
import com.babycare.model.entity.ChildEntity;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.entity.UserEntity;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.util.PushMessageFactory;
import com.wedevol.xmpp.util.Util;

@Component
public class ChangeEventListener extends BaseApplicationListener {
	public static final Logger logger = Logger.getLogger(ChangeEventListener.class.getName());
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ChangeEvent) {
			ChangeEvent changeEvent = (ChangeEvent) event;
			BaseModel baseModel = changeEvent.getModel();
			if (baseModel instanceof UserEntity) {
				UserEntity entity = (UserEntity) baseModel;
				onChangeEvent(MessageConstant.MessageAction.ACCOUNT_CHANGE.getName(), entity.getUserId(), entity.getRequestBySessionId(), "User changed");
			} else if (baseModel instanceof ChildEntity) {
				ChildEntity entity = (ChildEntity) baseModel;
				onChangeEvent(MessageConstant.MessageAction.CHILDREN_CHANGE.getName(), entity.getUserId(), entity.getRequestBySessionId(), "Child changed");
			} else if (baseModel instanceof SessionEntity) {
				SessionEntity entity = (SessionEntity) baseModel;
				onChangeEvent(MessageConstant.MessageAction.SESSION_CHANGE.getName(), entity.getUserId(), entity.getRequestBySessionId(), "Session changed");
				if (StringUtils.isNotBlank(entity.getOldPushId())) {
					pushMessageService.deleteMessageByPushID(entity.getOldPushId());
				}
			}
		}
	}

	private void onChangeEvent(String action, Long userId, Long sessionId, String message) {
		BaseModel baseModel = sessionService.getSessionListByUserId(userId);
		if (baseModel instanceof ResultList) {
			com.babycare.model.ResultList<SessionEntity> resultList = (ResultList) baseModel;
			List<SessionEntity> list = resultList.getResultList();
			List<String> messages = new ArrayList<String>();
			for (SessionEntity entity : list) {
				if (entity.getSessionId() != sessionId) {
					String messageId = Util.getUniqueMessageId();
					Map<String, String> dataPayload = new HashMap<String, String>();
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, message);
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_ACTION,
							action);
					CcsOutMessage ccsOutMessage = PushMessageFactory.
							createSimpleCCsOutMessage(entity.getPushId(), messageId, dataPayload);
					String payLoad = PushMessageFactory.createMessagePayLoad(ccsOutMessage);
					messages.add(payLoad);
					pushMessageService.createEntity(new PushMessageEntity().
							setMessageId(messageId).setPayLoad(payLoad).
							setAction(action).setPushId(entity.getPushId()));
				}
			}
			for (String msg : messages) {
				ccsClient.send(msg);
			}
		}
	}
	
}
