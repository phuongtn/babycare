package com.babycare.dao;

import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentTypeEntity;

public interface IContentTypeDAO extends IOperations<ContentTypeEntity>{
	BaseModel fetchContentTypes();
}
