package com.babycare.dao.impl;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.Utils;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IChildDAOPagingRepo;
import com.babycare.dao.IUserDAOPagingRepo;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.common.CommonResponseEx;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;
import com.babycare.service.ISessionService;

@Repository
@Component
@Qualifier("userDAO")
public class UserDAO extends AbstractJpaDao<UserEntity> implements IUserDao {
	public static final Logger logger = Logger.getLogger(UserDAO.class.getName());
	@Autowired
	private IUserDAOPagingRepo repo;
	@Autowired
	@Qualifier("sessionService")
	private ISessionService sessionService;

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
			final String hql = "FROM UserEntity as usr WHERE usr.provider=:provider AND usr.email=:email";
			UserEntity entity = null;
			try {
				entity = (UserEntity) em.createQuery(hql).setParameter("provider", provider).setParameter("email", email)
						.getSingleResult();
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
			final String hql = "FROM UserEntity as usr WHERE usr.userId = :userId ";
			UserEntity entity = null;
			try {
				entity = (UserEntity) em.createQuery(hql).setParameter("userId", id).getSingleResult();
			} catch (Exception e) {
				entity = null;
			}
			if (entity != null) {
				return entity;
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
			/*
			 * if (StringUtils.isEmpty(user.getName())) { return false; }
			 */
			if (StringUtils.isEmpty(user.getProvider())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public BaseModel updateUserByUserId(@Nonnull User user) {
		if (!validateUser(true, user)) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model = getUserByUserId(user.getUserId());
			// model.setSessionId(user.getSessionId());
			return updateUser(model, user);
		}
	}

	@Override
	public BaseModel updateByEmailAndProvider(User user) {
		if (!validateUser(false, user)) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model = getUserByEmailAndProvider(user.getEmail(), user.getProvider());
			// model.setSessionId(user.getSessionId());
			return updateUser(model, user);
		}
	}

	private BaseModel updateUser(BaseModel model, User user) {
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
				// Send Notification
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

	@Override
	public BaseModel deleteUser(User user) {
		if (user == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			return deleteUserById(user.getUserId());
		}
	}

	private BaseModel deleteUserById(Long id) {
		if (id == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			try {
				UserEntity entity = findOne(id);
				if (entity != null) {
					deleteById(id);
					return new CommonResponseEx("User removed", true);
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
				}
				
			} catch (Exception e) {
				return ErrorConstant.getError(ErrorConstant.ERROR_REMOVE_USER);
			}
		}
	}

	@Override
	public Page<UserEntity> findPaginated(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Page<UserEntity> findPaginated(Integer page, Integer size) {
		return repo.findAll(PageRequest.of(page, size));
	}

	@Override
	public Page<UserEntity> findExamplePaginated(Example<UserEntity> example, Pageable pageable) {
		return repo.findAll(example, pageable);
	}

	@Override
	public Page<UserEntity> findExamplePaginated(Example<UserEntity> example, Integer page, Integer size) {
		return repo.findAll(example, PageRequest.of(page, size));
	}

}
