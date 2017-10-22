package com.babycare.events;

import org.springframework.context.ApplicationEvent;

import com.babycare.model.BaseModel;

public abstract class BaseApplicationEvent<T extends BaseModel > extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	protected BaseModel model;

	public BaseApplicationEvent(T source) {
		super(source);
		model = source;
	}
	
	public BaseModel getModel() {
		return model;
	}
}
