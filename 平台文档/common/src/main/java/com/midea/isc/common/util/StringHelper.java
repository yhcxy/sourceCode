package com.midea.isc.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringHelper {
	public static String getObjectValue(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static int toIntByDefault(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			result = 0;
			log.warn(str + " cant change to int");
		}

		return result;
	}
}
