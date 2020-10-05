package com.mckesson.integration.notification.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mckesson.integration.notification.constant.NotificationHandlerConstants;
import com.mckesson.integration.notification.controller.NotificationController;
import com.mckesson.integration.notification.repository.BrDetailsRepository;
import com.mckesson.integration.notification.service.NotificationSchedulerService;
import com.mckesson.integration.notification.service.NotificationService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@ComponentScan(basePackageClasses = { NotificationController.class, NotificationSchedulerService.class,
		NotificationHandlerConstants.class, BrDetailsRepository.class })
@EntityScan(basePackages = "com.mckesson.integration.notification.model")
@EnableJpaRepositories(basePackages = "com.mckesson.integration.notification.repository")
@EnableMongoRepositories({ "com.mckesson.integration.notification.repository" })
@Import({ ApplicationConfiguration.class, MailConfig.class })
@EnableTransactionManagement
@EnableScheduling
@EnableWebMvc
@EnableEncryptableProperties
@PropertySource("classpath:application.properties")
@PropertySources({ @PropertySource({ "classpath:email-config.properties" }),
		@PropertySource(value = "file:./resources/email-config.properties", ignoreResourceNotFound = true), })
public class NotificationHandlerApplication {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	public static void main(String[] args) {

		logger.debug("Entry into NotificationHandlerApplication.main method...");
		InputStream fis = NotificationHandlerApplication.class.getClassLoader()
				.getResourceAsStream("application.properties");
		Properties prop = new Properties();
		try {
			prop.load(fis);
		} catch (IOException e) {
			logger.error("Exception while loading property file {}", e);
		}
		String externalPropertiesFilePath = prop.getProperty("external.properties.common.file.path");
		String externalPropertiesFileName = prop.getProperty("external.properties.common.file.name");
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(
				NotificationHandlerApplication.class)
						.properties("spring.config.name:" + externalPropertiesFileName,
								"spring.config.location:file:" + externalPropertiesFilePath)
						.build().run(args);

		logger.debug("Exit  from NotificationHandlerApplication.main method...{}",
				applicationContext.getApplicationName());
	}

}
