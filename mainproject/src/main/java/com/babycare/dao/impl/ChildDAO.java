package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IChildDAO;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.entity.Child;
import com.babycare.model.entity.User;
import com.babycare.model.payload.ChildPayload;

@Repository
@Qualifier("childDAO")
public class ChildDAO extends AbstractJpaDao<Child> implements IChildDAO {
	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;
	
	public ChildDAO() {
		super();
		setClazz(Child.class);
	}

	@Override
	public BaseModel addChild(Child child) {
		if (child == null || child.getBabyType() == null || StringUtils.isBlank(child.getName())) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Long userId = child.getUserId();
			if (userId == null) {
				return ErrorConstant.getError(ErrorConstant.ERROR_USER_NOT_EXIST);
			} else {
				Child childCreated = null;
				String exception = null;
				try {
					childCreated = createEntity(child);
				}catch (Exception e) {
					childCreated = null;
					exception = e.getMessage();
				}
				if (childCreated != null) {
					return childCreated;
				} else {
					if (StringUtils.isNotEmpty(exception)) {
						return ErrorConstant.getError(ErrorConstant.ERROR_ADD_CHILD, exception);
					} else {
						return ErrorConstant.getError(ErrorConstant.ERROR_ADD_CHILD);
					}
				}
			}
		}
	}

	@Override
	public BaseModel updateChild(Child child) {
		if (child == null || child.getChildId() == null || child.getUserId() == null || 
				child.getBabyType() == null || StringUtils.isBlank(child.getName())) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Child childCreated = null;
			String exception = null;
			try {
				childCreated = updateEntity(child);
			}catch (Exception e) {
				childCreated = null;
				exception = e.getMessage();
			}
			if (childCreated != null) {
				return childCreated;
			} else {
				if (StringUtils.isNotEmpty(exception)) {
					return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_CHILD, exception);
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_UPDATE_CHILD);
				}
			}
		}
	}


	@Override
	public BaseModel removeChildById(ChildPayload payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model  = getChildById(payload);
			if (model instanceof Child) {
				try {
					deleteById(payload.getChildId());
				} catch(Exception e) {
					return ErrorConstant.getError(ErrorConstant.ERROR_REMOVE_CHILD);
				}
				return (Child) model;
			} else {
				return model;
			}
		}
	}
	

	@Override
	public BaseModel getChildById(ChildPayload payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Long childId = payload.getChildId();
			return getChildById(childId);
		}
	}

	public BaseModel getChildById(Long id) {
		if (id != null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Child childEntity = null;
			String exception = null;
			try {
				childEntity = findOne(id);
			}catch (Exception e) {
				childEntity = null;
				exception = e.getMessage();
			}
			if (childEntity != null) {
				return childEntity;
			} else {
				if (StringUtils.isNotEmpty(exception)) {
					return ErrorConstant.getError(ErrorConstant.ERROR_CHILD_NOT_EXIST, exception);
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_CHILD_NOT_EXIST);
				}
			}
		}
	}
	
	@Override
	public BaseModel fetchChildrenByUserId(ChildPayload payload) {
		if (payload == null || payload.getUserId() == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Long userId = payload.getUserId();
			String hql = "FROM Child as child WHERE child.userId = ?";
			try {
				List<BaseModel> result = (List<BaseModel>)em.createQuery(hql).setParameter(0, userId).getResultList();
				if (result != null || !result.isEmpty()) {
					ResultList<BaseModel> resultList = new ResultList<BaseModel>(result);
					return resultList;
				} else {
					return new ResultList<BaseModel>(new ArrayList<>());
				}
			} catch (Exception e) {
				return ErrorConstant.getError(ErrorConstant.ERROR_FETCH_CHILD);
			}
		}
	}

}
