package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IChildDAO;
import com.babycare.dao.IUserDao;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.entity.Child;
import com.babycare.model.entity.User;

@Repository
@Transactional
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
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			Long userId = child.getUserId();
			if (userId == null) {
				return new Error(ErrorConstant.ERROR_USER_NOT_EXIST, ErrorConstant.ERROR_USER_NOT_EXIST_MESSAGE);
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
						return new Error(ErrorConstant.ERROR_ADD_CHILD, ErrorConstant.ERROR_ADD_CHILD_MESSAGE, exception);
					} else {
						return new Error(ErrorConstant.ERROR_ADD_CHILD, ErrorConstant.ERROR_ADD_CHILD_MESSAGE);
					}
				}
			}
		}
	}

	@Override
	public BaseModel updateChild(Child child) {
		if (child == null || child.getChildId() == null || child.getUserId() == null || 
				child.getBabyType() == null || StringUtils.isBlank(child.getName())) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE); 
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
					return new Error(ErrorConstant.ERROR_UPDATE_CHILD, ErrorConstant.ERROR_UPDATE_CHILD_MESSAGE, exception);
				} else {
					return new Error(ErrorConstant.ERROR_UPDATE_CHILD, ErrorConstant.ERROR_UPDATE_CHILD_MESSAGE);
				}
			}
		}
	}


	@Override
	public BaseModel removeChildById(Child child) {
		if (child == null || child.getChildId() == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			BaseModel model  = getChildById(child);
			if (model instanceof Child) {
				try {
					deleteById(child.getChildId());
				} catch(Exception e) {
					return new Error(ErrorConstant.ERROR_REMOVE_CHILD, ErrorConstant.ERROR_REMOVE_CHILD_MESSAGE);
				}
				return (Child) model;
			} else {
				return model;
			}
		}
	}
	

	@Override
	public BaseModel getChildById(Child child) {
		if (child == null || child.getChildId() == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE);
		} else {
			Long childId = child.getChildId();
			Child childEntity = null;
			String exception = null;
			try {
				childEntity = findOne(childId);
			}catch (Exception e) {
				childEntity = null;
				exception = e.getMessage();
			}
			if (childEntity != null) {
				return childEntity;
			} else {
				if (StringUtils.isNotEmpty(exception)) {
					return new Error(ErrorConstant.ERROR_CHILD_NOT_EXIST, ErrorConstant.ERROR_CHILD_NOT_EXIST_MESSAGE);
				} else {
					return new Error(ErrorConstant.ERROR_CHILD_NOT_EXIST, ErrorConstant.ERROR_CHILD_NOT_EXIST_MESSAGE, exception);
				}
			}
		}
	}

	@Override
	public BaseModel fetchChildrenByUserId(Child child) {
		if (child == null || child.getUserId() == null) {
			return new Error(ErrorConstant.ERROR_INPUT_ERROR, ErrorConstant.ERROR_INPUT_ERROR_MESSAGE); 
		} else {
			Long userId = child.getUserId();
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
				return new Error(ErrorConstant.ERROR_FETCH_CHILD, ErrorConstant.ERROR_FETCH_CHILD_MESSAGE);
			}
		}
	}

}
