package com.babycare.dao.impl;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.ISessionDAO;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.UserConstant;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.Session;

@Repository
@Qualifier("sessionDAO")
public class SessionDAO extends AbstractJpaDao<SessionEntity> implements ISessionDAO {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;
	/*private static Logger LOG = Logger.getLogger(SessionDAO.class);*/

	public SessionDAO() {
		super();
		setClazz(SessionEntity.class);
	}

	private BaseModel updateSessionWithSessionId(SessionEntity session) {
		if (session == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			String hardwareId = session.getHardwareId();
			String platform = session.getPlatform();
			Integer status = session.getStatus();
			Long sessionId = session.getSessionId();
			if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform) || status != null) {
				String exception = null;
				SessionEntity sessionEntityUpdated = null;
				SessionEntity sessionEntity = findOne(sessionId);
				if (sessionEntity != null) {
					session.setUser(sessionEntity.getUser());
					session.setSessionId(sessionEntity.getSessionId());
					try {
						sessionEntityUpdated = updateEntity(session);
					} catch (Exception e) {
						sessionEntityUpdated = null;
						exception = e.getMessage();
					}
					if (sessionEntityUpdated != null) {
						return sessionEntityUpdated;
					} else {
						if (StringUtils.isNotEmpty(exception)) {
							return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION, exception);
						} else {
							return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION);
						}
					}
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
				}
				
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
			}
			
		}
	}

	@Override
	public BaseModel addOrUpdateSession(Session session) {
		if (session == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			if (session.getUser() != null && session.getUser().getUserId() != null) {
				BaseModel sessionEntity = getSessionByHardwareId(session.getHardwareId());
				if (sessionEntity instanceof Error) {
					// Session not found so, add session
					return addSession(new SessionEntity(session));
				} else {
					// sessionId not null, try to add Session with Id
					return updateSession(session);
				}
			} else {
				Long userId = session.getUserId();
				if (userId == null) {
					return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
				} else if (session.getSessionId() != null) {
					// Update if sessionId != null
					return updateSessionWithSessionId(new SessionEntity(session));
				} else {
					BaseModel sessionEntity = getSessionByHardwareId(session.getHardwareId());
					if (sessionEntity instanceof Error) {
						// Add session id not found session by hardwareId
						return addSession(userId, new SessionEntity(session));
					} else {
						// Add session id not found session by hardwareId
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

	private BaseModel updateSession(Session session) {
		SessionEntity sessionEntityUpdated = null;
		try {
			sessionEntityUpdated = updateEntity(new SessionEntity(session));
		} catch (Exception e) {
			sessionEntityUpdated = null;
		}
		if (sessionEntityUpdated != null) {
			sessionEntityUpdated.setUserId(session.getUserId());
			return sessionEntityUpdated;
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION);
		}
	}

	private BaseModel addSession(SessionEntity session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform)
				 || status != null) {
			SessionEntity sessionEntityAdded;
			String exception = null;
			session.setSessionId(null);
			try {
				sessionEntityAdded = createEntity(session);
				sessionEntityAdded.setUserId(session.getUserId());
			} catch(Exception e) {
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
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	private BaseModel addSession(Long userId, SessionEntity session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform)
				 || status != null) {
			UserEntity userEntity = null;
			String exception = null;
			SessionEntity sessionEntityAdded;
			try {
				userEntity = userDao.findOne(userId);
			} catch (Exception e) {
				exception = e.getMessage();
			}
			if (userEntity != null) {
				session.setUser(userEntity);
				try {
					sessionEntityAdded = createEntity(session);
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

	@Override
	public BaseModel getSessionBySessionId(Session payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
		} else {
			Long sessionId = payload.getSessionId();
			BaseModel model = getSessionById(sessionId);
			if (model instanceof SessionEntity) {
				SessionEntity entity = (SessionEntity) model;
				entity.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
				return entity;
			} else {
				return model;
			}
		}
	}

	private BaseModel getSessionById(Long id) {
		if (id != null) {
			SessionEntity sessionEntity = findOne(id);
			if (sessionEntity != null) {
				sessionEntity.setUserId(sessionEntity.getUser() != null? sessionEntity.getUser().getUserId() : null);
				return sessionEntity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	@Override
	public BaseModel getSessionByHardwareId(Session payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
		} else {
			String hardwareId = payload.getHardwareId();
			return getSessionByHardwareId(hardwareId);
		}
	}

	private BaseModel getSessionByHardwareId(String hardwareId) {
		if (StringUtils.isNotEmpty(hardwareId)) {
			String hql = "FROM SessionEntity WHERE hardwareId = ?";
			SessionEntity entity = null;
			try {
				entity = (SessionEntity) em.createQuery(hql).setParameter(0, hardwareId).getSingleResult();
			} catch (Exception e) {
				entity = null;
				return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST, e.getMessage());
			} 
			if (entity != null) {
				entity.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : (Long) null);
				return entity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
		}
	}

	@Override
	public BaseModel loginBySessionId(Session payload) {
		BaseModel model = getSessionBySessionId(payload);
		if (model instanceof SessionEntity) {
			return updateSessionStatus((SessionEntity) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutBySessionId(Session session) {
		BaseModel model = getSessionBySessionId(session);
		if (model instanceof SessionEntity) {
			return updateSessionStatus((SessionEntity) model, UserConstant.Status.SIGNOUT.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel loginByHardwareId(Session payload) {
		BaseModel model = getSessionByHardwareId(payload);
		if (model instanceof SessionEntity) {
			return updateSessionStatus((SessionEntity) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutByHardwareId(Session payload) {
		BaseModel model = getSessionByHardwareId(payload);
		if (model instanceof SessionEntity) {
			return updateSessionStatus((SessionEntity) model, UserConstant.Status.SIGNOUT.getValue());
		} else {
			return model;
		}
	}

	private BaseModel updateSessionStatus(SessionEntity entity, Integer status) {
		entity.setStatus(status);
		Session sessionUpdated = null;
		String exception = null;
		try {
			sessionUpdated = updateEntity(entity);
		} catch (Exception e) {
			sessionUpdated = null;
			exception = e.getMessage();
		}
		if (sessionUpdated != null) {
			return sessionUpdated;
		} else {
			if (StringUtils.isNotEmpty(exception)) {
				return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_STATUS, exception);
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_STATUS);
			}
		}
	}

	@Override
	public BaseModel updatePushIdBySessionId(Session payload) {
		BaseModel model = getSessionBySessionId(payload);
		return updatePushId(model, payload.getPushId());
	}

	@Override
	public BaseModel updatePushIdByHardwareId(Session payload) {
		BaseModel model = getSessionByHardwareId(payload);
		updatePushId(model, payload.getPushId());
		return null;
	}

	private BaseModel updatePushId(BaseModel model, String pushId) {
		if (model instanceof SessionEntity) {
			SessionEntity sessionEntity = (SessionEntity) model;
			sessionEntity.setPushId(pushId);
			Session sessionUpdated = null;
			String exception = null;
			try {
				sessionUpdated = updateEntity(sessionEntity);
			} catch(Exception e) {
				sessionUpdated = null;
				exception = e.getMessage();
			}
			if (sessionUpdated != null) {
				return sessionUpdated;
			} else {
				if (StringUtils.isNotEmpty(exception)) {
					return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_PUSHID, exception);
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_PUSHID, exception);
				}
			}
		} else {
			return model;
		}
	}

}
