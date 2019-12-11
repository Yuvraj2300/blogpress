package com.lrn.blgprss.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogpressUtil {
	
	private static Logger logger =  LoggerFactory.getLogger(BlogpressUtil.class);
	
	private static String elasticDateFormat = "MM-dd-yyyy'T'HH:mm:ss";
	private static String displayDateFormat = "MMMMM dd yyyy h:mm:ss a";
	private static final String ALPHA_NUMERIC_STRING = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
	private static int randomNoLength = 10;

	private BlogpressUtil() {

	}
	
	public static String RandomNumber(Date currentDate) {
		int count = randomNoLength;
		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		Date date = currentDate;
		if (date == null) {
			currentDate = new Date();

		}
		return builder.append(currentDate.getTime()).toString();
	}
}
