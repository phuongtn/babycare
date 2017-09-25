package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.babycare.dao.IOperations;
import com.babycare.dao.ISessionDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.Session;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.ISessionService;


@Service(value = "sessionService")
public class SessionService  extends AbstractJpaService<Session> implements ISessionService{

	@Autowired
	@Qualifier("sessionDAO")
	private ISessionDAO sessionDAO;
	@Override
	public BaseModel addOrUpdateSession(Session session) {
		return sessionDAO.addOrUpdateSession(session) ;
	}

	@Override
	public BaseModel getSession(Session session) {
		return sessionDAO.getSession(session);
	}

	@Override
	public Session createEntity(Session entity) {
		return sessionDAO.createEntity(entity);
	}

	@Override
	protected IOperations<Session> getDao() {
		return sessionDAO;
	}

	@Override
	public Session updateEntity(Session entity) {
		return sessionDAO.updateEntity(entity);
	}

}
