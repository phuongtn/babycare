package com.babycare.dao;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;

public interface IPushMessageDAO extends IOperations<PushMessageEntity> {
	BaseModel getByMessageId(String messageId);
	int deleteByMessageId(String messageId);
}
