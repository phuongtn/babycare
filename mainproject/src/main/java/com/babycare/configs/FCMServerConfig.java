package com.babycare.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.wedevol.xmpp.server.CcsClient;

@Configuration
@EnableScheduling
public class FCMServerConfig {
	@Autowired
	private Environment env;

	@Bean
	@Qualifier("CcsClient")
	public CcsClient ccsClient() {
		CcsClient client = CcsClient.prepareClient(env.getProperty("FCM_SENDER_KEY"), env.getProperty("FCM_SERVER_KEY"), false);
		return client;
	}
}