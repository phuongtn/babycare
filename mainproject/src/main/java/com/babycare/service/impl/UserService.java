package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IUserService;

@Service(value = "userService")
public class UserService extends AbstractJpaService<UserEntity> implements IUserService {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;

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
		return userDao.updateEntity(entity);
	}

	@Override
	public BaseModel updateByEmailAndProvider(User user) {
		return userDao.updateByEmailAndProvider(user);
	}

	@Override
	public BaseModel getUserByUserId(User user) {
		return userDao.getUserByUserId(user);
	}

	@Override
	public BaseModel getUserByEmailAndProvider(User payload) {
		return userDao.getUserByEmailAndProvider(payload);
	}

	@Override
	public BaseModel updateUserByUserId(User user) {
		return userDao.updateUserByUserId(user);
	}

}
