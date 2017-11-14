package com.babycare.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.babycare.dao.IOperations;
import com.babycare.dao.IPushMessageDAO;
import com.babycare.events.ChangeEvent;
import com.babycare.events.CleanUpPushMessageEvent;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.PushMessageEntity;
import com.babycare.model.payload.PushMessage;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IPushMessageService;

@Service(value = "pushMessageService")
public class PushMessageService extends AbstractJpaService<PushMessageEntity> implements IPushMessageService, ApplicationEventPublisherAware {
	private final Logger LOG = LoggerFactory.getLogger(PushMessageService.class);
	private ApplicationEventPublisher publisher;	
/*	@Value("${schedule_clean_up_push_message_in_ms: #{172800000}}")
	private long cleanUpSchedule;*/

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
	public Page<PushMessageEntity> findPaginated(Integer page, Integer size) {
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
	public Page<PushMessageEntity> findExamplePaginated(Example<PushMessageEntity> example, Integer page, Integer size) {
		return pushMessageDAO.findExamplePaginated(example, page, size);
	}

	@Override
	public BaseModel deleteMessageBySessionId(Long sessionId) {
		return pushMessageDAO.deleteMessageBySessionId(sessionId);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	@Scheduled(fixedRate = 172800000L)
	// 2 days
	void cleanUpPushMessages() {
		publisher.publishEvent(new ChangeEvent(new CleanUpPushMessageEvent()));
	}

	@Override
	public void cleanUpPushMessage() {
		pushMessageDAO.cleanUpPushMessage();
	}

}
