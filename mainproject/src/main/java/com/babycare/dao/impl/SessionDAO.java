package com.babycare.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.ISessionDAO;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.UserConstant;
import com.babycare.model.entity.Session;
import com.babycare.model.entity.User;

@Repository
@Transactional
@Qualifier("sessionDAO")
public class SessionDAO extends AbstractJpaDao<Session> implements ISessionDAO {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;
	private static Logger LOG = Logger.getLogger(SessionDAO.class);

	public SessionDAO() {
		super();
		setClazz(Session.class);
	}

	private BaseModel updateSessionWithSessionId(Session session) {
		if (session == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			String hardwareId = session.getHardwareId();
			String platform = session.getPlatform();
			//String pushId = session.getPushId();
			Integer status = session.getStatus();
			Long sessionId = session.getSessionId();
			if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform) && status != null) {
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
							return new Error(ErrorConstant.ERROR_UPDATE_SESSION, ErrorConstant.ERROR_UPDATE_SESSION_MESSAGE,
									exception);
						} else {
							return new Error(ErrorConstant.ERROR_UPDATE_SESSION, ErrorConstant.ERROR_UPDATE_SESSION_MESSAGE);
						}
					}
				} else {
					return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST,
							ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE);
				}
				
			} else {
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			}
			
		}
	}

	@Override
	public BaseModel addOrUpdateSession(Session session) {
		if (session == null) {
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			Long userId = session.getUserId();
			if (userId == null) {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			} else if (session.getSessionId() != null) {
				// Update if sessionId != null
				return updateSessionWithSessionId(session);
			} else {
				BaseModel sessionEntity = getSessionByHardwareId(session);
				if (sessionEntity instanceof Error) {
					// Add session id not found session by hardwareId
					return addSession(userId, session);
				} else {
					// Add session id not found session by hardwareId
					String hardwareId = session.getHardwareId();
					String platform = session.getPlatform();
					String pushId = session.getPushId();
					Integer status = session.getStatus();
					if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform) && status != null) {
						session.setUser(((Session) sessionEntity).getUser());
						session.setSessionId(((Session) sessionEntity).getSessionId());
						return updateSession(session);
					} else {
						return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
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
			return new Error(ErrorConstant.ERROR_UPDATE_SESSION, ErrorConstant.ERROR_UPDATE_SESSION_MESSAGE);
		}
	}

	private BaseModel addSession(Long userId, Session session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		String pushId = session.getPushId();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform)
				&& StringUtils.isNotEmpty(pushId) && status != null) {
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
						return new Error(ErrorConstant.ERROR_ADD_SESSION, ErrorConstant.ERROR_ADD_SESSION_MESSAGE,
								exception);
					} else {
						return new Error(ErrorConstant.ERROR_ADD_SESSION, ErrorConstant.ERROR_ADD_SESSION_MESSAGE);
					}
				}
			} else {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			}
		} else {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		}
	}

	@Override
	public BaseModel getSessionBySessionId(Session session) {
		if (session == null) {
			return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST, ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE); 
		} else {
			Long sessionId = session.getSessionId();
			if (sessionId != null) {
				Session sessionEntity = findOne(sessionId);
				if (sessionEntity != null) {
					return sessionEntity;
				} else {
					return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST, ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE);
				}
			} else {
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			}
		}
	}

	@Override
	public BaseModel getSessionByHardwareId(Session session) {
		if (session == null) {
			return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST, ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE); 
		} else {
			String hardwareId = session.getHardwareId();
			if (StringUtils.isNotEmpty(hardwareId)) {
				String hql = "FROM Session WHERE hardwareId = ?";
				Session entity = null;
				try {
					entity = (Session) em.createQuery(hql).setParameter(0, hardwareId).getSingleResult();
				} catch (Exception e) {
					entity = null;
					return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST,
							ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE, e.getMessage());
				} 
				if (entity != null) {
					entity.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : (Long) null);
					return entity;
				} else {
					return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST,
							ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE);
				}
			} else {
				return new Error(ErrorConstant.ERROR_SESSION_NOT_EXIST, ErrorConstant.ERROR_SESSION_NOT_EXIST_MESSAGE); 
			}
		}
	}

	@Override
	public BaseModel loginBySessionId(Session session) {
		BaseModel model = getSessionBySessionId(session);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutBySessionId(Session session) {
		BaseModel model = getSessionBySessionId(session);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNOUT.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel loginByHardwareId(Session session) {
		BaseModel model = getSessionByHardwareId(session);
		if (model instanceof Session) {
			return updateSessionStatus((Session) model, UserConstant.Status.SIGNIN.getValue());
		} else {
			return model;
		}
	}

	@Override
	public BaseModel logoutByHardwareId(Session session) {
		BaseModel model = getSessionByHardwareId(session);
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
				return new Error(ErrorConstant.ERROR_UPDATE_SESSION_STATUS, ErrorConstant.ERROR_UPDATE_SESSION_STATUS_MESSAGE, exception);
			} else {
				return new Error(ErrorConstant.ERROR_UPDATE_SESSION_STATUS, ErrorConstant.ERROR_UPDATE_SESSION_STATUS_MESSAGE);
			}
		}
	}

	@Override
	public BaseModel updatePushIdBySessionId(Session session) {
		BaseModel model = getSessionBySessionId(session);
		return updatePushId(model, session.getPushId());
	}

	@Override
	public BaseModel updatePushIdByHardwareId(Session session) {
		BaseModel model = getSessionByHardwareId(session);
		updatePushId(model, session.getPushId());
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
					return new Error(ErrorConstant.ERROR_UPDATE_SESSION_PUSHID, ErrorConstant.ERROR_UPDATE_SESSION_PUSHID_MESSAGE, exception);
				} else {
					return new Error(ErrorConstant.ERROR_UPDATE_SESSION_PUSHID, ErrorConstant.ERROR_UPDATE_SESSION_PUSHID_MESSAGE);
				}
			}
		} else {
			return model;
		}
	}

}
