package com.mckesson.integration.notification.model;

import java.util.ArrayList;
import java.util.List;

public class BRTemplateNotificationParam {

	private Integer supplierId;
	private String supplierName;
	private List<String> attachments = new ArrayList<>();
	private Long wqId;
	

	public Integer getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}


	public String getSupplierName() {
		return supplierName;
	}


	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	public List<String> getAttachments() {
		return attachments;
	}


	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}


	public Long getWqId() {
		return wqId;
	}


	@Override
	public String toString() {
		return "BRTemplateNotificationParam [supplierId=" + supplierId + ", supplierName=" + supplierName
				+ ", attachments=" + attachments + ", wqId=" + wqId + "]";
	}


	public void setWqId(Long wqId) {
		this.wqId = wqId;
	}


}
