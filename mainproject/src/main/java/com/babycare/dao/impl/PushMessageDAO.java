package com.babycare.dao.impl;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.babycare.dao.AbstractJpaDao;
import com.babycare.dao.IPushMessageDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.ErrorConstant;
import com.babycare.model.entity.PushMessageEntity;

@Repository
@Component
@Qualifier("pushMessageDAO")
public class PushMessageDAO extends AbstractJpaDao<PushMessageEntity> implements IPushMessageDAO {

	@Override
	public int deleteByMessageId(String messageId) {
		String hql = "DELETE FROM PushMessageEntity WHERE messageId = :messageId";
		Query query = em.createQuery(hql);
		query.setParameter(0, messageId);
		try {
			return query.executeUpdate();
		} catch (Exception e) {
			return -1;
		}

	}

	public PushMessageDAO() {
		super();
		setClazz(PushMessageEntity.class);
	}

	@Override
	public BaseModel getByMessageId(String messageId) {
		String hql = "FROM PushMessageEntity WHERE messageId = :messageId";
		PushMessageEntity result = null;
		try {
			result = (PushMessageEntity) em.createQuery(hql).setParameter(0, messageId).getSingleResult();
		} catch (Exception e) {
			result = null;
		}
		if (result != null) {
			return result;
		} else {
			return ErrorConstant.getError(ErrorConstant.ERROR_RECORD_NOT_FOUND);
		}
	}
}
