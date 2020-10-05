package com.mckesson.integration.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mckesson.integration.notification.model.BrNotificationDetail;

public interface BrDetailsRepository extends MongoRepository<BrNotificationDetail, String> {

	@Query("{emailParamsId : ?0}")
	public List<BrNotificationDetail> findByRecId(int emailParamsId);

}
