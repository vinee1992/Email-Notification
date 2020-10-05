package com.mckesson.integration.notification.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification_metadata")
public class NotificationMetaData {

	@Id
	@Column(name = "notification_type")
	private String notificationType;

	@Column(name = "template_name")
	private String templateName;

	@Column(name = "recipient_list")
	private String recipientList;

	@Column(name = "subject")
	private String subject;

	@Column(name = "mail_from")
	private String mailFrom;

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getRecipientList() {
		return recipientList;
	}

	public void setRecipientList(String recipientList) {
		this.recipientList = recipientList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	@Override
	public String toString() {
		return "NotificationMetaData [notificationType=" + notificationType + ", templateName=" + templateName
				+ ", recipientList=" + recipientList + ", subject=" + subject + ", mailFrom=" + mailFrom + "]";
	}

}
