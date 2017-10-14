package com.babycare.dao.impl;


import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.babycare.Utils;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;

import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;

@Repository
@Qualifier("userDAO")
public class UserDAO extends AbstractJpaDao<UserEntity> implements IUserDao {
	public UserDAO() {
		super();
		setClazz(UserEntity.class);
	}

	@Override
	public BaseModel getUserByEmailAndProvider(User user) {
		if (user != null) {
			String email = user.getEmail();
			String provider = user.getProvider();
			return getUserByEmailAndProvider(email, provider);
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	private BaseModel getUserByEmailAndProvider(String email, String provider) {
		if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(provider)) {
			String hql = "FROM UserEntity as usr WHERE usr.provider = ? AND usr.email = ?";
			UserEntity entity = null;
			try {
				entity = (UserEntity) em.createQuery(hql).setParameter(0, provider).setParameter(1, email).getSingleResult();
			} catch (Exception e) {
				entity = null;
			}
			if (entity != null) {
				return entity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}

	@Override
	public BaseModel register(User user) {
		if (!validateUser(false, user)) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model = getUserByEmailAndProvider(user.getEmail(), user.getProvider());
			if (model instanceof UserEntity) {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_EXIST);
			} else {
				UserEntity entity = new UserEntity(user);
				return createUser(entity);
			}
		}
	}

	private BaseModel createUser(User user) {
		UserEntity userCreated = null;
		String exception = null;
		try {
			userCreated = createEntity(new UserEntity(user));
		} catch (Exception e) {
			userCreated = null;
			exception = e.getMessage();
		}
		if (userCreated != null) {
			return userCreated;
		} else {
			if (StringUtils.isNotEmpty(exception)) {
				return ErrorConstant.getError(ErrorConstant.ERROR_CREATE_USER, exception);
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_CREATE_USER);
			}
		}
	}

	@Override
	public BaseModel updateByEmailAndProvider(User user) {
		if (!validateUser(false, user)) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model = getUserByEmailAndProvider(user.getEmail(), user.getProvider());
			if (model instanceof Error) {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			} else {
				UserEntity userEntityUpdated = null;
				String exception = null;
				try {
					UserEntity updatedModel = (UserEntity) model;
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
						return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_USER, exception);
					} else {
						return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_USER);
					}
				}
			}
		}
	}

	@Override
	public BaseModel getUserByUserId(User user) {
		if (user == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			return getUserByUserId(user.getUserId());
		}
	}

	private BaseModel getUserByUserId(Long id) {
		if (id == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			UserEntity enntity = findOne(id);
			if (enntity == null) {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			} else {
				return enntity;
			}
		}
	}

	@Override
	public BaseModel updateUserByUserId(@Nonnull User user) {
		UserEntity userEntityUpdated = null;
		String exception = null;
		if (!validateUser(true, user)) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model = getUserByUserId(user.getUserId());
			if (model instanceof UserEntity) {
				try {
					UserEntity updatedModel = (UserEntity) model;
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
						return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_USER);
					} else {
						return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_USER, exception);
					}
				}
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			}
		}
	}
	
	private boolean validateUser(boolean isCheckId, User user) {
		if (user == null) {
			return false;
		} else {
			if (isCheckId && user.getUserId() == null) {
				return false;
			}
			if (!Utils.isValidEmailAddress(user.getEmail())) {
				return false;
			}
			/*if (StringUtils.isEmpty(user.getName())) {
				return false;
			}*/
			if (StringUtils.isEmpty(user.getProvider())) {
				return false;
			}
		}
		return true;
	}
	
}
