package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.User;

public interface IUserDao extends IOperations<User> {
	BaseModel register(User user);
	BaseModel updateByEmailAndProvider(User user);
	BaseModel getUserByUserId(User user);
	BaseModel getUserByEmailAndProvider(User user);
	BaseModel updateUser(User user);
}
