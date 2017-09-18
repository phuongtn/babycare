package com.babycare.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IUserDao;
import com.babycare.model.User;
import com.babycare.model.UserConstant;

@Repository
@Qualifier("userDAO")
public class UserDAO extends AbstractJpaDao<User> implements IUserDao {
	public UserDAO() {
		super();
		setClazz(User.class);
	}

	@Override
	public User getUserByHardwareIdAndProvider(String hardwareId, String provider) {
		String hql = "FROM User as usr WHERE usr.hardwareId = ? AND usr.provider = ?";
		User user = null;
		try {
			user = (User) em.createQuery(hql).
					setParameter(0, hardwareId).
					setParameter(1, provider).getSingleResult();
		} catch (Exception e) {
			user = null;
		}
		return user;
	}

	@Override
	public User updatePushIdByHardwareIdAndProvider(String hardwareId, String provider, String pushId) {
		User user = getUserByHardwareIdAndProvider(hardwareId, provider);
		if (user != null) {
			user.setPushId(pushId);
			update(user);
			return user;
			
		} else {
			return null;
		}
	}

	@Override
	public User updatePushIdByUserId(Long userId, String pushId) {
		User user = findOne(userId);
		if (user != null) {
			user.setPushId(pushId);
			update(user);
		}
		return user;
	}

	@Override
	public User updateSignInStatusByUserId(Long userId, Integer status) {
		User user = findOne(userId);
		if (user != null) {
			user.setStatus(status);
			update(user);
		}
		return user;
	}

	@Override
	public User updateSignInStatusByHardwareIdAndProvider(String hardwareId, String provider, Integer status) {
		User user = getUserByHardwareIdAndProvider(hardwareId, provider);
		if (user != null) {
			user.setStatus(status);
			update(user);
		}
		return user;
	}

	@Override
	public User signIn(User user) {
		return updateSignInStatus(user, Integer.valueOf(UserConstant.Status.SIGNIN.getValue()));
	}

	private User updateSignInStatus(User user, Integer status) {
		if (user != null) {
			Long id = user.getUserId();
			User entity = findOne(new Long(id));
			if (entity != null) {
				entity.setStatus(status);
				update(entity);
				return entity;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public User signOut(User user) {
		return updateSignInStatus(user, Integer.valueOf(UserConstant.Status.SIGNOUT.getValue()));
	}

	@Override
	public User getUserByUserId(Long userId) {
		return findOne(userId);
	}
}
