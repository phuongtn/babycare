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

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.entity.User;
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
		return Response(model);
	}

	@PostMapping(value = "get/userid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> getUserByUserId(@RequestBody User body) {
		BaseModel model = userService.getUserByUserId(body);
		return Response(model);
	}

	@PostMapping(value = "/get/email/provider", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> getUserByEmailAndProvider(@RequestBody User body) {
		BaseModel model = userService.getUserByEmailAndProvider(body);
		return Response(model);
	}

	@PostMapping(value = "/update/by/email/provider", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> updateByEmailAndProvider(@RequestBody User body) {
		BaseModel model = userService.updateByEmailAndProvider(body);
		return Response(model);
	}
	
	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof User) {
			return new ResponseEntity<BaseModel>((User) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>((Error) model, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<BaseModel>((User) null, HttpStatus.CONFLICT);
		}
	}

}
