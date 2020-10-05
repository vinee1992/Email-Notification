package com.mckesson.integration.notification.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("BR_Notification_Details")
public class BrNotificationDetail {

	@Field("Email_Params_ID")
	private int emailParamsId;

	@Field("WQ_ID")
	private Long wqId;

	@Field("Exception_Type")
	private String exceptionType;

	@Field("Sub_Type")
	private String subType;

	@Field("Rule_Name")
	private String ruleName;

	@Field("Record_Sequence")
	private String recordSequence;

	@Field("Category")
	private String category;

	@Field("Sub_Category")
	private String subCategory;

	@Field("Notification_Count")
	private String notificationCount;

	@Field("Additional_Info")
	private String additionalInfo;

	



	public Long getWqId() {
		return wqId;
	}

	public void setWqId(Long wqId) {
		this.wqId = wqId;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRecordSequence() {
		return recordSequence;
	}

	public void setRecordSequence(String recordSequence) {
		this.recordSequence = recordSequence;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getNotificationCount() {
		return notificationCount;
	}

	public void setNotificationCount(String notificationCount) {
		this.notificationCount = notificationCount;
	}

	public int getEmailParamsId() {
		return emailParamsId;
	}

	public void setEmailParamsId(int emailParamsId) {
		this.emailParamsId = emailParamsId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
