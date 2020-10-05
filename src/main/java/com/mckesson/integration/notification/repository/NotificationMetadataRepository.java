package com.mckesson.integration.notification.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mckesson.integration.notification.model.NotificationMetaData;

@Repository
public interface NotificationMetadataRepository extends JpaRepository<NotificationMetaData, String> {

	public NotificationMetaData findBynotificationType(String notificationType);
	
}
