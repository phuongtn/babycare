package com.babycare.dao.impl;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IPushMessageDAO;
import com.babycare.dao.IPushMessagePagingRepo;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.PushMessageConstant;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;
import com.babycare.model.response.CommonResponse;

@Repository
@Component
@Qualifier("pushMessageDAO")
public class PushMessageDAO extends AbstractJpaDao<PushMessageEntity> implements IPushMessageDAO {
	@Autowired
	private IPushMessagePagingRepo repo;
	@Override
	public BaseModel deleteMessage(PushMessage pushMessage) {
		if (pushMessage != null) {
			return deleteMessageByMessageId(pushMessage.getMessageId());
		} else {
			return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
		}
	}

	@Override
	public BaseModel deleteMessageByMessageId(String messageId) {
		if (StringUtils.isNotEmpty(messageId)) {
			String hql = "DELETE PushMessageEntity WHERE messageId=:messageId";
			int result = 0;
			Query query = em.createQuery(hql);
			query.setParameter("messageId", messageId);
			try {
				result = query.executeUpdate();
			} catch (Exception e) {
				result = 0;
			}
			if (result > 0) {
				return new CommonResponse(PushMessageConstant.MESSAGE_DELETED, true);
			} else {
				return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
			}
		} else {
			return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
		}
	}

	public PushMessageDAO() {
		super();
		setClazz(PushMessageEntity.class);
	}

	@Override
	public BaseModel getByMessage(PushMessage pushMessage) {
		if (pushMessage != null ) {
			return getByMessageId(pushMessage.getMessageId());
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}

	private BaseModel getByMessageId(String messageId) {
		if (StringUtils.isNotEmpty(messageId)) {
			String hql = "FROM PushMessageEntity WHERE messageId=:messageId";
			PushMessageEntity result = null;
			try {
				result = (PushMessageEntity) em.createQuery(hql).setParameter("messageId", messageId).getSingleResult();
			} catch (Exception e) {
				result = null;
			}
			if (result != null) {
				return result;
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}

	}
	@Override
	public BaseModel deleteMessageByPushID(String pushId) {
		if (StringUtils.isNotEmpty(pushId)) {
			String hql = "DELETE PushMessageEntity WHERE pushId=:pushId";
			int result = 0;
			Query query = em.createQuery(hql);
			query.setParameter("pushId", pushId);
			try {
				result = query.executeUpdate();
			} catch (Exception e) {
				result = 0;
			}
			if (result > 0) {
				return new CommonResponse(PushMessageConstant.MESSAGE_DELETED, true);
			} else {
				return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
			}
		} else {
			return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
		}
	}

	@Override
	public BaseModel updateStatus(String messageId, String status) {
		if (StringUtils.isNotEmpty(messageId) && StringUtils.isNotEmpty(status)) {
			BaseModel baseModel = getByMessageId(messageId);
			if (baseModel instanceof PushMessageEntity) {
				PushMessageEntity record = (PushMessageEntity) baseModel;
				record.setSendStatus(status);
				return updateEntity(record);
			} else {
				return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
			}
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}

	@Override
	public Page<PushMessageEntity> findPaginated(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Page<PushMessageEntity> findPaginated(int page, int size) {
		return repo.findAll(PageRequest.of(page, size));
	}

	@Override
	public Page<PushMessageEntity> findExamplePaginated(Example<PushMessageEntity> example, Pageable pageable) {
		return repo.findAll(example, pageable);
	}

	@Override
	public Page<PushMessageEntity> findExamplePaginated(Example<PushMessageEntity> example, int page, int size) {
		return repo.findAll(example, PageRequest.of(page, size));
	}

	@Override
	public BaseModel deleteMessageBySessionId(Long sessionId) {
		if (sessionId != null) {
			String hql = "DELETE PushMessageEntity WHERE sessionId=:sessionId";
			int result = 0;
			Query query = em.createQuery(hql);
			query.setParameter("sessionId", sessionId);
			try {
				result = query.executeUpdate();
			} catch (Exception e) {
				result = 0;
			}
			if (result > 0) {
				return new CommonResponse(PushMessageConstant.MESSAGE_DELETED, true);
			} else {
				return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
			}
		} else {
			return new CommonResponse(PushMessageConstant.MESSAGE_NOT_FOUND, true);
		}
	}
}
