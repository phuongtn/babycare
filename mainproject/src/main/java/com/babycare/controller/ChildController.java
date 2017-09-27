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
import com.babycare.model.ResultList;
import com.babycare.model.entity.Child;
import com.babycare.model.entity.User;
import com.babycare.service.IChildService;

@RestController(value = "childController")
@RequestMapping("child")
public class ChildController {
	@Autowired
	@Qualifier("childService")
	private IChildService childService;

	@PostMapping(value = "/add", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> addChild(@RequestBody Child body) {
		BaseModel model = childService.addChild(body);
		return Response(model);
	}

	@PutMapping(value = "/update", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> updateChild(@RequestBody Child body) {
		BaseModel model = childService.updateChild(body);
		return Response(model);
	}

	@PostMapping(value = "/get/by/id", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> getChildById(@RequestBody Child body) {
		BaseModel model = childService.getChildById(body);
		return Response(model);
	}

	@PostMapping(value = "/fetch/by/userid", headers = "Accept=application/json", produces = "applicaiton/json")
	public @ResponseBody ResponseEntity<BaseModel> fetchChildrenByUserId(@RequestBody Child body) {
		BaseModel model = childService.fetchChildrenByUserId(body);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof Child) {
			return new ResponseEntity<BaseModel>((Child) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof ResultList){
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((Child) null, HttpStatus.CONFLICT);
		}
	}
}
