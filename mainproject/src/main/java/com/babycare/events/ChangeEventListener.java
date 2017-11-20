package com.babycare.events;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.babycare.model.entity.ContentTypeEntity;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.ContentType;
import com.babycare.utils.DateTimeUtils;
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
			} else if (baseModel instanceof FCMReconnectSuccessEvent) {
				onFCMReconnectSuccessful();
			} else if (baseModel instanceof RemoveUserEvent) {
				onRemoveUserEvent((RemoveUserEvent)baseModel);
			} else if (baseModel instanceof CleanUpPushMessageEvent) {
				onCleanUpPushMessageEvent();
			} else if (baseModel instanceof PushContentMessageEvent) {
				
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
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_ACTION, action);
					dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE_ID, messageId);
					CcsOutMessage ccsOutMessage = PushMessageFactory.
							createSimpleCCsOutMessage(entity.getPushId(), messageId, dataPayload);
					String payLoad = PushMessageFactory.createMessagePayLoad(ccsOutMessage);
					messages.put(messageId, payLoad);
					pushMessageService.createEntity(new PushMessageEntity().
							setMessageId(messageId).setPayLoad(payLoad).
							setAction(action).setPushId(entity.getPushId()).
							setSendStatus(PushMessageStatus.SENT.getName()).
							setSessionId(entity.getSessionId()));
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
		Page<PushMessageEntity> page = pushMessageService.findExamplePaginated(example, 0, 10);
		for(;;) {
			List<PushMessageEntity> list = page.getContent();
			if (list != null && !list.isEmpty()) {
				for (PushMessageEntity item : list) {
					try {
						ccsClient.send(item.getPayLoad());
						item.setSendStatus("SENT");
						pushMessageService.update(item);
					} catch (Exception e) {
						
					}
				}
				if (page.hasNext()) {
					page = pushMessageService.findExamplePaginated(example, page.nextPageable());
				} else {
					break;
				}
			} else {
				break;
			}
		}
	/*	
		do {
			List<PushMessageEntity> list = page.getContent();
			if (list != null && !list.isEmpty()) {
				for (PushMessageEntity item : list) {
					try {
						ccsClient.send(item.getPayLoad());
						item.setSendStatus("SENT");
						pushMessageService.update(item);
					} catch (Exception e) {
						
					}
				}
				if (page.hasNext()) {
					page = pushMessageService.findExamplePaginated(example, page.nextPageable());
				}
			}
		} while (page != null && !page.isLast()); */
	}

	private void onRemoveUserEvent(RemoveUserEvent event) {
		Set<SessionEntity> sessions = event.getSessions();
		if (sessions != null && !sessions.isEmpty()) {
			for (SessionEntity sessionEntity : sessions) {
				pushMessageService.deleteMessageBySessionId(sessionEntity.getSessionId());
			}
		}
	}
	
	private void onCleanUpPushMessageEvent() {
		pushMessageService.cleanUpPushMessage();
	}
	
	private void onPushContentMessageEvent() {
		Page<UserEntity> page = userService.findPaginated(0, 10);
		List<ContentTypeEntity> contentList = contentTyeService.findAll();
		for(;;) {
			List<UserEntity> list = page.getContent();
			if (list != null && !list.isEmpty()) {
				for (UserEntity item : list) {
					Set<ChildEntity> children = item.getChildEntities();
					if (children != null && !children.isEmpty()) {
						for (ChildEntity child : children) {
							Long dob = child.getDob();
							if (dob != null) {
								int weekDiff = DateTimeUtils.getWeekDiffFromNow(dob);
								BaseModel baseModel = sessionService.getSessionListByUserId(child.getUserId());
								if (baseModel instanceof ResultList) {
									@SuppressWarnings("unchecked")
									ResultList<SessionEntity> sessionContainer = (ResultList<SessionEntity>) baseModel;
									List<SessionEntity> sessions = sessionContainer.getResultList();
									if (sessions != null && !sessions.isEmpty()) {
									
									}
								}
							}

						}
					}
				}
				if (page.hasNext()) {
					page = userService.findPaginated(page.nextPageable());
				} else {
					break;
				}
			} else {
				break;
			}
		} 
	}

}
