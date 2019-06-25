package com.midea.isc.common.constant;

import com.midea.isc.common.config.RedisConfig;

public class DateFormatConstants {
	public static final String DEFAULT_FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String COUNTRY_DATE_FORMAT_CACHE_KEY = "COUNTRY_DATE_FORMAT_CACHE";

	private DateFormatConstants() {
		throw new IllegalAccessError("DateFormatConstants class");
	}

	/**
	 * 根据国家获取时间格式化规则
	 * @param country
	 * @return
	 */
	public static String getCountryDateFormat(String country) {
		Object dateFmt = RedisConfig.getRedisTemplate().opsForHash().get(COUNTRY_DATE_FORMAT_CACHE_KEY,country);
		return dateFmt == null ? null : dateFmt.toString();
	}
}
