package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Child;
import com.babycare.model.payload.ChildPayload;

public interface IChildDAO extends IOperations<Child>{
	BaseModel addChild(Child child);
	BaseModel updateChild(Child child);
	BaseModel removeChildById(ChildPayload payload);
	BaseModel getChildById(ChildPayload child);
	BaseModel fetchChildrenByUserId(ChildPayload child);
}
