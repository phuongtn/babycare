package com.babycare.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IContentDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;

@Repository
@Qualifier("contentDAO")
public class ContentDAO extends AbstractJpaDao<ContentEntity> implements IContentDAO {

	@Override
	public BaseModel addContent(Content payload) {
		return createEntity(new ContentEntity(payload));
	}

	@Override
	public BaseModel updateContent(Content payload) {
		return updateEntity(new ContentEntity(payload));
	}

	@Override
	public BaseModel getContentById(Content payload) {
		return findOne(payload.getContentId());
	}

	public ContentDAO() {
		super();
		setClazz(ContentEntity.class);
	}
	
}
