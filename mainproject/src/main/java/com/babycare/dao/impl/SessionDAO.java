package com.babycare.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.babycare.model.entity.Session;
import com.babycare.model.entity.User;
import com.babycare.model.payload.SessionPayload;

@Repository
@Qualifier("sessionDAO")
public class SessionDAO extends AbstractJpaDao<Session> implements ISessionDAO {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;
	/*private static Logger LOG = Logger.getLogger(SessionDAO.class);*/

	public SessionDAO() {
		super();
		setClazz(Session.class);
	}

	private BaseModel updateSessionWithSessionId(Session session) {
		if (session == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			String hardwareId = session.getHardwareId();
			String platform = session.getPlatform();
			Integer status = session.getStatus();
			Long sessionId = session.getSessionId();
			if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform) || status != null) {
				String exception = null;
				Session sessionEntityUpdated = null;
				Session sessionEntity = findOne(sessionId);
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
			Long userId = session.getUserId();
			if (userId == null) {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			} else if (session.getSessionId() != null) {
				// Update if sessionId != null
				return updateSessionWithSessionId(session);
			} else {
				BaseModel sessionEntity = getSessionByHardwareId(session.getHardwareId());
				if (sessionEntity instanceof Error) {
					// Add session id not found session by hardwareId
					return addSession(userId, session);
				} else {
					// Add session id not found session by hardwareId
					String hardwareId = session.getHardwareId();
					String platform = session.getPlatform();
					String pushId = session.getPushId();
					Integer status = session.getStatus();
					if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform) || status != null) {
						session.setUser(((Session) sessionEntity).getUser());
						session.setSessionId(((Session) sessionEntity).getSessionId());
						return updateSession(session);
					} else {
						return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
					}
				}
			}
		}

	}

	private BaseModel updateSession(Session session) {
		Session sessionEntityUpdated = updateEntity(session);
		if (sessionEntityUpdated != null) {
			return sessionEntityUpdated;
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION);
		}
	}

	private BaseModel addSession(Long userId, Session session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform)
				 || status != null) {
			User userEntity = null;
			String exception = null;
			Session sessionEntityAdded;
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
	public BaseModel getSessionBySessionId(SessionPayload payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
		} else {
			Long sessionId = payload.getSessionId();
			return getSessionById(sessionId);
		}
	}

	private BaseModel getSessionById(Long id) {
		if (id != null) {
			Session sessionEntity = findOne(id);
			if (sessionEntity != null) {
				return sessionEntity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	@Override
	public BaseModel getSessionByHardwareId(SessionPayload payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_SESSION_NOT_EXIST);
		} else {
			String hardwareId = payload.getHardwareId();
			return getSessionByHardwareId(hardwareId);
		}
	}

	private BaseModel getSessionByHardwareId(String hardwareId) {
		if (StringUtils.isNotEmpty(hardwareId)) {
			String hql = "FROM Session WHERE hardwareId = ?";
			Session entity = null;
			try {
				entity = (Session) em.createQuery(hql).setParameter(0, hardwareId).getSingleResult();
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
	public BaseModel loginBySessionId(SessionPayload payload) {
		BaseModel model = getSessionBySessionId(payload);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutBySessionId(SessionPayload session) {
		BaseModel model = getSessionBySessionId(session);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNOUT.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel loginByHardwareId(SessionPayload payload) {
		BaseModel model = getSessionByHardwareId(payload);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutByHardwareId(SessionPayload payload) {
		BaseModel model = getSessionByHardwareId(payload);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNOUT.getValue());
		} else {
			return model;
		}
	}

	private BaseModel updateSessionStatus(Session session, Integer status) {
		session.setStatus(status);
		Session sessionUpdated = null;
		String exception = null;
		try {
			sessionUpdated = updateEntity(session);
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
	public BaseModel updatePushIdBySessionId(SessionPayload payload) {
		BaseModel model = getSessionBySessionId(payload);
		return updatePushId(model, payload.getPushId());
	}

	@Override
	public BaseModel updatePushIdByHardwareId(SessionPayload payload) {
		BaseModel model = getSessionByHardwareId(payload);
		updatePushId(model, payload.getPushId());
		return null;
	}

	private BaseModel updatePushId(BaseModel model, String pushId) {
		if (model instanceof Session) {
			Session sessionEntity = (Session) model;
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
