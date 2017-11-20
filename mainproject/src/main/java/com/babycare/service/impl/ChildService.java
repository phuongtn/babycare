package com.babycare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babycare.dao.IChildDAO;
import com.babycare.dao.IOperations;
import com.babycare.events.ChangeEvent;
import com.babycare.model.BaseModel;
import com.babycare.model.entity.ChildEntity;
import com.babycare.model.payload.Child;
import com.babycare.service.AbstractJpaService;
import com.babycare.service.IChildService;

@Service(value = "childService")
public class ChildService extends AbstractJpaService<ChildEntity> implements IChildService, ApplicationEventPublisherAware {
	@Autowired
	@Qualifier("childDAO")
	private IChildDAO childDAO;

	private ApplicationEventPublisher publisher;

	@Override
	public BaseModel addChild(Child child) {
		BaseModel model = childDAO.addChild(child);
		publishEvent(child.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel updateChild(Child child) {
		BaseModel model = childDAO.updateChild(child);
		publishEvent(child.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public BaseModel removeChildById(Child payload) {
		BaseModel model = childDAO.removeChildById(payload);
		publishEvent(payload.getRequestBySessionId(), model);
		return model;
	}

	@Override
	public ChildEntity createEntity(ChildEntity entity) {
		BaseModel model = childDAO.createEntity(entity);

		return (ChildEntity) model;
	}

	@Override
	protected IOperations<ChildEntity> getDao() {
		return childDAO;
	}

	@Override
	public BaseModel getChildById(Child payload) {
		return childDAO.getChildById(payload);
	}

	@Override
	public BaseModel getChildById(Long childId) {
		return childDAO.getChildById(childId);
	}

	@Override
	public BaseModel fetchChildrenByUserId(Child payload) {
		return childDAO.fetchChildrenByUserId(payload);
	}
	
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}
	
	private void publishEvent(Long requestId, BaseModel model) {
		if (model instanceof ChildEntity) {
			model.setRequestBySessionId(requestId);
			publisher.publishEvent(new ChangeEvent(model));
		}
	}

	@Override
	public Page<ChildEntity> findPaginated(Pageable pageable) {
		return childDAO.findPaginated(pageable);
	}

	@Override
	public Page<ChildEntity> findPaginated(Integer page, Integer size) {
		return childDAO.findPaginated(page, size);
	}

	@Override
	public Page<ChildEntity> findExamplePaginated(Example<ChildEntity> example, Pageable pageable) {
		return childDAO.findExamplePaginated(example, pageable);
	}

	@Override
	public Page<ChildEntity> findExamplePaginated(Example<ChildEntity> example, Integer page, Integer size) {
		return childDAO.findExamplePaginated(example, page, size);
	}
}
