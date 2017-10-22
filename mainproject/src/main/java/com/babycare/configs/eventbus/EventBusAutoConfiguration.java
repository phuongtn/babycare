/*package com.babycare.configs.eventbus;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.util.Assert;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;


@Configuration
public class EventBusAutoConfiguration {

    @Bean
    public EventBusSupport eventBusWrapper() {
        return new EventBusSupportImpl(eventBus(), asyncEventBus());
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = new EventBus();
        return eventBus;
    }

    @Bean
    public AsyncEventBus asyncEventBus() {
        AsyncEventBus asyncEventBus = new AsyncEventBus("asyncDefault", Executors.newFixedThreadPool(2));
        return asyncEventBus;
    }

    @Bean
    public EventBusSubscriberBeanPostProcessor subscriberAnnotationProcessor() {
        return new EventBusSubscriberBeanPostProcessor(eventBus(), asyncEventBus());
    }

    static final class EventBusSupportImpl implements EventBusSupport {
        private EventBus eventBus;
        private AsyncEventBus asyncEventBus;

        EventBusSupportImpl(final EventBus eventBus, final AsyncEventBus asyncEventBus) {
            Assert.notNull(eventBus, "EventBus should not be null");
            Assert.notNull(asyncEventBus, "AsyncEventBus should not be null");
            this.eventBus = eventBus;
            this.asyncEventBus = asyncEventBus;
        }

        @Override
        public void post(final Object event) {
            this.eventBus.post(event);
        }

        @Override
        public void postAsync(final Object event) {
            this.asyncEventBus.post(event);
        }
    }
}*/