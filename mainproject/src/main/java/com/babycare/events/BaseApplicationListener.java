package com.babycare.events;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.babycare.service.IChildService;
import com.babycare.service.IPushMessageService;
import com.babycare.service.ISessionService;
import com.babycare.service.IUserService;
import com.wedevol.xmpp.server.CcsClient;

public abstract class BaseApplicationListener implements ApplicationListener<ApplicationEvent> {
	@PersistenceContext
	protected EntityManager em;

	@Autowired
	@Qualifier("sessionService")
	protected ISessionService sessionService;

	@Autowired
	@Qualifier("pushMessageService")
	protected IPushMessageService pushMessageService;

	@Autowired
	@Qualifier("userService")
	protected IUserService userService;

	@Autowired
	@Qualifier("childService")
	protected IChildService childService;
	
	@Autowired
	@Qualifier("CcsClient")
	private CcsClient ccsClient;
	
	public abstract void onApplicationEvent(ApplicationEvent event);

}
