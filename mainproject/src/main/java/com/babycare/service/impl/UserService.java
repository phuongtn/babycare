package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IUserDao;
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
	public User updatePushIdByHardwareIdAndProvider(String hardwareId, String provider, String pushId) {
		return userDao.updatePushIdByHardwareIdAndProvider(hardwareId, provider, pushId);
	}

	@Override
	public User updatePushIdByUserId(Long userId, String pushId) {
		return userDao.updatePushIdByUserId(userId, pushId);
	}

	@Override
	public User updateSignInStatusByUserId(Long userId, Integer status) {
		return userDao.updateSignInStatusByUserId(userId, status);
	}

	@Override
	public User updateSignInStatusByHardwareIdAndProvider(String hardwareId, String provider, Integer status) {
		return userDao.updateSignInStatusByHardwareIdAndProvider(hardwareId, provider, status);
	}

	@Override
	public User getUserByHardwareIdAndProvider(String hardwareId, String provider) {
		return userDao.getUserByHardwareIdAndProvider(hardwareId, provider);
	}

	@Override
	public User signIn(User user) {
		return userDao.signIn(user);
	}

	@Override
	public User signOut(User user) {
		return userDao.signOut(user);
	}

	@Override
	public User getUserByUserId(Long userId) {
		return userDao.findOne(userId);
	}
}
