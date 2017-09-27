package com.babycare.dao.impl;


import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.babycare.Utils;
import com.babycare.dao.AbstractJpaDao;
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

	@Override
	public BaseModel getUserByEmailAndProvider(User user) {
		if (user != null) {
			String email = user.getEmail();
			String provider = user.getProvider();
			if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(provider)) {
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
		if (user == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			if (validateUser(false, user)) {
				return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
			} else {
				BaseModel model = getUserByEmailAndProvider(user);
				if (model instanceof User) {
					return new Error(ErrorConstant.ERROR_USER_EXIST, ErrorConstant.ERROR_USER_EXIST_MESSAGE);
				} else {
					return createUser(user);
				}
			}
		}
		
	}

	private BaseModel createUser(User user) {
		User userCreated = null;
		String exception = null;
		try {
			userCreated = createEntity(user);
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

	@Override
	public BaseModel updateByEmailAndProvider(User user) {
		if (!validateUser(false, user)) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			BaseModel model = getUserByEmailAndProvider(user);
			if (model instanceof Error) {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			} else {
				User userEntityUpdated = null;
				String exception = null;
				try {
					User updatedModel = (User) model;
					updatedModel.setDob(user.getDob());
					updatedModel.setName(user.getName());
					userEntityUpdated = updateEntity(updatedModel);
				} catch (Exception e) {
					userEntityUpdated = null;
					exception = e.getMessage();
				}
				if (userEntityUpdated != null) {
					return userEntityUpdated;
				} else {
					if (StringUtils.isNotEmpty(exception)) {
						return new Error(ErrorConstant.ERROR_UPDATE_USER, ErrorConstant.ERROR_UPDATE_USER_MESSAGE, exception);
					} else {
						return new Error(ErrorConstant.ERROR_UPDATE_USER, ErrorConstant.ERROR_UPDATE_USER_MESSAGE);
					}
				}
			}
		}
	}

	@Override
	public BaseModel getUserByUserId(User user) {
		if (user == null || user.getUserId() == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			User enntity = findOne(user.getUserId());
			if (enntity == null) {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			} else {
				return enntity;
			}
		}
	}

	@Override
	public BaseModel updateUserByUserId(@Nonnull User user) {
		User userEntityUpdated = null;
		String exception = null;
		if (!validateUser(true, user)) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			BaseModel model = getUserByUserId(user);
			if (model instanceof User) {
				try {
					userEntityUpdated = updateEntity(user);
				} catch (Exception e) {
					userEntityUpdated = null;
					exception = e.getMessage();
				}
				if (userEntityUpdated != null) {
					return userEntityUpdated;
				} else {
					if (StringUtils.isNotEmpty(exception)) {
						return new Error(ErrorConstant.ERROR_UPDATE_USER, ErrorConstant.ERROR_UPDATE_USER_MESSAGE, exception);
					} else {
						return new Error(ErrorConstant.ERROR_UPDATE_USER, ErrorConstant.ERROR_UPDATE_USER_MESSAGE);
					}
				}
			} else {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
			}
		}
	}
	
	private boolean validateUser(boolean isCheckId, User user) {
		if (user == null) {
			return false;
		} else {
			if (isCheckId ) {
				return (user.getUserId() != null);
			}
			return (Utils.isValidEmailAddress(user.getEmail()) && StringUtils.isNotEmpty(user.getName()) && StringUtils.isNotEmpty(user.getProvider()));
		}
	}
}
