package com.mckesson.integration.notification.model;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Document_Upload")
public class DocumentUpload {

	@Id
	private String id;

	@Field("File_Name")
	private String fileName;

	@Field("File_Type")
	private String fileType;

	@Field("File_Path")
	private String filePath;

	@Field("Description")
	private String fileDesc;

	@Field("WQ_ID")
	private Long wqId;

	@Field("Uploaded_Date")
	private Date creationDate;

	@Field("Uploaded_By")
	private String createdBy;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public Long getWqId() {
		return wqId;
	}

	public void setWqId(Long wqId) {
		this.wqId = wqId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
