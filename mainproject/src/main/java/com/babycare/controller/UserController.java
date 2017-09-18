package com.babycare.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.User;
import com.babycare.service.IUserService;

@RestController(value = "userController")
@RequestMapping("user")
public class UserController {
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@PostMapping(value = "/register", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> registerUser(@RequestBody User body) {
		User user = userService.createEntity(body);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/update/pushid/byhardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> updatePushIdByHardwareIdAndProvider(@RequestBody User body) {
		if (body != null) {
			String hardwareId = body.getHardwareId();
			String provider = body.getProvider();
			String pushId = body.getPushId();
			User user = userService.updatePushIdByHardwareIdAndProvider(hardwareId, provider, pushId);
			if (user != null) {
				return new ResponseEntity<User>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/update/pushid/byuserid/", headers = "Accept=application/json", produces = "applicaiton/json")
	public  @ResponseBody ResponseEntity<User> updatePushIdByUserId(@RequestBody User body) {
		if (body != null) {
			Long userId = body.getUserId();
			String pushId = body.getPushId();
			User entity;
			if (userId != null && StringUtils.isNotEmpty(pushId)) {
				entity = userService.updatePushIdByUserId(userId, pushId);
				if (entity != null) {
					return new ResponseEntity<User>(entity, HttpStatus.OK);
				} else {
					return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
				}
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(value = "/update/signin/status/byuserid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> updateSignInStatusByUserId(@RequestBody User body) {
		if (body != null) {
			Long userId = body.getUserId();
			Integer status = body.getStatus();
			User entity = null;
			if (userId != null && status != null) {
				entity = userService.updateSignInStatusByUserId(userId, status);
				if (entity != null) {
					return new ResponseEntity<User>(entity, HttpStatus.OK);
				} else {
					return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
				}
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(value = "/update/signin/status/byhardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> getUserByHardwareIdAndProvider(@RequestBody User body) {
		if (body != null) {
			Long userId = body.getUserId();
			String provider = body.getProvider();
			String hardwareId = body.getHardwareId();
			if (userId != null && StringUtils.isNotEmpty(provider)) {
				User entity = userService.getUserByHardwareIdAndProvider(hardwareId, provider);
				if (entity != null) {
					return new ResponseEntity<User>(entity, HttpStatus.OK);
				} else {
					return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
				}
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
			
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(value = "/signin", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> signin (@RequestBody User body) {
		if (body != null) {
			User entity = userService.signIn(body);
			if (entity != null) {
				return new ResponseEntity<User>(entity, HttpStatus.OK);
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/signout", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<User> signout (@RequestBody User body) {
		if (body != null) {
			User entity = userService.signOut(body);
			if (entity != null) {
				return new ResponseEntity<User>(entity, HttpStatus.OK);
			} else {
				return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
		}
	}
}
