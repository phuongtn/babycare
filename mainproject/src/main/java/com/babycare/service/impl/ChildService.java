package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.babycare.dao.IChildDAO;
import com.babycare.dao.IOperations;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.ChildEntity;
import com.babycare.model.payload.Child;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IChildService;

@Service(value = "childService")
public class ChildService extends AbstractJpaService<ChildEntity> implements IChildService {
	@Autowired
	@Qualifier("childDAO")
	private IChildDAO childDAO;

	@Override
	public BaseModel addChild(Child child) {
		return childDAO.addChild(child);
	}

	@Override
	public BaseModel updateChild(Child child) {
		return childDAO.updateChild(child);
	}

	@Override
	public BaseModel removeChildById(Child payload) {
		return childDAO.removeChildById(payload);
	}

	@Override
	public ChildEntity createEntity(ChildEntity entity) {
		return childDAO.createEntity(entity);
	}

	@Override
	protected IOperations<ChildEntity> getDao() {
		return childDAO;
	}

	@Override
	public BaseModel getChildById(Child payload) {
		return childDAO.getChildById(payload);
	}

	@Override
	public BaseModel fetchChildrenByUserId(Child payload) {
		return childDAO.fetchChildrenByUserId(payload);
	}
}
