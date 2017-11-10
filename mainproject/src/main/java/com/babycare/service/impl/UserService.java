package com.babycare.service.impl;

import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.ISessionDAO;
import com.babycare.dao.IUserDao;
import com.babycare.events.ChangeEvent;
import com.babycare.events.RemoveUserEvent;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IUserService;

@Service(value = "userService")
public class UserService extends AbstractJpaService<UserEntity> implements IUserService, ApplicationEventPublisherAware {
	public static final Logger logger = Logger.getLogger(UserService.class.getName());
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;

	@Autowired
	@Qualifier("sessionDAO")
	private ISessionDAO sessionDAO;

	private ApplicationEventPublisher publisher;

	@Override
	public BaseModel register(User user) {
		return userDao.register(user);
	}

	@Override
	public UserEntity createEntity(UserEntity entity) {
		return userDao.createEntity(entity);
	}

	@Override
	protected IOperations<UserEntity> getDao() {
		return userDao;
	}

	@Override
	public UserEntity updateEntity(UserEntity entity) {
		UserEntity model = userDao.updateEntity(entity);
		return model;
	}

	@Override
	public BaseModel updateByEmailAndProvider(User user) {
		BaseModel model = userDao.updateByEmailAndProvider(user);
		publishEvent(user.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel getUserByUserId(User user) {
		BaseModel model = userDao.getUserByUserId(user);
		return model;
	}

	@Override
	public BaseModel getUserByEmailAndProvider(User payload) {
		BaseModel model = userDao.getUserByEmailAndProvider(payload);
		return model;
	}

	@Override
	public BaseModel updateUserByUserId(User user) {
		BaseModel model = userDao.updateUserByUserId(user);
		publishEvent(user.getRequestBySessionId(), model);
		return model;
	}
	
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	private void publishEvent(Long requestId, BaseModel model) {
		if (model instanceof UserEntity) {
			model.setRequestBySessionId(requestId);
			publisher.publishEvent(new ChangeEvent(model));
		}
	}

	@Override
	public BaseModel deleteUser(User user) {
		UserEntity entity = null;
		if (user != null && user.getUserId() != null) {
			 entity = userDao.findOne(user.getUserId());
			if (entity != null) {
				Set<SessionEntity> sessions = entity.getSessionEntities();
				if (sessions != null && !sessions.isEmpty()) {
					publisher.publishEvent(new ChangeEvent(new RemoveUserEvent(sessions)));
				}
			}
		}
		//return entity;
		return userDao.deleteUser(user);
	}
}
