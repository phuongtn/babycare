package com.babycare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ResultList;
import com.babycare.model.entity.SessionEntity;
import com.babycare.model.payload.Session;
import com.babycare.service.ISessionService;

@RestController(value = "sessionController")
@RequestMapping("session")
public class SessionController {
	@Autowired
	@Qualifier("sessionService")
	private ISessionService sessionService;

	@PostMapping(value = "/addorupdate", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> addorupdate(@RequestBody Session body) {
		BaseModel model = sessionService.addOrUpdateSession(body);
		return Response(model);
	}

	@PostMapping(value = "/by/sessionid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getSessionBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.getSessionBySessionId(body);
		return Response(model);
	}

	@PostMapping(value = "/by/hardwareid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getSessionByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.getSessionByHardwareId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/status/signin/by/sessionid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> loginBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.loginBySessionId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/status/signout/by/sessionid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> logoutBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.logoutBySessionId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/status/signin/by/hardwareid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> loginByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.loginByHardwareId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/status/signout/by/hardwareid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> logoutByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.logoutByHardwareId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/pushid/by/sessionid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updatePushIdBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.updatePushIdBySessionId(body);
		return Response(model);
	}

	@PutMapping(value = "/update/pushid/by/hardwareid", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updatePushIdByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.updatePushIdByHardwareId(body);
		return Response(model);
	}

	@GetMapping(value = "/get/list/session/{userId}")
	public @ResponseBody ResponseEntity<BaseModel> getSessionListByUserId(
			@PathVariable("userId") Long userId) {
		BaseModel model = sessionService.getSessionListByUserId(userId);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof SessionEntity) {
			return new ResponseEntity<BaseModel>((Session) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>((Error) model, HttpStatus.CONFLICT);
		}else if (model instanceof ResultList){
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((Session) null, HttpStatus.CONFLICT);
		}
	}
}
