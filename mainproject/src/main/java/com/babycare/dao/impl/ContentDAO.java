package com.babycare.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IContentDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.Content;

@Repository
@Component
@Qualifier("contentDAO")
public class ContentDAO extends AbstractJpaDao<ContentEntity> implements IContentDAO {
	private static Logger LOG = Logger.getLogger(ContentDAO.class);
	@Override
	public BaseModel addContent(Content payload) {
		ContentEntity entity = null;
		try {
			entity = createEntity(new ContentEntity(payload));
			if (entity != null) {
				return entity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_ADD_CONTENT);
			}
		} catch(Exception e) {
			return ErrorConstant.getError(ErrorConstant.ERROR_ADD_CONTENT);
		}
	}

	@Override
	public BaseModel updateContent(Content payload) {
		ContentEntity entity = null;
		try {
			entity = updateEntity(new ContentEntity(payload));
			if (entity != null) {
				return entity;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
			}
		} catch (Exception e) {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}

	@Override
	public BaseModel getContentById(Content payload) {
		return getContent(payload);
	}

	private BaseModel getContent(Content payload) {
		if (payload != null && payload.getContentId() != null) {
			return getContent(payload.getContentId());
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}
	
	private BaseModel getContent(Long contentId) {
		ContentEntity result = null;
		try {
			result = findOne(contentId);
		} catch (Exception e) {
		}
		if (result != null) {
			return result;
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}

	public ContentDAO() {
		super();
		setClazz(ContentEntity.class);
	}
	
}
