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
import com.babycare.model.ResultList;

import com.babycare.model.entity.ContentEntity;

import com.babycare.model.payload.Content;
import com.babycare.service.IContentService;

@RestController(value = "contentController")
@RequestMapping("content")
public class ContentController {
	@Autowired
	@Qualifier("contentService")
	private IContentService contentService;

	@PostMapping(value = "/add", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> addContent(@RequestBody Content body) {
		BaseModel model = contentService.addContent(body);
		return Response(model);
	}

	@PostMapping(value = "/update", headers = "Accept=application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<BaseModel> updateContent(@RequestBody Content body) {
		BaseModel model = contentService.updateContent(body);
		return Response(model);
	}

	private @ResponseBody ResponseEntity<BaseModel> Response(BaseModel model) {
		if (model instanceof ContentEntity) {
			return new ResponseEntity<BaseModel>((Content) model, HttpStatus.OK);
		} else if (model instanceof Error) {
			return new ResponseEntity<BaseModel>(model, HttpStatus.CONFLICT);
		} else if (model instanceof ResultList){
			return new ResponseEntity<BaseModel>(model, HttpStatus.OK);
		} else {
			return new ResponseEntity<BaseModel>((Content) null, HttpStatus.CONFLICT);
		}
	}
}
