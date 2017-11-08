package com.babycare.dao;

import com.babycare.model.BaseModel;

import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;

public interface IUserDao extends IOperations<UserEntity> {
	BaseModel register(User user);
	BaseModel updateByEmailAndProvider(User user);
	BaseModel getUserByUserId(User user);
	BaseModel getUserByEmailAndProvider(User user);
	BaseModel updateUserByUserId(User user);
	//BaseModel deleteUser(User user);
}
