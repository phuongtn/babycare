package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.User;
import com.babycare.model.payload.UserPayload;

public interface IUserDao extends IOperations<User> {
	BaseModel register(User user);
	BaseModel updateByEmailAndProvider(User user);
	BaseModel getUserByUserId(UserPayload payload);
	BaseModel getUserByEmailAndProvider(UserPayload payload);
	BaseModel updateUserByUserId(User user);
}
