package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Session;

public interface ISessionDAO extends IOperations<Session> {
	BaseModel addOrUpdateSession(Session session);
	BaseModel getSession(Session session);
}
