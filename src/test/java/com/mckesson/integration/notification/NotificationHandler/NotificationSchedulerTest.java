package com.mckesson.integration.notification.NotificationHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.mckesson.integration.notification.model.EmailParams;
import com.mckesson.integration.notification.model.EmailStatus;
import com.mckesson.integration.notification.model.NotificationMetaData;
import com.mckesson.integration.notification.repository.EmailParamRepository;
import com.mckesson.integration.notification.repository.NotificationMetadataRepository;
import com.mckesson.integration.notification.service.NotificationSchedulerService;
import com.mckesson.integration.notification.service.NotificationService;

@RunWith(MockitoJUnitRunner.class)
public class NotificationSchedulerTest {

	@InjectMocks
	NotificationSchedulerService notificationSchedulerService;

	@InjectMocks
	NotificationService notificationService;

	@InjectMocks
	NotificationMetaData notificationMetaData;

	@Mock
	EmailParamRepository emailParamRepository;

	@Mock
	NotificationMetadataRepository notificationMetadataRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void statusNew() {
		List<EmailParams> emailParamsList = emailParamRepository.findByFlag(EmailStatus.NEW.name());
		for (EmailParams e : emailParamsList) {
			assertEquals("NEW", e.getFlag());
		}
	}

	@Test
	public void statusOtherThanNew() {
		List<EmailParams> emailParamsList = emailParamRepository.findByFlag(EmailStatus.NEW.name());
		for (EmailParams e : emailParamsList) {
			assertNotEquals("Fail", e.getFlag());
		}
	}

	// if Template is Notification type is not present in metadata , it is mandatory
	// that all the notification type should be present in meta data table
	@Test()
	public void nullNotificationMetadata() {
		NotificationMetaData notificationMetaData = notificationMetadataRepository.findBynotificationType("INT0011");
		assertEquals(null, notificationMetaData);

	}

	// accessing private methods
	@Test
	public void recipientList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = notificationService.getClass().getDeclaredMethod("prepareRecipientsList", String.class);
		method.setAccessible(true);
		String emailAddress = "vineet.chaurasia@genpact.com,prashant.jadhav@genpact.com";
		method.invoke(notificationService, emailAddress);
		String emailAddresswithoutDelimiter = "vineet.chaurasia@genpact.com";
		method.invoke(notificationService, emailAddresswithoutDelimiter);

	}

	@Test
	public void safeAttachment() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method method = notificationService.getClass().getDeclaredMethod("emailContentParserMDM", String.class);
		method.setAccessible(true);
		String mailBody = "{\"errorCodeName\":null,\"errorName\":null,\"groupName\":null,\"jobNameList\":null,\"jobWithFileList\":null,\"emailSubject\":null}";
		method.invoke(notificationService, mailBody);
	}
}
