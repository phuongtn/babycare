package com.babycare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;
import com.babycare.service.ISessionService;
import com.babycare.service.IUserService;

@RestController(value = "userController")
@RequestMapping("user")
public class UserController {
	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("sessionService")
	private ISessionService sessionService;

	@PostMapping(value = "/register", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> register(@RequestBody User body) {
		BaseModel model = userService.register(body);
		return Response(model);
	}

	@PostMapping(value = "/get/by/id", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getUserByUserId(@RequestBody User payload) {
		BaseModel model = userService.getUserByUserId(payload);
		return Response(model);
	}

	@PostMapping(value = "/get/by/email/provider", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getUserByEmailAndProvider(@RequestBody User payload) {
		BaseModel model = userService.getUserByEmailAndProvider(payload);
		return Response(model);
	}

	@PutMapping(value = "/update/by/email/provider", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updateByEmailAndProvider(@RequestBody User body) {
		BaseModel model = userService.updateByEmailAndProvider(body);
		return Response(model);
	}

	@PutMapping(value = "/update/by/id", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updateUserByUserId(@RequestBody User body) {
		BaseModel model = userService.updateUserByUserId(body);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof UserEntity) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>((Error) model, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<BaseModel>((User) null, HttpStatus.CONFLICT);
		}
	}

}
