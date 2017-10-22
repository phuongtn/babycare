/*package com.babycare.configs.eventbus;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.util.Assert;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class EventBusSubscriberBeanPostProcessor implements BeanPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(EventBusSubscriberBeanPostProcessor.class);

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;

    @Autowired
    public EventBusSubscriberBeanPostProcessor(final EventBus eventBus, final AsyncEventBus asyncEventBus) {
        Assert.notNull(eventBus, "EventBus should not be null");
        Assert.notNull(asyncEventBus, "AsyncEventBus should not be null");
        this.eventBus = eventBus;
        this.asyncEventBus = asyncEventBus;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

        for (Method m : bean.getClass().getMethods()) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                logger.info("register bean as subscriber");
                this.eventBus.register(bean);
                this.asyncEventBus.register(bean);
                break;
            }
        }

        return bean;
    }

}*/