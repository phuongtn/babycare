package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.payload.Session;

public interface ISessionDAO extends IOperations<SessionEntity> {
	BaseModel addOrUpdateSession(Session session);
	BaseModel getSessionBySessionId(Session payload);
	BaseModel getSessionByHardwareId(Session payload);
	BaseModel loginBySessionId(Session payload);
	BaseModel logoutBySessionId(Session payload);
	BaseModel loginByHardwareId(Session payload);
	BaseModel logoutByHardwareId(Session payload);
	BaseModel updatePushIdBySessionId(Session payload);
	BaseModel updatePushIdByHardwareId(Session payload);
}
