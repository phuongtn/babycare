package com.babycare.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;
import com.babycare.model.response.CommonResponse;
import com.babycare.service.impl.PushMessageService;

@RestController(value = "pushMessageController")
@RequestMapping("pushmessage")
public class PushMessageCotroller {
	@Autowired
	@Qualifier("pushMessageService")
	private PushMessageService pushMessageService;


	@DeleteMapping(value = "/delete", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> deleteMessage(@RequestBody PushMessage body) {
		return Response(pushMessageService.deleteMessage(body));

	}

	@PostMapping(value = "/get", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> getMessage(@RequestBody PushMessage body) {
		return Response(pushMessageService.getByMessage(body));
	}
	
	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof PushMessageEntity) {
			return new ResponseEntity<BaseModel>((PushMessageEntity) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof CommonResponse){
			return new ResponseEntity<BaseModel>((CommonResponse) model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((PushMessageEntity) null, HttpStatus.CONFLICT);
		}
	}
}
