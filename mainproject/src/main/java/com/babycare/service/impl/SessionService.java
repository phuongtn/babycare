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
public class SessionService extends AbstractJpaService<Session> implements ISessionService {
	@Autowired
	@Qualifier("sessionDAO")
	private ISessionDAO sessionDAO;
	@Override
	public BaseModel addOrUpdateSession(Session session) {
		return sessionDAO.addOrUpdateSession(session) ;
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

	@Override
	public BaseModel getSessionBySessionId(Session session) {
		return sessionDAO.getSessionBySessionId(session);
	}

	@Override
	public BaseModel getSessionByHardwareId(Session session) {
		return sessionDAO.getSessionByHardwareId(session);
	}

	@Override
	public BaseModel loginBySessionId(Session session) {
		return sessionDAO.loginBySessionId(session);
	}

	@Override
	public BaseModel logoutBySessionId(Session session) {
		return sessionDAO.logoutBySessionId(session);
	}

	@Override
	public BaseModel loginByHardwareId(Session session) {
		return sessionDAO.loginByHardwareId(session);
	}

	@Override
	public BaseModel logoutByHardwareId(Session session) {
		return sessionDAO.logoutByHardwareId(session);
	}

	@Override
	public BaseModel updatePushIdBySessionId(Session session) {
		return sessionDAO.updatePushIdBySessionId(session);
	}

	@Override
	public BaseModel updatePushIdByHardwareId(Session session) {
		return sessionDAO.updatePushIdByHardwareId(session);
	}

}
