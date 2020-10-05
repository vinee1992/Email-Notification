package com.mckesson.integration.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mckesson.integration.notification.model.DocumentUpload;

public interface DocumentUploadRepository extends MongoRepository<DocumentUpload, Object> {

}
