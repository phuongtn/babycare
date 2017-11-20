package com.babycare.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babycare.dao.IContentDAO;
import com.babycare.dao.IOperations;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.ResultList;
import com.babycare.model.common.ResultListEx;
import com.babycare.model.entity.ChildEntity;
import com.babycare.model.entity.ContentEntity;
import com.babycare.model.entity.ContentTypeEntity;
import com.babycare.model.payload.Content;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IContentService;
import com.babycare.utils.DateTimeUtils;

@Service(value = "contentService")
public class ContentService extends AbstractJpaService<ContentEntity> implements IContentService {
	@Autowired
	@Qualifier("contentTypeService")
	private ContentTypeService contentTypeService;

	@Autowired
	@Qualifier("childService")
	private ChildService childService;

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

	@Override
	public BaseModel getContentByWeekAndContentTypeId(Integer weekNumber, Long contentTypeId) {
		return contentDAO.getContentByWeekAndContentTypeId(weekNumber, contentTypeId);
	}

	@Override
	public BaseModel getContentByWeek(Integer weekNumber) {
		BaseModel baseModel = contentTypeService.fetchContentTypes();
		List<ContentEntity> contentEntities = new ArrayList<>();
		if (baseModel instanceof ResultListEx) {
			List<ContentTypeEntity> contentTypeEntities = ((ResultListEx) baseModel).getResultList();
			if (contentTypeEntities != null & !contentTypeEntities.isEmpty()) {
				for (ContentTypeEntity contentTypeEntity : contentTypeEntities) {
					BaseModel model = getContentByWeekAndContentTypeId(weekNumber, contentTypeEntity.getContentTypeId());
					if (model instanceof ContentEntity) {
						contentEntities.add((ContentEntity)model);
					}
				}
			}
		}
		return new ResultListEx<ContentEntity>(contentEntities);
	}

	@Override
	public BaseModel getContentByContentTypeIdChildId(Long contentTypeId, Long childId) {
		return getContentByChildID(contentTypeId, childId);
	}

	@Override
	public BaseModel getContentByChildId(Long childId) {
		return getContentByChildID(null, childId);
	}

	private BaseModel getContentByChildID(Long contentTypeId, Long childId) {
		if (childId != null) {
			BaseModel childModel = childService.getChildById(childId);
			if (childModel instanceof ChildEntity) {
				ChildEntity childEntity = (ChildEntity) childModel;
				int weekDiff = childEntity.getDob() != null ? DateTimeUtils.getWeekDiffFromNow(childEntity.getDob()) : -1;
				if (weekDiff != -1) {
					if (contentTypeId != null) {
						return getContentByWeekAndContentTypeId(weekDiff, contentTypeId);
					} else {
						return getContentByWeek(weekDiff);
					}
				} else {
					return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
				}
			} else {
				 return ErrorConstant.getError(ErrorConstant.ERROR_CHILD_NOT_EXIST);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_INPUT_ERROR);
		}
	}
}
