package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.babycare.dao.IOperations;
import com.babycare.dao.ISessionDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.payload.Session;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.ISessionService;


@Service(value = "sessionService")
public class SessionService extends AbstractJpaService<SessionEntity> implements ISessionService {
	@Autowired
	@Qualifier("sessionDAO")
	private ISessionDAO sessionDAO;
	@Override
	public BaseModel addOrUpdateSession(Session session) {
		return sessionDAO.addOrUpdateSession(session) ;
	}

	@Override
	public SessionEntity createEntity(SessionEntity entity) {
		return sessionDAO.createEntity(entity);
	}

	@Override
	protected IOperations<SessionEntity> getDao() {
		return sessionDAO;
	}

	@Override
	public SessionEntity updateEntity(SessionEntity entity) {
		return sessionDAO.updateEntity(entity);
	}

	@Override
	public BaseModel getSessionBySessionId(Session payload) {
		return sessionDAO.getSessionBySessionId(payload);
	}

	@Override
	public BaseModel getSessionByHardwareId(Session payload) {
		return sessionDAO.getSessionByHardwareId(payload);
	}

	@Override
	public BaseModel loginBySessionId(Session payload) {
		return sessionDAO.loginBySessionId(payload);
	}

	@Override
	public BaseModel logoutBySessionId(Session payload) {
		return sessionDAO.logoutBySessionId(payload);
	}

	@Override
	public BaseModel loginByHardwareId(Session payload) {
		return sessionDAO.loginByHardwareId(payload);
	}

	@Override
	public BaseModel logoutByHardwareId(Session payload) {
		return sessionDAO.logoutByHardwareId(payload);
	}

	@Override
	public BaseModel updatePushIdBySessionId(Session payload) {
		return sessionDAO.updatePushIdBySessionId(payload);
	}

	@Override
	public BaseModel updatePushIdByHardwareId(Session payload) {
		return sessionDAO.updatePushIdByHardwareId(payload);
	}

}
