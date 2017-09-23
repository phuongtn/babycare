package com.babycare.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.babycare.Utils;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
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
	public BaseModel updatePushID(User user) {
		if (user != null) {
			if (user.getUserId() != null) {
				if (StringUtils.isNotEmpty(user.getPushId())) {
					BaseModel model = getUserByUserId(user.getUserId());
					if (model instanceof User) {
						User entity = (User) model;
						entity.setPushId(user.getPushId());
						return update(entity);
					} else {
						return model;
					}
				} else {
					return new Error(ErrorConstant.ERROR_PUSHID_INVALID, ErrorConstant.ERROR_PUSHID_INVALID_MESSAGE);
				}

			} else {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			}
		} else {
			return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
		}
	}

	private BaseModel updateStatus(User user, Integer status) {
		if (user != null) {
			if (user.getUserId() != null) {
				BaseModel model = getUserByUserId(user.getUserId());
				if (model instanceof User) {
					User entity = (User) model;
					entity.setStatus(status);
					return update(entity);
				} else {
					return model;
				}
			} else {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			}
		} else {
			return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
		}
	}

	@Override
	public BaseModel getUserByUserId(Long userId) {
		BaseModel user = findOne(userId);
		if (user != null) {
			return user;
		} else {
			return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
		}
	}

	private BaseModel getUserByHardwareProviderEmail(User user) {
		if (user != null) {
			String email = user.getEmail();
			String hardwareId = user.getHardwareId();
			String provider = user.getProvider();
			if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(hardwareId) && StringUtils.isNotEmpty(provider)) {
				String hql = "FROM User as usr WHERE usr.hardwareId = ? AND usr.provider = ? AND usr.email = ?";
				User entity = null;
				try {
					entity = (User) em.createQuery(hql).setParameter(0, hardwareId).setParameter(1, provider)
							.setParameter(2, email).getSingleResult();
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
	public BaseModel Login(User user) {
		BaseModel model = getUserByHardwareProviderEmail(user);
		if (model instanceof User) {
			User entity = (User) model;
			entity.setStatus(UserConstant.Status.SIGNIN.getValue());
			User response = update(entity);
			return response;
		} else {
			return model;
		}
	}

	@Override
	public BaseModel LogOut(User user) {
		return updateStatus(user, UserConstant.Status.SIGNOUT.getValue());
	}


	@Override
	public BaseModel register(User user) {
		if (user != null && !Utils.isValidEmailAddress(user.getEmail())) {
			return new Error(ErrorConstant.ERROR_EMAIL_INVALID, ErrorConstant.ERROR_EMAIL_INVALID_MESSAGE); 
		} else {
			BaseModel model = getUserByHardwareProviderEmail(user);
			if (model instanceof User) {
				return new Error(ErrorConstant.ERROR_USER_EXIST, ErrorConstant.ERROR_USER_EXIST_MESSAGE);
			} else {
				return createEntity(user);
			}
		}
	}

}
