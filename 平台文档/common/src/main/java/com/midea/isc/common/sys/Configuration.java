package com.midea.isc.common.sys;

import org.springframework.beans.factory.annotation.Value;

public class Configuration {
	@Value("${spring.jackson.date-format}")
	public static String DateFormat;
	@Value("${spring.jackson.datetime-format}")
	public static String DatetimeFormat;
}
