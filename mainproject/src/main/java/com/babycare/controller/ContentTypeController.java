package com.babycare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.babycare.model.BaseModel;
import com.babycare.model.Error;
import com.babycare.model.ResultList;

import com.babycare.model.entity.ContentTypeEntity;

import com.babycare.service.IContentTypeService;

@RestController(value = "contentTypeController")
@RequestMapping("contenttype")
public class ContentTypeController {
	@Autowired
	@Qualifier("contentTypeService")
	private IContentTypeService contentTypeService;

	@GetMapping(value = "/get/all", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> fetchContentTypes() {
		BaseModel model = contentTypeService.fetchContentTypes();
		return Response(model);
	}
	
	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof ContentTypeEntity) {
			return new ResponseEntity<BaseModel>((ContentTypeEntity) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof ResultList){
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((ContentTypeEntity) null, HttpStatus.CONFLICT);
		}
	}
}
