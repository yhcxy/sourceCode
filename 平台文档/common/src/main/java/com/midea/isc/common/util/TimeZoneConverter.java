package com.midea.isc.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneConverter {
	public static Date transferTimeZone(Date sourceDate, String sourceTimeZone,
			String targetTimeZone) {
		TimeZone source = TimeZone.getTimeZone(sourceTimeZone);
		TimeZone target = TimeZone.getTimeZone(targetTimeZone);
		Long targetTime = sourceDate.getTime() - source.getRawOffset()
				+ target.getRawOffset();
		return new Date(targetTime);
	}

	public static Date autoTransferTimeZone(SimpleDateFormat sdf) {
		Calendar cd = Calendar.getInstance();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
		return cd.getTime();
	}
}
