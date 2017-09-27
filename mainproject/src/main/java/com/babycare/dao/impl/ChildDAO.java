package com.babycare.dao.impl;

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
		if (child == null || !validChild(child)) {
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
		if (child == null || child.getChildId() == null || child.getUserId() == null) {
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
	public BaseModel removeChild(Child child) {
		return null;
	}
	
	private boolean validChild(Child child) {
		return (child.getBabyType() != null && StringUtils.isNotBlank(child.getName())); 
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

}
