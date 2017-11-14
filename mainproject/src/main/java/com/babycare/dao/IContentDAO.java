package com.babycare.dao;

import java.util.List;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;

public interface IContentDAO extends IPagingOperations<ContentEntity> {
	BaseModel addContent(Content payload);
	BaseModel updateContent(Content payload);
	BaseModel getContentById(Content payload);
	BaseModel getByContentTypeId(Content payload);
}
