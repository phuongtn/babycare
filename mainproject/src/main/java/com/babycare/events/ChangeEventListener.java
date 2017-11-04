package com.babycare.events;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
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
import com.wedevol.xmpp.util.PushMessageStatus;
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
				onChangeEvent(MessageConstant.MessageAction.ACCOUNT_CHANGED.getName(), entity.getUserId(), entity.getRequestBySessionId(), "User changed");
			} else if (baseModel instanceof ChildEntity) {
				ChildEntity entity = (ChildEntity) baseModel;
				onChangeEvent(MessageConstant.MessageAction.CHILDREN_CHANGED.getName(), entity.getUserId(), entity.getRequestBySessionId(), "Child changed");
			} else if (baseModel instanceof SessionEntity) {
				SessionEntity entity = (SessionEntity) baseModel;
				onChangeEvent(MessageConstant.MessageAction.SESSION_CHANGED.getName(), entity.getUserId(), entity.getRequestBySessionId(), "Session changed");
				if (StringUtils.isNotBlank(entity.getOldPushId())) {
					pushMessageService.deleteMessageByPushID(entity.getOldPushId());
				}
			} else if (baseModel instanceof UnRecoverablePushMessage) {
				pushMessageService.deleteMessageByMessageId(((UnRecoverablePushMessage) baseModel).getMessageId());
			} else if (baseModel instanceof FCMReconnectSuccessful) {
				onFCMReconnectSuccessful();
				//logger.log(Level.INFO, "PHUONG FCMReconnectSuccessful");
			}
		}
	}

	private void onChangeEvent(String action, Long userId, Long sessionId, String message) {
		BaseModel baseModel = sessionService.getSessionListByUserId(userId);
		if (baseModel instanceof ResultList) {
			com.babycare.model.ResultList<SessionEntity> resultList = (ResultList) baseModel;
			List<SessionEntity> list = resultList.getResultList();
			Map<String, String> messages = new HashMap<>();
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
					messages.put(messageId, payLoad);
					pushMessageService.createEntity(new PushMessageEntity().
							setMessageId(messageId).setPayLoad(payLoad).
							setAction(action).setPushId(entity.getPushId()).
							setSendStatus(PushMessageStatus.SENT.getName()));
				}
			}
			Set<Entry<String, String>> set = messages.entrySet();
			for (Entry<String, String> entry : set) {
				String value = entry.getValue();
				try {
					ccsClient.send(value);
				} catch(Exception e) {
					pushMessageService.updateStatus(entry.getKey(), PushMessageStatus.PENDING.getName());
				}
			}
		}
	}
	
	private void onFCMReconnectSuccessful() {
		Example<PushMessageEntity> example = Example.of(new PushMessageEntity().setSendStatus("PENDING"));
		Page<PushMessageEntity> page;
		do {
			page = pushMessageService.findExamplePaginated(example, 0, 10);
			List<PushMessageEntity> list = page.getContent();
			for (PushMessageEntity item : list) {
				try {
					ccsClient.send(item.getPayLoad());
					item.setSendStatus("SENT");
					pushMessageService.update(item);
				} catch (Exception e) {
					
				}
			}
		} while(page != null && !page.isLast()); 
	}
}
