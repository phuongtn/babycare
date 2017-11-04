package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;

public interface IContentDAO extends IOperations<ContentEntity>{
	BaseModel addContent(Content payload);
	BaseModel updateContent(Content payload);
	BaseModel getContentById(Content payload);
}
