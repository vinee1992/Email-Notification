package com.mckesson.integration.notification.NotificationHandler;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

import com.mckesson.integration.notification.constant.NotificationHandlerConstants;
import com.mckesson.integration.notification.exception.NotificationException;
import com.mckesson.integration.notification.model.EmailParams;
import com.mckesson.integration.notification.model.NotificationMetaData;
import com.mckesson.integration.notification.service.NotificationService;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

	@InjectMocks
	NotificationService NotificationService;

	@Mock
	JavaMailSender javamailSender;

	@InjectMocks
	NotificationMetaData notificationMetaData;

	@Test
	public void returnMimemessage() throws NotificationException, MessagingException {
		NotificationHandlerConstants notificationHandlerConstant = new NotificationHandlerConstants();
		notificationHandlerConstant.setTemplatePath("J://Sayali//dev2Prop//");

		EmailParams email = new EmailParams();

		MimeMessage message = javamailSender.createMimeMessage();

		NotificationService.transformEmail(javamailSender, email, notificationMetaData);
		assertEquals(message, NotificationService.transformEmail(javamailSender, email, notificationMetaData));

	}

}
