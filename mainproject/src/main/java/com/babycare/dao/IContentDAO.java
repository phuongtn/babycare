package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;

public interface IContentDAO extends IPagingOperations<ContentEntity> {
	BaseModel addContent(Content payload);
	BaseModel updateContent(Content payload);
	BaseModel getContentById(Content payload);
	BaseModel getByContentTypeId(Content payload);
	BaseModel getContentByWeekAndContentTypeId(Integer weekNumber, Long contentTypeId);
	BaseModel getContentByWeek(Integer weekNumber);
	BaseModel getContentByContentTypeIdChildId(Long contentTypeId, Long ChildId);
	BaseModel getContentByChildId(Long ChildId);
}
