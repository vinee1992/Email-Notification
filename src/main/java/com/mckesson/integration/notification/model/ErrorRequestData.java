package com.mckesson.integration.notification.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ErrorRequestData class to set error code mail body for notification type
 * MDM0001
 * 
 * @author 703252734
 *
 */
public class ErrorRequestData {
	private String errorCodeName;
	private String groupName;
	private String errorName;
	private Map<String, List<String>> jobNameList;
	private Map<String, Set<String>> jobWithFileList;
	private String emailSubject;
	private String fileName;
	private String applicationName;
	
	public Map<String, Set<String>> getJobWithFileList() {
		return jobWithFileList;
	}

	public void setJobWithFileList(Map<String, Set<String>> jobWithFileList) {
		this.jobWithFileList = jobWithFileList;
	}

	public String getErrorCodeName() {
		return errorCodeName;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setErrorCodeName(String errorCodeName) {
		this.errorCodeName = errorCodeName;
	}

	public Map<String, List<String>> getJobNameList() {
		return jobNameList;
	}

	public void setJobNameList(Map<String, List<String>> jobNameList) {
		this.jobNameList = jobNameList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	@Override
	public String toString() {
		return "ErrorRequestData [errorCodeName=" + errorCodeName + ", groupName=" + groupName + ", errorName="
				+ errorName + ", jobNameList=" + jobNameList + ", jobWithFileList=" + jobWithFileList
				+ ", emailSubject=" + emailSubject + ", fileName=" + fileName + ", applicationName=" + applicationName
				+ "]";
	}

}
