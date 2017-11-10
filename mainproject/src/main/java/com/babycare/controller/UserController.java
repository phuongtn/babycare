package com.babycare.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.common.CommonResponseEx;
import com.babycare.model.entity.UserEntity;
import com.babycare.model.payload.User;
import com.babycare.model.response.CommonResponse;
import com.babycare.service.ISessionService;
import com.babycare.service.IUserService;

@RestController(value = "userController")
@RequestMapping("user")
public class UserController {
	private final Logger LOG = LoggerFactory.getLogger(UserController.class);
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

	@DeleteMapping(value = "/delete", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> deleteUser(@RequestBody User body, @RequestHeader(value="SECRET_KEY") String secKey) {
		if (StringUtils.isNotEmpty(secKey) && secKey.equals("ZXCASD~!@#")) {
			BaseModel model = userService.deleteUser(body);
			return Response(model);
		} else {
			return new ResponseEntity<BaseModel>(new CommonResponseEx().setIsSuccess(false).setMesage("Cannot delete Account"), HttpStatus.CONFLICT);
		}

	}

	
	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof UserEntity) {
			//return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof CommonResponse) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((User) null, HttpStatus.CONFLICT);
		}
	}

}
