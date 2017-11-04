package com.babycare.dao;

import com.babycare.model.BaseModel;

import com.babycare.model.entity.ChildEntity;
import com.babycare.model.payload.Child;


public interface IChildDAO extends IOperations<ChildEntity>{
	BaseModel addChild(Child child);
	BaseModel updateChild(Child child);
	BaseModel removeChildById(Child payload);
	BaseModel getChildById(Child child);
	BaseModel fetchChildrenByUserId(Child child);
}
