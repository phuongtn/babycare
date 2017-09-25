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
			String pushId = session.getPushId();
			Integer status = session.getStatus();
			Long sessionId = session.getSessionId();
			if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform)
					&& StringUtils.isNotEmpty(pushId) && status != null) {
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
				return updateSessionWithSessionId(session);
			} else {
				BaseModel sessionEntity = getSession(session);
				if (sessionEntity instanceof Error) {
					return addSession(userId, session);
				} else {
					String hardwareId = session.getHardwareId();
					String platform = session.getPlatform();
					String pushId = session.getPushId();
					Integer status = session.getStatus();
					if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform)
							&& StringUtils.isNotEmpty(pushId) && status != null) {
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
	public BaseModel getSession(Session session) {
		if (session != null) {
			String hardwareId = session.getHardwareId();
			String platform = session.getPlatform();
			Long userId = session.getUserId();
			if (StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(platform) && userId != null) {
				String hql = "FROM Session WHERE hardwareId = ? AND platform = ? AND userId = ?";
				Session entity = null;
				try {
					entity = (Session) em.createQuery(hql).setParameter(0, hardwareId).setParameter(1, platform)
							.setParameter(2, userId).getSingleResult();
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
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			}
		} else {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		}
	}

}
