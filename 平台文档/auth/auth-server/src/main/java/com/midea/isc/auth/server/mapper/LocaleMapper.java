package com.midea.isc.auth.server.mapper;

import java.util.List;

import com.midea.isc.auth.server.model.Locale;
import com.midea.isc.auth.server.param.LocaleParam;
import com.midea.isc.auth.server.vo.LocaleVo;
import com.midea.isc.common.mapper.BaseMapper;

public interface LocaleMapper extends BaseMapper<Locale, LocaleParam> {
	/**
	 * 获取系统语言
	 * @param applicationName
	 * @return
	 */
	List<String> languages(String applicationName);

	/**
	 * 获取系统语言
	 * @return
	 */
	List<LocaleVo> listLanguage(String application);
}
