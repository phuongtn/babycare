package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.Session;
import com.babycare.model.entity.User;

public interface IUserDao extends IOperations<User> {
	BaseModel register(User user);
	//BaseModel registerAndAddSession();
}
