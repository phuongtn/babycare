package com.babycare.dao;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;
import com.babycare.model.response.CommonResponse;

public interface IPushMessageDAO extends IOperations<PushMessageEntity> {
	BaseModel getByMessage(PushMessage messageId);
	BaseModel deleteMessage(PushMessage messageId);
	BaseModel deleteMessageByPushID(String pushId);
}
