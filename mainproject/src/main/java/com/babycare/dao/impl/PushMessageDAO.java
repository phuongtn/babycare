package com.babycare.dao.impl;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IPushMessageDAO;
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

	@Override
	public BaseModel deleteMessage(PushMessage pushMessage) {
		if (pushMessage != null && StringUtils.isNotEmpty(pushMessage.getMessageId())) {
			String hql = "DELETE PushMessageEntity WHERE messageId=:messageId";
			int result = 0;
			Query query = em.createQuery(hql);
			query.setParameter("messageId", pushMessage.getMessageId());
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
		if (pushMessage != null && StringUtils.isNotEmpty(pushMessage.getMessageId())) {
			String hql = "FROM PushMessageEntity WHERE messageId=:messageId";
			PushMessageEntity result = null;
			try {
				result = (PushMessageEntity) em.createQuery(hql).setParameter("messageId", pushMessage.getMessageId()).getSingleResult();
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
		return null;
	}
}
