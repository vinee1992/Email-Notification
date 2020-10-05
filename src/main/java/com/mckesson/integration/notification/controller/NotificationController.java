package com.mckesson.integration.notification.controller;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mckesson.integration.notification.model.EmailParams;
import com.mckesson.integration.notification.model.EmailStatus;
import com.mckesson.integration.notification.repository.EmailParamRepository;

@RestController
@RequestMapping(path = "/email")
public class NotificationController {

	@Autowired
	EmailParamRepository emailParamRepository;

	private Logger logger = LoggerFactory.getLogger(NotificationController.class);

	
	public ResponseEntity<String> sendNotification(EmailParams emailParams) {

		try {
			emailParams.setFlag(EmailStatus.NEW.name());
			emailParams.setInsertDate(Calendar.getInstance().getTime());
			emailParamRepository.save(emailParams);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Request successfully added to queue, will be processed shortly",
				HttpStatus.OK);
	}

}