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
import com.babycare.model.entity.Session;
import com.babycare.service.ISessionService;

@RestController(value = "sessionController")
@RequestMapping("session")
public class SessionController {
	@Autowired
	@Qualifier("sessionService")
	private ISessionService sessionService;

	@PostMapping(value = "/addorupdate", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> addorupdate(@RequestBody Session body) {
		BaseModel model = sessionService.addOrUpdateSession(body);
		return Response(model);
	}


	@PostMapping(value = "/by/sessionid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> getSessionBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.getSessionBySessionId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/by/hardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> getSessionByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.getSessionByHardwareId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/status/signin/by/sessionid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> loginBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.loginBySessionId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/status/signout/by/sessionid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> logoutBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.logoutBySessionId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/status/signin/by/hardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> loginByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.loginByHardwareId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/status/signout/by/hardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> logoutByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.logoutByHardwareId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/pushid/by/sessionid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> updatePushIdBySessionId(@RequestBody Session body) {
		BaseModel model = sessionService.updatePushIdBySessionId(body);
		return Response(model);
	}
	
	@PostMapping(value = "/update/pushid/by/hardwareid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> updatePushIdByHardwareId(@RequestBody Session body) {
		BaseModel model = sessionService.updatePushIdByHardwareId(body);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof Session) {
			return new ResponseEntity<BaseModel>((Session) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>((Error) model, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<BaseModel>((Session) null, HttpStatus.CONFLICT);
		}
	}
}
