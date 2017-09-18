package com.babycare.dao;

import com.babycare.model.User;

public interface IUserDao extends IOperations<User> {
	User updatePushIdByHardwareIdAndProvider(String hardwareId, String provider, String pushId);
	User updatePushIdByUserId(Long userId, String pushId);
	
	User updateSignInStatusByUserId(Long userId, Integer status);
	
	User updateSignInStatusByHardwareIdAndProvider(String hardwareId, String provider, Integer status);
	User getUserByHardwareIdAndProvider(String hardwareId, String provider);
	User getUserByUserId(Long userId);

	User signIn(User user);
	User signOut(User user);
}
