package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Session;

public interface ISessionDAO extends IOperations<Session> {
	BaseModel addOrUpdateSession(Session session);
	BaseModel getSessionBySessionId(Session session);
	BaseModel getSessionByHardwareId(Session session);
	BaseModel loginBySessionId(Session session);
	BaseModel logoutBySessionId(Session session);
	BaseModel loginByHardwareId(Session session);
	BaseModel logoutByHardwareId(Session session);
}
