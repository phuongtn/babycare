package com.babycare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.Utils;
import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ErrorConstant;
import com.babycare.model.User;
import com.babycare.service.IUserService;

@RestController(value = "userController")
@RequestMapping("user")
public class UserController {
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@PostMapping(value = "/register", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> register(@RequestBody User body) {
		BaseModel model = userService.register(body);
		if (model instanceof User) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else if (model instanceof Error){
			return new ResponseEntity<BaseModel>((Error) model, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<BaseModel>((User) null, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/update/pushid/", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> updatePushId(@RequestBody User body) {
		BaseModel model = userService.updatePushID(body);
		if (model instanceof User) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/login/", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> Login(@RequestBody User body) {
		BaseModel model = userService.Login(body);
		if (model instanceof User) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		}
	}

	@PostMapping(value = "/logout/", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> Logout(@RequestBody User body) {
		BaseModel model = userService.LogOut(body);
		if (model instanceof User) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		}
	}
}
