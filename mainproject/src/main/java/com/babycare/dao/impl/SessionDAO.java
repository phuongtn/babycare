package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.ISessionDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.UserConstant;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.payload.Session;
import com.wedevol.xmpp.server.CcsClient;

@Repository
@Component
@Qualifier("sessionDAO")
public class SessionDAO extends AbstractJpaDao<SessionEntity> implements ISessionDAO {
	@Autowired
	@Qualifier("CcsClient")
	private CcsClient ccsClient;

	private static Logger LOG = Logger.getLogger(SessionDAO.class);

	public SessionDAO() {
		super();
		setClazz(SessionEntity.class);
	}

	@Override
	public BaseModel updateSessionBySessionId(Session session) {
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
						sessionEntityUpdated = updateEntity(new SessionEntity(session));
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
		return null;
	}

	@Override
	public BaseModel updateSession(Session session) {
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

	@Override
	public BaseModel addSession(Session session) {
		String hardwareId = session.getHardwareId();
		String platform = session.getPlatform();
		Integer status = session.getStatus();
		if (StringUtils.isNotEmpty(hardwareId) || StringUtils.isNotEmpty(platform)
				 || status != null) {
			SessionEntity sessionEntityAdded;
			String exception = null;
			session.setSessionId(null);
			try {
				sessionEntityAdded = createEntity(new SessionEntity(session));
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

	@Override
	public BaseModel addSession(Long userId, Session session) {
		return null;
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
		SessionEntity sessionUpdated = null;
		String exception = null;
		if (status ==  UserConstant.Status.SIGNIN.getValue()) {
			try {
				entity.setStatus(status);
				sessionUpdated = updateEntity(entity);
			} catch(Exception e) {
				return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_STATUS, exception);
			}
		} else {
			try {
				delete(entity);
				sessionUpdated = entity;
			} catch (Exception e) {
				sessionUpdated = null;
				exception = e.getMessage();
			}
		}
		if (sessionUpdated != null) {
			return sessionUpdated;
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_SESSION_STATUS, exception);
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

	@Override
	public BaseModel getSessionListByUserId(Long userId) {
		String hql = "FROM SessionEntity WHERE user.userId = ?";
		try {
			List<BaseModel> result = (List<BaseModel>)em.createQuery(hql).setParameter(0, userId).getResultList();
			if (result != null || !result.isEmpty()) {
				ResultList<BaseModel> resultList = new ResultList<BaseModel>(result);
				return resultList;
			} else {
				return new ResultList<BaseModel>(new ArrayList<>());
			}
		} catch (Exception e) {
			return ErrorConstant.getError(ErrorConstant.ERROR_FETCH_SESSION);
		}
	}
}
