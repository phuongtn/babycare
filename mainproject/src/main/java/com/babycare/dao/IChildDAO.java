package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Child;

public interface IChildDAO extends IOperations<Child>{
	BaseModel addChild(Child child);
	BaseModel updateChild(Child child);
	BaseModel removeChild(Child child);
	BaseModel getChildById(Child child);
}
