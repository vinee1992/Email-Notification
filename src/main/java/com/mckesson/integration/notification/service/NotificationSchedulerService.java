package com.mckesson.integration.notification.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mckesson.integration.notification.constant.Constants;
import com.mckesson.integration.notification.constant.NotificationHandlerConstants;
import com.mckesson.integration.notification.exception.NotificationException;
import com.mckesson.integration.notification.model.BrNotificationDetail;
import com.mckesson.integration.notification.model.DocumentUpload;
import com.mckesson.integration.notification.model.EmailParams;
import com.mckesson.integration.notification.model.EmailStatus;
import com.mckesson.integration.notification.model.NotificationMetaData;
import com.mckesson.integration.notification.repository.BrDetailsRepository;
import com.mckesson.integration.notification.repository.DocumentUploadRepository;
import com.mckesson.integration.notification.repository.EmailParamRepository;
import com.mckesson.integration.notification.repository.NotificationMetadataRepository;

/*
 * Author - Vinay Jain
 * Purpose - To schedule email notifications.
 */

@Service
public class NotificationSchedulerService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationSchedulerService.class);
	@Autowired
	TaskScheduler scheduler;
	@Autowired
	EmailParamRepository emailParamRepository;
	@Autowired
	NotificationMetadataRepository notificationMetadataRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	NotificationService notificationService;

	@Autowired
	NotificationHandlerConstants notificationHandlerConstant;

	@Autowired
	BrDetailsRepository brDetailsRepository;

	@Autowired
	DocumentUploadRepository documentUploadRepository;

	@Autowired
	Environment env;

	/*
	 * Scheduler which reads email_params table based on the interval defined in the
	 * property file and call NotificationService methods to prepare and send mail.
	 */

	// int count = 0;

	@Scheduled(fixedDelayString = "${notification.triggerInterval}")
	public void triggerNotificationSender() throws IOException {
		logger.debug("Next scheduled batch started...");

		List<EmailParams> emailParamsList = emailParamRepository.findByFlag(EmailStatus.NEW.name());

		if (!emailParamsList.isEmpty()) {
			logger.debug("Entry into NotificationSchedulerService.triggerNotificationSender");

			emailParamsList.forEach(emailParam -> {
				logger.info("EmailParam {}", emailParam);
				try {
					NotificationMetaData notificationMetaData = notificationMetadataRepository
							.findBynotificationType(emailParam.getNotificationType());
					if (notificationMetaData != null) {
						if (emailParam.getMailTo() == null || emailParam.getMailFrom() == null
								|| emailParam.getSubject() == null) {
							String subject = (emailParam.getSubject() == null ? notificationMetaData.getSubject()
									: emailParam.getSubject());
							String mailFrom = (emailParam.getMailFrom() == null ? notificationMetaData.getMailFrom()
									: emailParam.getMailFrom());
							String mailTo = (emailParam.getMailTo() == null ? notificationMetaData.getRecipientList()
									: emailParam.getMailTo());
							emailParam.setSubject(subject);
							emailParam.setMailFrom(mailFrom);
							emailParam.setMailTo(mailTo);
							emailParamRepository.updateEmailParamsMailToFromAndSubjectByRecId(subject, mailFrom, mailTo,
									emailParam.getRecId());
						}
						MimeMessage mimeMessage = notificationService.transformEmail(javaMailSender, emailParam,
								notificationMetaData);

						if (mimeMessage != null) {
							javaMailSender.send(mimeMessage);
							logger.info("Email sent successfully To {}",
									Arrays.toString(mimeMessage.getAllRecipients()));

							emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.SUCCESS.name(),
									emailParam.getRecId(), new Date());

							if (!(emailParam.getNotificationType().equals(notificationHandlerConstant.getTemplate2())
									|| emailParam.getNotificationType()
											.equals(notificationHandlerConstant.getTemplate1())
									|| emailParam.getNotificationType()
											.equals(notificationHandlerConstant.getTemplate3())
									|| emailParam.getNotificationType()
											.equals(notificationHandlerConstant.getTemplate4())
									|| emailParam.getNotificationType()
											.equals(notificationHandlerConstant.getTemplate5())
									|| emailParam.getNotificationType()
											.equals(notificationHandlerConstant.getTemplate6()))) {

								int recId = emailParamRepository.fetchRecID(emailParam.getRecId());

								List<BrNotificationDetail> brNotificationDetails = brDetailsRepository
										.findByRecId(recId);

								logger.info("lis size {},", brNotificationDetails.size());

								if (brNotificationDetails != null && !brNotificationDetails.isEmpty()) {

									BrNotificationDetail brNotificationDetail = brNotificationDetails.get(0);
									try {

										String subject = emailParam.getSubject();
										String removeSpecialCharacterSubject = subject.replaceAll("[:/]", "-");
										String path = env.getProperty("eml.file.path");
										String directoryName = path
												.concat(String.valueOf(brNotificationDetail.getWqId()));
										String fileName = removeSpecialCharacterSubject + Constants.HYPHEN
												+ brNotificationDetail.getWqId() + Constants.EML_EXTENSION;
										File directory = new File(directoryName);
										logger.info("fileName {},", fileName);

										if (!directory.exists()) {
											directory.mkdir();
										}

										File file = new File(directory + File.separator + fileName);
										mimeMessage.writeTo(new FileOutputStream(file));

										DocumentUpload documentUpload = new DocumentUpload();
										documentUpload.setFileName(fileName);
										documentUpload.setFilePath(directory + File.separator);
										documentUpload.setFileType(Constants.EML_EXTENSION);

										if ("INT009".equals(emailParam.getNotificationType())) {
											documentUpload.setFileDesc(emailParam.getSubject());
										} else {
											documentUpload.setFileDesc("Supplier Notification" + Constants.HYPHEN
													+ emailParam.getSubject());
										}

										documentUpload.setWqId(brNotificationDetail.getWqId());
										documentUpload.setCreationDate(new Date());
										documentUpload.setCreatedBy(Constants.SYSTEM);
										documentUploadRepository.save(documentUpload);
									} catch (IOException | MessagingException e) {
										logger.error("Exception occured while saving file into .eml formate  {}", e);
									} catch (Exception e) {
										logger.error("Exception occured while tagging WQ with .eml file  {}", e);
									}

								} else {
									logger.debug("no Rec_id mapped with Email_params_id in BRNotification collection ");
								}
							}

						} else {
							if (emailParam.getNotificationType().equals(notificationHandlerConstant.getTemplate5())) {
								logger.debug("group Name can not be null");
							} else {
								logger.debug("No Attachment found at the path mentioned for Rec_id {}",
										emailParam.getRecId());
							}
						}

					} else {
						logger.info(
								"Notification type from notification metadata does not matched with notification type fron email params table");
					}

				} catch (NullPointerException fe) {

					emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.FAILURE.name(), emailParam.getRecId(),
							null);
					logger.error(fe.getMessage());
				} catch (NotificationException | MessagingException e) {

					logger.error("Exception {}", e);
					MimeMessage mimeMessage;
					try {
						emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.FAILURE.name(),
								emailParam.getRecId(), null);
						logger.info(
								"status flag updated to Failure in email params table , email not sent , so sending exception template to recipients .");
						mimeMessage = notificationService.transformEmailForException(javaMailSender, e, emailParam);
						javaMailSender.send(mimeMessage);
					} catch (AddressException e1) {
						logger.error(e1.getMessage(), e1);
					} catch (MessagingException e2) {
						logger.error(e2.getMessage(), e2);
					}

				} catch (Exception e) {
					emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.FAILURE.name(), emailParam.getRecId(),
							null);
					logger.error(e.getMessage());
				}
			});
		} else {
			logger.info("None of the record have New status");
		}
		logger.debug("Exit from NotificationSchedulerService.triggerNotificationSender");

	}
}
