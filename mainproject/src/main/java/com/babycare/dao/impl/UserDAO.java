package com.babycare.dao.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.babycare.Utils;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.ISessionDAO;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.entity.User;

@Repository
@Transactional
@Qualifier("userDAO")
public class UserDAO extends AbstractJpaDao<User> implements IUserDao {
	public UserDAO() {
		super();
		setClazz(User.class);
	}

	private BaseModel getUserByProviderEmail(User user) {
		if (user != null) {
			String email = user.getEmail();
			String provider = user.getProvider();
			if (StringUtils.isNotEmpty(email) || StringUtils.isNotEmpty(provider)) {
				String hql = "FROM User as usr WHERE usr.provider = ? AND usr.email = ?";
				User entity = null;
				try {
					entity = (User) em.createQuery(hql).setParameter(0, provider).setParameter(1, email).getSingleResult();
				} catch (Exception e) {
					entity = null;
				}
				if (entity != null) {
					return entity;
				} else {
					return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
				}
			} else {
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			}
		} else {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		}
	}

	@Override
	public BaseModel register(User user) {
		if (user != null || !Utils.isValidEmailAddress(user.getEmail()) ||
				StringUtils.isEmpty(user.getProvider()) || StringUtils.isEmpty(user.getName())) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			//return new Error(ErrorConstant.ERROR_EMAIL_INVALID, ErrorConstant.ERROR_EMAIL_INVALID_MESSAGE); 
		} else {
			BaseModel model = getUserByProviderEmail(user);
			if (model instanceof User) {
				return new Error(ErrorConstant.ERROR_USER_EXIST, ErrorConstant.ERROR_USER_EXIST_MESSAGE);
			} else {
				return createUser(user);
			}
		}
	}

	private BaseModel createUser(User user) {
		User userCreated = null;
		String exception = null;
		try {
			userCreated = updateEntity(user);
		} catch (Exception e) {
			userCreated = null;
			exception = e.getMessage();
		}
		if (userCreated != null) {
			return userCreated;
		} else {
			if (StringUtils.isNotEmpty(exception)) {
				return new Error(ErrorConstant.ERROR_CREATE_USER, ErrorConstant.ERROR_CREATE_USER_MESSAGE, exception);
			} else {
				return new Error(ErrorConstant.ERROR_CREATE_USER, ErrorConstant.ERROR_CREATE_USER_MESSAGE);
			}
		}
	}
}
