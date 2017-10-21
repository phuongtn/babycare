package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.babycare.dao.IContentDAO;
import com.babycare.dao.IOperations;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IContentService;

@Service(value = "contentService")
public class ContentService extends AbstractJpaService<ContentEntity> implements IContentService {
	@Autowired
	@Qualifier("contentDAO")
	private IContentDAO contentDAO;

	@Override
	public BaseModel addContent(Content payload) {
		return contentDAO.addContent(payload);
	}

	@Override
	public BaseModel updateContent(Content payload) {
		return contentDAO.updateContent(payload);
	}

	@Override
	public BaseModel getContentById(Content payload) {
		return contentDAO.getContentById(payload);
	}

	@Override
	public ContentEntity createEntity(ContentEntity entity) {
		return contentDAO.createEntity(entity);
	}

	@Override
	protected IOperations<ContentEntity> getDao() {
		return contentDAO;
	}
}
