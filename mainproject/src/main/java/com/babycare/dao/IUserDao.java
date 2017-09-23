package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.User;

public interface IUserDao extends IOperations<User> {
	BaseModel updatePushID(User user);
	BaseModel getUserByUserId(Long userId);
	BaseModel Login(User user);
	BaseModel LogOut(User user);
	BaseModel register(User user);
}
