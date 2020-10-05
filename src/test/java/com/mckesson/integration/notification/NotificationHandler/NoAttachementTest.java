package com.mckesson.integration.notification.NotificationHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.mckesson.integration.notification.model.NotificationMetaData;
import com.mckesson.integration.notification.service.NotificationService;

@RunWith(MockitoJUnitRunner.class)
public class NoAttachementTest {

	@Mock
	NotificationService NotificationService;

	@Mock
	JavaMailSender javamailSender;

	@Mock
	MimeMessageHelper mimeMessageHelper;

	@InjectMocks
	NotificationMetaData notificationMetaData;

	String filePathPresent = ".\\src\\main\\resources\\templates\\INT001.vm";

	String filePathAbsent = ".\\src\\main\\resources\\templates\\INT00176.vm";

	BiFunction<MimeMessageHelper, String, MimeMessageHelper> fileAttachment = (mimeMessageHelper, attachment) -> {
		try {
			File file = new File(attachment);
			if (file.exists()) {
				FileSystemResource fr = new FileSystemResource(attachment);
				mimeMessageHelper.addAttachment(file.getName(), fr);
				return mimeMessageHelper;
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return null;
	};

	@Test
	public void attachementTestPass() {

		MimeMessageHelper mimeMessage = fileAttachment.apply(mimeMessageHelper, filePathPresent);
		assertNotEquals(null, mimeMessage);

	}

}
