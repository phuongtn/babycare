package com.babycare.dao;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;

public interface IPushMessageDAO extends IPagingOperations<PushMessageEntity> {
	BaseModel getByMessage(PushMessage messageId);
	BaseModel deleteMessage(PushMessage messageId);
	BaseModel deleteMessageByPushID(String pushId);
	BaseModel updateStatus(String messageId, String status);
	BaseModel deleteMessageByMessageId(String messageId);
	BaseModel deleteMessageBySessionId(Long sessionId);
}
