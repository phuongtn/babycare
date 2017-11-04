package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IContentTypeDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.entity.ContentTypeEntity;

@Repository
@Component
@Qualifier("contentTypeDAO")
public class ContentTypeDAO extends AbstractJpaDao<ContentTypeEntity> implements IContentTypeDAO {

	@Override
	public BaseModel fetchContentTypes() {
		String hql = "FROM ContentTypeEntity";
		try {
			List<BaseModel> result = (List<BaseModel>) em.createQuery(hql).getResultList();
			if (result != null || !result.isEmpty()) {
				ResultList<BaseModel> resultList = new ResultList<BaseModel>(result);
				return resultList;
			} else {
				return new ResultList<BaseModel>(new ArrayList<>());
			}
		} catch (Exception e) {
			return ErrorConstant.getError(ErrorConstant.ERROR_FETCH_CONTENT_TYPE);
		}
	}

	public ContentTypeDAO() {
		super();
		setClazz(ContentTypeEntity.class);
	}
}
