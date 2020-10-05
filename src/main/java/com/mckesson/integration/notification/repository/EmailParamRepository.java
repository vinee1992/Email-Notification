package com.mckesson.integration.notification.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mckesson.integration.notification.model.EmailParams;

@Repository
public interface EmailParamRepository extends JpaRepository<EmailParams, Long> {

	public List<EmailParams> findByFlag(String flag);

	@Transactional
	@Modifying
	@Query("update EmailParams ep set ep.flag = :flag ,ep.notificationTriggerTime=:dateTime where ep.recId = :recId")
	public void updateEmailParamsFlagByRecId(@Param("flag") String flag, @Param("recId") Long recId,
			@Param("dateTime") Date triggerDate);

	@Transactional
	@Modifying
	@Query("update EmailParams ep set ep.subject = :subject, ep.mailFrom = :mailFrom, ep.mailTo = :mailTo  where ep.recId = :recId")
	public void updateEmailParamsMailToFromAndSubjectByRecId(@Param("subject") String subject,
			@Param("mailFrom") String mailFrom, @Param("mailTo") String mailTo, @Param("recId") Long recId);

	@Query("select recId from EmailParams where recId=:recId")
	public int fetchRecID(@Param("recId") long recId);

}
