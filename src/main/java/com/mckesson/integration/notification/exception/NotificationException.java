package com.mckesson.integration.notification.exception;

public class NotificationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NotificationException(String errorMsg, Throwable err) {
		super(errorMsg, err);
	}

}
