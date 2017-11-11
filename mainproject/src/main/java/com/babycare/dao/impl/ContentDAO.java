package com.babycare.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IContentDAO;
import com.babycare.dao.IContentPagingRepo;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.common.ResultListEx;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.payload.Content;

@Repository
@Component
@Qualifier("contentDAO")
public class ContentDAO extends AbstractJpaDao<ContentEntity> implements IContentDAO {
	private static Logger LOG = Logger.getLogger(ContentDAO.class);
	@Autowired
	private IContentPagingRepo repo;
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

	@Override
	public Page<ContentEntity> findPaginated(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Page<ContentEntity> findPaginated(int page, int size) {
		return repo.findAll(PageRequest.of(page, size));
	}

	@Override
	public Page<ContentEntity> findExamplePaginated(Example<ContentEntity> example, Pageable pageable) {
		return repo.findAll(example, pageable);
	}

	@Override
	public Page<ContentEntity> findExamplePaginated(Example<ContentEntity> example, int page, int size) {
		return repo.findAll(example, PageRequest.of(page, size));
	}

	@SuppressWarnings("null")
	@Override
	public BaseModel getByContentTypeId(Content payload) {
		if (payload == null || payload.getContentTypeId() == null) {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		} else {
			final String hql = "FROM ContentEntity WHERE contentTypeId=:contentTypeId";
			try {
				@SuppressWarnings("unchecked")
				List<ContentEntity> result = (List<ContentEntity>)em.createQuery(hql).
						setParameter("contentTypeId", payload.getContentTypeId()).getResultList();
				if (result != null && !result.isEmpty()) {
					ResultListEx<ContentEntity> resultList = new ResultListEx<ContentEntity>(result);
					return resultList;
				} else {
					return new ResultListEx<BaseModel>(new ArrayList<BaseModel>());
				}
			} catch (Exception e) {
				return ErrorConstant.getError(ErrorConstant.ERROR_FETCH_DATA);
			}
		}
	}

}
