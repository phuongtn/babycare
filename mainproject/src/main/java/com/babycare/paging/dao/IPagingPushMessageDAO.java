/*package com.babycare.paging.dao;

import java.io.Serializable;

import com.babycare.model.BaseModel;
import com.babycare.model.payload.PushMessage;
import com.babycare.paging.IPagingOperations;

public interface IPagingPushMessageDAO<E extends Serializable, K extends Object> extends IPagingOperations<E, K> {
	BaseModel getByMessage(PushMessage messageId);
	BaseModel deleteMessage(PushMessage messageId);
	BaseModel deleteMessageByPushID(String pushId);
	BaseModel updateStatus(String messageId, String status);
	BaseModel deleteMessageByMessageId(String messageId);
}
*/