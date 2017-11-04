package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IPushMessageDAO;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;
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
	public BaseModel getByMessage(PushMessage pushMessage) {
		return pushMessageDAO.getByMessage(pushMessage);
	}

	@Override
	public BaseModel deleteMessage(PushMessage pushMessage) {
		return pushMessageDAO.deleteMessage(pushMessage);
	}

	@Override
	protected IOperations<PushMessageEntity> getDao() {
		return pushMessageDAO;
	}

	@Override
	public BaseModel deleteMessageByPushID(String pushId) {
		return pushMessageDAO.deleteMessageByPushID(pushId);
	}

	@Override
	public BaseModel updateStatus(String messageId, String status) {
		return pushMessageDAO.updateStatus(messageId, status);
	}

	@Override
	public BaseModel deleteMessageByMessageId(String messageId) {
		return pushMessageDAO.deleteMessageByMessageId(messageId);
	}

	@Override
	public Page<PushMessageEntity> findPaginated(int page, int size) {
		return pushMessageDAO.findPaginated(page, size);
	}

	@Override
	public Page<PushMessageEntity> findPaginated(Pageable pageable) {
		return pushMessageDAO.findPaginated(pageable);
	}

	@Override
	public Page<PushMessageEntity> findExamplePaginated(Example<PushMessageEntity> example, Pageable pageable) {
		return pushMessageDAO.findExamplePaginated(example, pageable);
	}

	@Override
	public Page<PushMessageEntity> findExamplePaginated(Example<PushMessageEntity> example, int page, int size) {
		return pushMessageDAO.findExamplePaginated(example, page, size);
	}

}
