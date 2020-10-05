package com.mckesson.integration.notification.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.validation.constraints.Email;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckesson.integration.notification.constant.Constants;
import com.mckesson.integration.notification.constant.NotificationHandlerConstants;
import com.mckesson.integration.notification.exception.NotificationException;
import com.mckesson.integration.notification.model.BRTemplateNotificationParam;
import com.mckesson.integration.notification.model.BRTemplateParams;
import com.mckesson.integration.notification.model.EmailParams;
import com.mckesson.integration.notification.model.EmailStatus;
import com.mckesson.integration.notification.model.ErrorRequestData;
import com.mckesson.integration.notification.model.NotificationMetaData;
import com.mckesson.integration.notification.repository.EmailParamRepository;

@Service
public class NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	public static final String INSERT_TIME = "insertTime";
	public static final String FILE_TEMPLATE = "file:./templates/";
	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${mail.from}")
	private String emailFrom;

	@Value("${mail.to}")
	private String exceptionMailrecipientList;

	@Value("${subject}")
	private String exceptionMailSubject;

	@Value("${notification.type}")
	private String notificationType;

	@Email(message = "Email should be valid")
	private String emailAddress;

	@Autowired
	Environment env;

	@Autowired
	EmailParamRepository emailParamRepository;

	@Autowired
	NotificationHandlerConstants notificationHandlerConstant;

	public NotificationService() {
		super();
	}

	/*
	 * @param emailRecipientsList - {@link EmailParams#mailTo}.
	 * 
	 * @return InternetAddress[] recipientsAddress - list of recipients email
	 * addresses.
	 */
	private InternetAddress[] prepareRecipientsList(String emailRecipientsList) throws AddressException {
		InternetAddress[] recipientAddress = null;
		logger.debug("Entry into NotificationSchedulerService.prepareRecipientsList");

		if (StringUtils.isNotBlank(emailRecipientsList)) {
			logger.info("emailRecipints {} ", emailRecipientsList);

			if (emailRecipientsList.contains(",") && emailRecipientsList.indexOf(',') >= 1) {
				List<String> recipientList = Arrays.asList(emailRecipientsList.split(","));
				recipientAddress = new InternetAddress[recipientList.size()];
				int counter = 0;
				logger.info("recipientList {} ", recipientList);

				for (String recipient : recipientList) {
					this.emailAddress = recipient;
					recipientAddress[counter] = new InternetAddress(this.emailAddress.trim());
					counter++;
				}

				return recipientAddress;
			} else {
				List<String> recipientList = new ArrayList<>();
				recipientList.add(emailRecipientsList);
				recipientAddress = new InternetAddress[recipientList.size()];
				logger.info("recipientList {} ", recipientList);

				int counter = 0;
				for (String recipient : recipientList) {
					this.emailAddress = recipient;
					recipientAddress[counter] = new InternetAddress(this.emailAddress.trim());
					counter++;
				}
				logger.debug("Exit  from   NotificationSchedulerService.prepareRecipientsList");

				return recipientAddress;
			}
		}
		return recipientAddress;
	}

	/*
	 * null check, if attachments array is null then return emptyList else return
	 * the list, To avoid Null pointer exception
	 * 
	 * @param attachments - List of email attachment
	 */
	private List<String> safe(List<String> attachments) {
		return attachments == null ? Collections.emptyList() : attachments;
	}

	/*
	 * Converting Json to BRTemplateParams object
	 * 
	 * @param mailContent - {@link EmailParams#mailBodyParams}
	 * 
	 * @return bRTemplateParams - email template place holders BRTemplateParams
	 * 
	 */



	private ErrorRequestData emailContentParserMDM(String mailContent) throws IOException {
		return new ObjectMapper().readValue(mailContent, ErrorRequestData.class);
	}

	BiConsumer<MimeMessageHelper, String> fileAttachment = (mimeMessageHelper, attachment) -> {
		try {
			File file = new File(attachment);
			if (file.exists()) {
				FileSystemResource fr = new FileSystemResource(attachment);
				mimeMessageHelper.addAttachment(file.getName(), fr);
			}
		} catch (Exception e) {
			logger.error("Exception occured while attaching the file  {} ", e);
		}
	};

	/*
	 * prepare recipients list, email template and set the MineMessage obejct which
	 * then will be sent by sendNotification method
	 * 
	 * @param session - {@link NotificationSchedulerService.session} email session
	 * to send mail
	 * 
	 * @param emailParams - one row of email_params table contains mail details
	 * 
	 * @param notificationMetaData - one row of notification_metadata table contains
	 * mapping details
	 * 
	 * @return mimeMessage - email message with all the required info like
	 * recipients, subject etc
	 */
	public MimeMessage transformEmail(JavaMailSender javaMailSender, EmailParams emailParams,
			NotificationMetaData notificationMetaData) throws NotificationException, MessagingException {
		logger.debug("Entry into NotificationService.transformEmail");
		String templatePath = notificationHandlerConstant.getTemplatePath();
		MimeMessage message = javaMailSender.createMimeMessage();
		Template template = null;

		Properties p = new Properties();
		p.setProperty("file.resource.loader.path", templatePath);
		Velocity.init(p);

		// this.velocityEngine.setProperty("file.resource.loader.path", templatePath);
		// template = this.velocityEngine.getTemplate("INT005" + Constants.EXTENSION);

		VelocityContext velocityContext = null;

		MimeMessageHelper mimeMessageHelper = null;
		try {
			InternetAddress[] recipientAddress;
			InternetAddress[] recipientAddressCC;
			InternetAddress[] recipientAddressbCC;
			InternetAddress[] recipientAddressReplyTo;
			logger.debug("calling   NotificationService.prepareRecipientsList");

			recipientAddress = prepareRecipientsList(emailParams.getMailTo());
			recipientAddressCC = prepareRecipientsList(emailParams.getMailCc());
			recipientAddressbCC = prepareRecipientsList(emailParams.getMailBcc());
			recipientAddressReplyTo = prepareRecipientsList(emailParams.getReplyTo());
			if (emailParams.getNotificationType().equals(notificationHandlerConstant.getTemplate2())
					|| emailParams.getNotificationType().equals(notificationHandlerConstant.getTemplate1())
					|| emailParams.getNotificationType().equals(notificationHandlerConstant.getTemplate3())
					|| emailParams.getNotificationType().equals(notificationHandlerConstant.getTemplate4())) {
				logger.info("Temlate Name {} ", notificationHandlerConstant.getTemplate2());
				ErrorRequestData templateData = emailContentParserMDM(emailParams.getMailBodyParams());
				mimeMessageHelper = new MimeMessageHelper(message, true);
				mimeMessageHelper.setSubject(emailParams.getSubject());
				mimeMessageHelper.setFrom(new InternetAddress(emailParams.getMailFrom()));
				if (recipientAddress != null) {
					mimeMessageHelper.setTo(recipientAddress);
				}
				if (recipientAddressCC != null) {
					mimeMessageHelper.setCc(recipientAddressCC);
				}
				if (recipientAddressbCC != null) {
					mimeMessageHelper.setBcc(recipientAddressbCC);
				}

				if (recipientAddressReplyTo != null) {
					mimeMessageHelper.setReplyTo(recipientAddressReplyTo[0]);
				}

				template = Velocity.getTemplate(notificationMetaData.getTemplateName() + Constants.EXTENSION);

				/*
				 * template = this.velocityEngine.getTemplate("./" + File.separator +
				 * Constants.TEMPLATES + File.separator + notificationMetaData.getTemplateName()
				 * + Constants.EXTENSION);
				 */

				velocityContext = new VelocityContext();
				velocityContext.put("errorCodeName", templateData.getErrorCodeName());
				velocityContext.put("jobName", templateData.getJobNameList());
				velocityContext.put("groupName", templateData.getGroupName());
				velocityContext.put("jobNameWithFile", templateData.getJobWithFileList());
				velocityContext.put(INSERT_TIME, emailParams.getInsertDate());
			} else if (notificationHandlerConstant.getTemplate5().equals(emailParams.getNotificationType())) {
				ErrorRequestData templateData = emailContentParserMDM(emailParams.getMailBodyParams());
				if (StringUtils.isNotBlank(templateData.getGroupName()) && templateData.getGroupName() != null) {
					mimeMessageHelper = new MimeMessageHelper(message, true);
					mimeMessageHelper.setSubject(emailParams.getSubject());
					mimeMessageHelper.setFrom(new InternetAddress(emailParams.getMailFrom()));
					if (recipientAddress != null) {
						mimeMessageHelper.setTo(recipientAddress);
					}
					if (recipientAddressCC != null) {
						mimeMessageHelper.setCc(recipientAddressCC);
					}
					if (recipientAddressbCC != null) {
						mimeMessageHelper.setBcc(recipientAddressbCC);
					}

					if (recipientAddressReplyTo != null) {
						mimeMessageHelper.setReplyTo(recipientAddressReplyTo[0]);
					}

					template = Velocity.getTemplate(notificationMetaData.getTemplateName() + Constants.EXTENSION);
					/*
					 * template = this.velocityEngine.getTemplate("./" + File.separator +
					 * Constants.TEMPLATES + File.separator + notificationMetaData.getTemplateName()
					 * + Constants.EXTENSION);
					 */

					StringBuilder statitics = readDataLineByLine(templateData.getGroupName());
					List<String> attachmentsplit = new ArrayList<>();
					if (attachmentException.toString().contains(",")
							&& attachmentException.toString().indexOf(',') >= 1) {
						attachmentsplit = Arrays.asList(attachmentException.toString().split(","));
					} else {
						attachmentsplit.add(attachmentException.toString());
					}
					for (String attachment : safe(attachmentsplit)) {
						if (StringUtils.isNotBlank(attachment)
								&& !"Link Of Exception File ".equalsIgnoreCase(attachment)
								&& !"NA".equalsIgnoreCase(attachment)) {
							logger.info("Attachment {} ", attachment);

							fileAttachment.accept(mimeMessageHelper, attachment);
						}
					}

					velocityContext = new VelocityContext();
					velocityContext.put("statitics", statitics);
					velocityContext.put(INSERT_TIME, emailParams.getInsertDate());
				} else {
					logger.debug("Group Name cannot be null , hence mail will not trigger");
					emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.FAILURE.name(),
							emailParams.getRecId(), null);
				}

			} else if (notificationHandlerConstant.getTemplate6().equals(emailParams.getNotificationType())) {

				ErrorRequestData templateData = emailContentParserMDM(emailParams.getMailBodyParams());

				if (StringUtils.isNotBlank(templateData.getFileName())) {
					mimeMessageHelper = new MimeMessageHelper(message, true);
					mimeMessageHelper.setSubject(emailParams.getSubject());
					mimeMessageHelper.setFrom(new InternetAddress(emailParams.getMailFrom()));
					if (recipientAddress != null) {
						mimeMessageHelper.setTo(recipientAddress);
					}
					if (recipientAddressCC != null) {
						mimeMessageHelper.setCc(recipientAddressCC);
					}
					if (recipientAddressbCC != null) {
						mimeMessageHelper.setBcc(recipientAddressbCC);
					}

					if (recipientAddressReplyTo != null) {
						mimeMessageHelper.setReplyTo(recipientAddressReplyTo[0]);
					}

					template = Velocity.getTemplate(notificationMetaData.getTemplateName() + Constants.EXTENSION);
					/*
					 * template = this.velocityEngine.getTemplate("./" + File.separator +
					 * Constants.TEMPLATES + File.separator + notificationMetaData.getTemplateName()
					 * + Constants.EXTENSION);
					 */

					// StringBuilder statitics = readDataForRollupAndOB(templateData.getFileName());
					if (templateData.getFileName() != null) {
						// fileAttachment.accept(mimeMessageHelper, templateData.getFileName());

						File file = new File(templateData.getFileName());
						if (file.exists()) {
							FileSystemResource fr = new FileSystemResource(templateData.getFileName());
							mimeMessageHelper.addAttachment(file.getName(), fr);
						} else {
							logger.error(
									"No attachement found at , hence mail will not trigger. Please configure CSV file at location to get an email {}",
									templateData.getFileName());
							return null;
						}
					}

					// create a calendar
					// Calendar cal = Calendar.getInstance();

					// get the time zone
					// TimeZone tz = cal.getTimeZone();
					velocityContext = new VelocityContext();
					if ("Outbound job".equalsIgnoreCase(templateData.getApplicationName())) {
						velocityContext.put("message", Constants.OUTBOUND_MESSAGE);
					} else {
						velocityContext.put("message", Constants.ROLLUP_MESSAGE);
					}

					velocityContext.put("applicationName", templateData.getApplicationName());
					/*
					 * applicationName velocityContext.put(INSERT_TIME, emailParams.getInsertDate()
					 * + " " + tz.getDisplayName(false, TimeZone.SHORT));
					 */
				} else {
					logger.debug("filename cannot be  null , hence mail will not trigger");
					emailParamRepository.updateEmailParamsFlagByRecId(EmailStatus.FAILURE.name(),
							emailParams.getRecId(), null);
				}

			} else {

				/*
				 * BRTemplateNotificationParam brTemplateParams = emailContentParserforInt007(
				 * emailParams.getMailBodyParams());
				 */

				velocityContext = new VelocityContext();
				List<String> attachmentLocation = null;

				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> BrData = new HashMap<String, Object>();
				try {
					BrData = mapper.readValue(emailParams.getMailBodyParams(),
							new TypeReference<Map<String, Object>>() {
							});
				} catch (Exception e) {
					logger.error("Error while parsing json body", e);
				}

				mimeMessageHelper = new MimeMessageHelper(message, true);
				mimeMessageHelper.setSubject(emailParams.getSubject());
				mimeMessageHelper.setFrom(new InternetAddress(emailParams.getMailFrom()));
				if (recipientAddress != null) {
					mimeMessageHelper.setTo(recipientAddress);
				}
				if (recipientAddressCC != null) {
					mimeMessageHelper.setCc(recipientAddressCC);
				}
				if (recipientAddressbCC != null) {
					mimeMessageHelper.setBcc(recipientAddressbCC);
				}

				if (recipientAddressReplyTo != null) {
					mimeMessageHelper.setReplyTo(recipientAddressReplyTo[0]);
				}
				String parentFilePath = env.getProperty("attachmentpath.for.BR.email");

				for (String key : BrData.keySet()) {
					Object value = BrData.get(key);
					try {
						if (value instanceof List) {
							attachmentLocation = safe((List<String>) value);
							for (String attachment : attachmentLocation) {
								File file = new File(parentFilePath + attachment);

								if (file.exists()) {
									FileSystemResource fr = new FileSystemResource(parentFilePath + attachment);
									mimeMessageHelper.addAttachment(file.getName(), fr);
								} else {
									return null;
								}

							}
						}

						else if (value instanceof String) {
							velocityContext.put(key, value);
						} else {
							velocityContext.put(key, value);
						}
					} catch (Exception e) {
						logger.error("Error while extracting data from json body", e);

					}
				}
				// SimpleDateFormat formatter = new
				// SimpleDateFormat(env.getProperty("Br.dateFormate"));
				// String format = formatter.format(new Date());
				// velocityContext.put(env.getProperty("Br.currentDate"), format);
				/*
				 * template = this.velocityEngine.getTemplate("./" + File.separator +
				 * Constants.TEMPLATES + File.separator + notificationMetaData.getTemplateName()
				 * + Constants.EXTENSION);
				 */

				template = Velocity.getTemplate(notificationMetaData.getTemplateName() + Constants.EXTENSION);

			} /*
				 * else if (emailParams.getNotificationType().equals("INT009")) {
				 * BRTemplateParams brTemplateParams =
				 * emailContentParser(emailParams.getMailBodyParams()); mimeMessageHelper = new
				 * MimeMessageHelper(message, true);
				 * mimeMessageHelper.setSubject(emailParams.getSubject());
				 * mimeMessageHelper.setFrom(new InternetAddress(emailParams.getMailFrom())); if
				 * (recipientAddress != null) { mimeMessageHelper.setTo(recipientAddress); } if
				 * (recipientAddressCC != null) { mimeMessageHelper.setCc(recipientAddressCC); }
				 * if (recipientAddressbCC != null) {
				 * mimeMessageHelper.setBcc(recipientAddressbCC); }
				 * 
				 * if (recipientAddressReplyTo != null) {
				 * mimeMessageHelper.setReplyTo(recipientAddressReplyTo[0]); } String
				 * parentFilePath = env.getProperty("attachmentpath.for.BR.email"); for (String
				 * attachment : safe(brTemplateParams.getAttachments())) { File file = new
				 * File(parentFilePath + attachment); FileSystemResource fr = new
				 * FileSystemResource(parentFilePath + attachment);
				 * mimeMessageHelper.addAttachment(file.getName(), fr); }
				 * 
				 * template = this.velocityEngine.getTemplate(Constants.FILE + File.separator +
				 * Constants.TEMPLATES + File.separator + notificationMetaData.getTemplateName()
				 * + Constants.EXTENSION);
				 * 
				 * velocityContext = new VelocityContext(); velocityContext.put("supplierName",
				 * brTemplateParams.getSupplierName()); velocityContext.put("contractIdsList",
				 * brTemplateParams.getContractIds()); velocityContext.put("contractDate",
				 * brTemplateParams.getExpiryDate());
				 * /azfileshare/Application/cca-notification/output }
				 */
			StringWriter stringWriter = new StringWriter();
			if (template != null)
				template.merge(velocityContext, stringWriter);
			if (mimeMessageHelper != null) {
				logger.info("template name {}", template.getName());
				mimeMessageHelper.setText(stringWriter.toString(), true);
				logger.debug("Exit from NotificationService.transformEmail");
				return mimeMessageHelper.getMimeMessage();
			} else {
				return null;
			}
		} catch (AddressException e) {
			logger.error("AddressException while transforming / genrating mime message {}", e);
			throw new NotificationException("Exception while preparing recipients list", e);
		} catch (MessagingException | ResourceNotFoundException e) {
			logger.error("MessagingException/ResourceNotFoundException while transforming / genrating mime message {}",
					e);
			throw new NotificationException("Exception in Transforming Email", e);
		} catch (NullPointerException | IOException fe) {
			logger.error("NullPointerException/IOException while transforming / genrating mime message {}", fe);
		} catch (Exception e) {
			logger.error("Exception while transforming / genrating mime message {}", e.getMessage());
		}
		return null;
	}

	/*
	 * prepare email with exception details to send it to support team by
	 * sendNotification method
	 * 
	 * @param session - {@link NotificationSchedulerService.session} email session
	 * to send mail
	 * 
	 * @param e - custom NotificationException instance
	 * 
	 * @return mimeMessage - email message with all the required info like
	 * recipients, subject etc
	 */
	public MimeMessage transformEmailForException(JavaMailSender javaMailSender, Exception e, EmailParams emailParam)
			throws MessagingException {
		logger.debug("Entry into NotificationService.transformEmailForException");

		MimeMessage message = javaMailSender.createMimeMessage();
		if (StringUtils.isNotBlank(emailParam.getMailCc()) && emailParam.getMailCc() != null) {
			message.addRecipient(RecipientType.CC, new InternetAddress(emailParam.getMailCc()));
			logger.info("CC  {}", emailParam.getMailCc());

		}
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
			InternetAddress[] recipientAddress;
			recipientAddress = prepareRecipientsList(exceptionMailrecipientList);
			logger.info("exceptionMailSubject for recID {}", emailParam.getRecId());

			mimeMessageHelper.setSubject(exceptionMailSubject + " for " + emailParam.getRecId());
			mimeMessageHelper.setFrom(emailFrom);
			if (recipientAddress != null) {
				mimeMessageHelper.setTo(recipientAddress);
			}
			/*
			 * Template template = this.velocityEngine .getTemplate(FILE_TEMPLATE +
			 * emailParam.getNotificationType() + ".vm");
			 */

			Template template = Velocity.getTemplate(notificationType + Constants.EXTENSION);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("exceptionMessage", e.getMessage());
			velocityContext.put(INSERT_TIME, emailParam.getInsertDate());

			StringWriter exception = new StringWriter();
			e.printStackTrace(new PrintWriter(exception));
			velocityContext.put("exceptionDesc", exception.toString());
			StringWriter stringWriter = new StringWriter();
			template.merge(velocityContext, stringWriter);
			mimeMessageHelper.setText(stringWriter.toString(), true);
			logger.debug("Exit from NotificationService.transformEmailForException");

			return mimeMessageHelper.getMimeMessage();
		} catch (AddressException e1) {
			logger.error("Exception in generating mime type message {} ", e1);
		} catch (MessagingException me) {
			logger.error("MessagingException in generating mime type message {} ", me);
		}
		return null;
	}

	StringBuilder attachmentException = new StringBuilder();

	// Java code to illustrate reading a
	// CSV file line by line
	public StringBuilder readDataLineByLine(String fileName) {
		logger.debug("Entry into NotificationService.readDataLineByLine");

		StringBuilder email = new StringBuilder();
		attachmentException = new StringBuilder();
		String csvFile = fileName;
		String line = "";
		String cvsSplitBy = ",";
		int i = 0;
		String tdStr = "<td style=\"border: 1px solid black;\">";
		String tdClose = "</td>";
		email.append("<html><body>" + "<table style='border:2px solid black'>");
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {
				String[] statitics = line.split(cvsSplitBy);

				email.append("<tr style=\"border: 1px solid black;\">");
				email.append(tdStr);
				if (i == 0) {
					email.append(statitics[0] + tdClose);

				} else {
					email.append(i + tdClose);
				}

				email.append(tdStr);
				email.append(statitics[1] + tdClose);

				email.append(tdStr);
				email.append(statitics[2] + tdClose);

				email.append(tdStr);
				email.append(statitics[3] + tdClose);
				email.append(tdStr);
				email.append(statitics[4] + tdClose);
				email.append(tdStr);
				email.append(statitics[5] + tdClose);
				email.append(tdStr);
				email.append(statitics[6] + tdClose);
				email.append(tdStr);
				email.append(statitics[7] + tdClose);
				if (StringUtils.isNotBlank(statitics[7]) && statitics[7] != null
						&& !"null".equalsIgnoreCase(statitics[7])
						&& !"Link Of Exception File".equalsIgnoreCase(statitics[7])
						&& !"NA".equalsIgnoreCase(statitics[7])) {
					List<String> attachmentsplit = new ArrayList<>();
					if (statitics[7].contains(";") && statitics[7].indexOf(';') >= 1) {
						attachmentsplit = Arrays.asList(statitics[7].split(";"));
					} else {
						attachmentsplit.add(statitics[7]);
					}
					attachmentsplit.forEach(attachment -> {
						if (attachmentException.length() > 0) {
							attachmentException.append(",");
							attachmentException.append(attachment);
						} else {
							attachmentException.append(attachment);
						}
					});
				}

				email.append(tdStr);
				email.append(statitics[8] + tdClose);
				email.append("<tr>");
				i++;
			}

		} catch (IOException e) {
			logger.error("IOException while generating statisitcs", e);
		}

		email.append("</table></body></html>");
		return email;
	}

}
