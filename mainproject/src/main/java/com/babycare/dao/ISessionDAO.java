package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Session;
import com.babycare.model.payload.SessionPayload;

public interface ISessionDAO extends IOperations<Session> {
	BaseModel addOrUpdateSession(Session session);
	BaseModel getSessionBySessionId(SessionPayload payload);
	BaseModel getSessionByHardwareId(SessionPayload payload);
	BaseModel loginBySessionId(SessionPayload payload);
	BaseModel logoutBySessionId(SessionPayload payload);
	BaseModel loginByHardwareId(SessionPayload payload);
	BaseModel logoutByHardwareId(SessionPayload payload);
	BaseModel updatePushIdBySessionId(SessionPayload payload);
	BaseModel updatePushIdByHardwareId(SessionPayload payload);
}
