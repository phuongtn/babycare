package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IChildDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.common.ChildExt;
import com.babycare.model.common.ResultListEx;
import com.babycare.model.entity.ChildEntity;
import com.babycare.model.payload.Child;

@Repository
@Component
@Qualifier("childDAO")
public class ChildDAO extends AbstractJpaDao<ChildEntity> implements IChildDAO {
	public ChildDAO() {
		super();
		setClazz(ChildEntity.class);
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
				if (child.getChildId() != null) {
					child.setChildId((Long) null);
				}
				ChildEntity childCreated = null;
				String exception = null;
				try {
					childCreated = createEntity(new ChildEntity(child));
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
			ChildEntity childCreated = null;
			String exception = null;
			try {
				childCreated = updateEntity(new ChildEntity(child));
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
	public BaseModel removeChildById(Child payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			BaseModel model  = getChildById(payload);
			if (model instanceof ChildEntity) {
				try {
					deleteById(payload.getChildId());
				} catch(Exception e) {
					return ErrorConstant.getError(ErrorConstant.ERROR_REMOVE_CHILD);
				}
				return (ChildEntity) model;
			} else {
				return model;
			}
		}
	}
	

	@Override
	public BaseModel getChildById(Child payload) {
		if (payload == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Long childId = payload.getChildId();
			return getChildById(childId);
		}
	}

	public BaseModel getChildById(Long id) {
		if (id == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			ChildEntity childEntity = null;
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
	public BaseModel fetchChildrenByUserId(Child payload) {
		if (payload == null || payload.getUserId() == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			Long userId = payload.getUserId();
			String hql = "FROM ChildEntity as child WHERE child.userId = ?";
			try {
				List<ChildEntity> result = (List<ChildEntity>)em.createQuery(hql).setParameter(0, userId).getResultList();
				if (result != null && !result.isEmpty()) {
					List<ChildExt> childList = new ArrayList<ChildExt>();
					for (ChildEntity entity : result) {
						childList.add(new ChildExt().toChild(entity));
					}
					return new ResultListEx<ChildExt>(childList);
				} else {
					return new ResultListEx<BaseModel>(new ArrayList<>());
				}
			} catch (Exception e) {
				return ErrorConstant.getError(ErrorConstant.ERROR_FETCH_CHILD);
			}
		}
	}

	
}
