package com.mckesson.integration.notification.config;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {
	private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

	@Bean
	public VelocityEngine velocityEngine() {
		Properties properties = new Properties();
		try {
			logger.debug("Enter into Mailconfig.velocityEngine..");

			properties.setProperty("input.encoding", "UTF-8");
			properties.setProperty("output.encoding", "UTF-8");
			properties.setProperty("resource.loader", "class");
			properties.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			logger.debug("Exit from  Mailconfig.velocityEngine..");
		} catch (Exception e) {
			logger.error("Exception while loadin velocity Engine {}", e);
		}
		return new VelocityEngine(properties);

	}

}
