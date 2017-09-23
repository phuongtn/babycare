package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.User;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IUserService;

@Service(value = "userService")
public class UserService extends AbstractJpaService<User> implements IUserService {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;

	@Override
	public User createEntity(User entity) {
		return userDao.createEntity(entity);
	}

	@Override
	protected IOperations<User> getDao() {
		return userDao;
	}
	
	public UserService() {
		super();
	}

	@Override
	public User getUserByUserId(Long userId) {
		return userDao.findOne(userId);
	}

	@Override
	public BaseModel updatePushID(User user) {
		return userDao.updatePushID(user);
	}

	@Override
	public BaseModel Login(User user) {
		return userDao.Login(user);
	}

	@Override
	public BaseModel LogOut(User user) {
		return userDao.LogOut(user);
	}

	@Override
	public BaseModel register(User user) {
		return userDao.register(user);
	}

}