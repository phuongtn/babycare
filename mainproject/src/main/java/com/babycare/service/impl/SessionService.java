package com.babycare.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.ISessionDAO;
import com.babycare.events.ChangeEvent;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.Session;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.ISessionService;
import com.babycare.service.IUserService;


@Service(value = "sessionService")
public class SessionService extends AbstractJpaService<SessionEntity> implements ISessionService, ApplicationEventPublisherAware{
	@Autowired
	@Qualifier("sessionDAO")
	private ISessionDAO sessionDAO;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	private ApplicationEventPublisher publisher;

	@Override
	public BaseModel addOrUpdateSession(Session session) {
		if (session == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			if (session.getUser() != null && session.getUser().getUserId() != null) {
				BaseModel sessionEntity = sessionDAO.getSessionByHardwareId(session);
				if (sessionEntity instanceof Error) {
					// Session not found so, add session
					return addSession(session);
				} else {
					// sessionId not null, try to add Session with Id
					SessionEntity entity = (SessionEntity)sessionEntity;
					session.setSessionId(entity.getSessionId());
					return updateSession(session);
				}
			} else {
				Long userId = session.getUserId();
				if (userId == null) {
					return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
				} else if (session.getSessionId() != null) {
					// Update if sessionId != null
					return updateSessionBySessionId(session);
				} else {
					BaseModel sessionEntity = getSessionByHardwareId(session);
					if (sessionEntity instanceof Error) {
						// Add session id not found session by hardwareId
						return addSession(userId, session);
					} else {
						// Edit session id not found session by hardwareId
						String hardwareId = session.getHardwareId();
						String platform = session.getPlatform();
						String pushId = session.getPushId();
						Integer status = session.getStatus();
						if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform) || status != null) {
							SessionEntity  updatedEntity = (SessionEntity) sessionEntity;
							updatedEntity.setHardwareId(hardwareId);
							updatedEntity.setPlatform(platform);
							updatedEntity.setPushId(pushId);
							updatedEntity.setStatus(status);
							updatedEntity.setUserId(userId);
							return updateSession(updatedEntity);
						} else {
							return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
						}
					}
				}
			}
		}
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
		BaseModel model = sessionDAO.loginBySessionId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel logoutBySessionId(Session payload) {
		BaseModel model = sessionDAO.logoutBySessionId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel loginByHardwareId(Session payload) {
		BaseModel model = sessionDAO.loginByHardwareId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel logoutByHardwareId(Session payload) {
		BaseModel model = sessionDAO.logoutByHardwareId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel updatePushIdBySessionId(Session payload) {
		BaseModel model = sessionDAO.updatePushIdBySessionId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel updatePushIdByHardwareId(Session payload) {
		BaseModel model = sessionDAO.updatePushIdByHardwareId(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel getSessionListByUserId(Long userId) {
		return sessionDAO.getSessionListByUserId(userId);
	}

	@Override
	public BaseModel addSession(Session session) {
		BaseModel model = sessionDAO.addSession(session);
		publishEvent(session.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel updateSession(Session session) {
		BaseModel model = sessionDAO.updateSession(session);
		publishEvent(session.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel updateSessionBySessionId(Session session) {
		BaseModel model = sessionDAO.updateSessionBySessionId(session);
		publishEvent(session.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel addSession(Long userId, Session session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform)
				 || status != null) {
			UserEntity userEntity = null;
			String exception = null;
			SessionEntity sessionEntityAdded;
			try {
				userEntity = userService.findOne(userId);
			} catch (Exception e) {
				exception = e.getMessage();
			}
			if (userEntity != null) {
				session.setUser(userEntity);
				try {
					sessionEntityAdded = createEntity(new SessionEntity(session));
				} catch (Exception e) {
					sessionEntityAdded = null;
					exception = e.getMessage();
				}
				if (sessionEntityAdded != null) {
					return sessionEntityAdded;
				} else {
					if (StringUtils.isNotEmpty(exception)) {
						return ErrorConstant.getError(ErrorConstant.ERROR_ADD_SESSION, exception);
					} else {
						return ErrorConstant.getError(ErrorConstant.ERROR_ADD_SESSION);
					}
				}
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	private void publishEvent(Long requestId, BaseModel model) {
		if (model instanceof SessionEntity) {
			model.setRequestBySessionId(requestId);
			publisher.publishEvent(new ChangeEvent(model));
		}
	}
}
