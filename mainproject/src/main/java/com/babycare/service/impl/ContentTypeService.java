package com.babycare.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.babycare.dao.IContentTypeDAO;
import com.babycare.dao.IOperations;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentTypeEntity;

import com.babycare.service.AbstractJpaService;
import com.babycare.service.IContentTypeService;
@Service(value = "contentTypeService")
public class ContentTypeService extends AbstractJpaService<ContentTypeEntity> implements IContentTypeService {
	@Autowired
	@Qualifier("contentTypeDAO")
	private IContentTypeDAO contentTypeDAO;

	public BaseModel fetchContentTypes() {
		return contentTypeDAO.fetchContentTypes();
	}

	@Override
	protected IOperations<ContentTypeEntity> getDao() {
		return contentTypeDAO;
	}

	@Override
	public ContentTypeEntity createEntity(ContentTypeEntity entity) {
		return contentTypeDAO.createEntity(entity);
	}
}
