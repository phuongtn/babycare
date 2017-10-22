package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IPushMessageDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IPushMessageService;

@Service(value = "pushMessageService")
public class PushMessageService extends AbstractJpaService<PushMessageEntity> implements IPushMessageService {
	@Autowired
	@Qualifier("pushMessageDAO")
	private IPushMessageDAO pushMessageDAO;

	@Override
	public PushMessageEntity createEntity(PushMessageEntity entity) {
		return pushMessageDAO.createEntity(entity);
	}

	@Override
	public BaseModel getByMessageId(String messageId) {
		return pushMessageDAO.getByMessageId(messageId);
	}

	@Override
	public int deleteByMessageId(String messageId) {
		return pushMessageDAO.deleteByMessageId(messageId);
	}

	@Override
	protected IOperations<PushMessageEntity> getDao() {
		return pushMessageDAO;
	}

}
