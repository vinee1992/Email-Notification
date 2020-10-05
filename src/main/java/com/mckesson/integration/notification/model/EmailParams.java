package com.mckesson.integration.notification.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_params")
public class EmailParams {

	@Id
	@Column(name = "rec_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recId;

	@Column(name = "mail_to")
	private String mailTo;

	@Column(name = "mail_from")
	private String mailFrom;
	
	@Column(name = "mail_subject")
	private String subject;
	
	@Column(name = "mail_cc")
	private String mailCc;

	@Column(name = "mail_bcc")
	private String mailBcc;

	@Column(name = "mail_body_params")
	private String mailBodyParams;
	
	@Column(name = "notification_type")
	private String notificationType;
	
	



	@Column(name = "flag")
	private String flag;
	
	@Column(name = "insert_time")
	private Date insertDate;
	
	
	@Column(name = "Notification_Trigger_Time")
	private Date notificationTriggerTime;

	
	@Column(name = "Ticket_No")
	private String ticketNo;
	
	@Column(name = "Reply_To")
	private String replyTo;

	@Column(name = "Response_Time")
	private Date responseTime;
	
	public Date getNotificationTriggerTime() {
		return notificationTriggerTime;
	}

	public void setNotificationTriggerTime(Date notificationTriggerTime) {
		this.notificationTriggerTime = notificationTriggerTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public EmailParams() {
		//No op constructor
	}

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public String getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public String getMailBodyParams() {
		return mailBodyParams;
	}

	public void setMailBodyParams(String mailBodyParams) {
		this.mailBodyParams = mailBodyParams;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	@Override
	public String toString() {
		StringBuilder lBuilder = new StringBuilder();
		lBuilder.append(" Mail To:- ").append(getMailTo());
		lBuilder.append(" Mail From:- ").append(getMailFrom());
		lBuilder.append(" Mail Cc:- ").append(getMailCc());
		lBuilder.append(" Mail Bcc:- ").append(getMailBcc());
		lBuilder.append(" Mail Subject:- ").append(getSubject());
		lBuilder.append(" Mail Send Date:- ").append(getInsertDate());
		lBuilder.append(" Mail Body Params:- ").append(getMailBodyParams());
		lBuilder.append(" Mail Notification Type:- ").append(getNotificationType());
		return lBuilder.toString();
	}

}
