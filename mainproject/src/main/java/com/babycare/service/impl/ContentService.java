package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<ContentEntity> findPaginated(Pageable pageable) {
		return contentDAO.findPaginated(pageable);
	}

	@Override
	public Page<ContentEntity> findPaginated(Integer page, Integer size) {
		return contentDAO.findPaginated(page, size);
	}

	@Override
	public Page<ContentEntity> findExamplePaginated(Example<ContentEntity> example, Pageable pageable) {
		return contentDAO.findExamplePaginated(example, pageable);
	}

	@Override
	public Page<ContentEntity> findExamplePaginated(Example<ContentEntity> example, Integer page, Integer size) {
		return contentDAO.findExamplePaginated(example, page, size);
	}

	@Override
	public BaseModel getByContentTypeId(Content payload) {
		return contentDAO.getByContentTypeId(payload);
	}
}
