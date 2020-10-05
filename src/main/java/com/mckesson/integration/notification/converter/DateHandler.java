package com.mckesson.integration.notification.converter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateHandler extends StdDeserializer<Date> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DateHandler.class);

	public DateHandler() {
		this(null);
	}

	public DateHandler(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException

	{
		logger.debug("Entry into DateHandler.deserialize ..");

		String date = jsonparser.getText();
		logger.info("date {}", date);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			logger.debug("Exit from DateHandler.deserialize ..");
			return sdf.parse(date);

		} catch (ParseException e) {
			logger.error("Exception while deserializing date ... ", e);
		}
		return null;
	}
}
