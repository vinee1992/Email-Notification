package com.mckesson.integration.notification.model;

import java.util.List;

public class JobNameDetails {
	private String jobName;
	private List<String> pathList;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<String> getPathList() {
		return pathList;
	}

	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}

	@Override
	public String toString() {
		return "JobNameDetails [jobName=" + jobName + ", pathList=" + pathList + "]";
	}

}