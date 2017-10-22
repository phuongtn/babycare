package com.babycare.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import com.babycare.model.BaseModel;
import com.babycare.model.MessageConstant;
import com.babycare.model.ResultList;
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
				onAccountChange((UserEntity) baseModel);
			}
		}
	}

	private void onAccountChange(UserEntity userEntity) {
		Long sessionId = userEntity.getRequestBySessionId();
		BaseModel baseModel = sessionService.getSessionListByUserId(userEntity.getUserId());
		if (baseModel instanceof ResultList) {
			com.babycare.model.ResultList<SessionEntity> resultList = (ResultList) baseModel;
			List<SessionEntity> list = resultList.getResultList();
			for (SessionEntity entity : list) {
				if (entity.getSessionId() != sessionId) {
					String messageId = Util.getUniqueMessageId();
					Map<String, String> dataPayload = new HashMap<String, String>();
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "User updated");
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_ACTION,
							MessageConstant.MessageAction.ACCOUNT_CHANGE.getName());
					CcsOutMessage ccsOutMessage = PushMessageFactory.
							createSimpleCCsOutMessage(entity.getPushId(), messageId, dataPayload);
					pushMessageService.createEntity(new PushMessageEntity().
							setMessageId(messageId).setPayLoad(PushMessageFactory.createMessagePayLoad(ccsOutMessage)).
							setAction(MessageConstant.MessageAction.ACCOUNT_CHANGE.getName()));
				}
			}
		}
	}
}
