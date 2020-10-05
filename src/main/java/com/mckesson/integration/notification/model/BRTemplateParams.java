package com.mckesson.integration.notification.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;;

public class BRTemplateParams {

	private Integer supplierId;
	private String supplierName;
	private LocalDate expiryDate;
	private List<Number> contractIds;
	private String filePath;
	private List<String> attachments = new ArrayList<>();
	private Long wqId;
	private Integer caseCount; 
	

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


	public LocalDate getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}


	public List<Number> getContractIds() {
		return contractIds;
	}


	public void setContractIds(List<Number> contractIds) {
		this.contractIds = contractIds;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
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


	public void setWqId(Long wqId) {
		this.wqId = wqId;
	}


	public Integer getCaseCount() {
		return caseCount;
	}


	public void setCaseCount(Integer caseCount) {
		this.caseCount = caseCount;
	}


	@Override
	public String toString() {
		return "BRTemplateParams [supplierId=" + supplierId + ", supplierName=" + supplierName + ", contractIds="
				+ contractIds + ", expiryDate=" + expiryDate + ", attachments=" + attachments + "]";
	}

}
