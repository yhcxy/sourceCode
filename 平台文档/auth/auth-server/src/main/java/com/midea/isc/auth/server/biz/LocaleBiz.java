package com.midea.isc.auth.server.biz;

import java.util.List;

import com.midea.isc.auth.server.vo.LocaleVo;
import org.springframework.stereotype.Service;
import com.midea.isc.auth.server.model.Locale;
import com.midea.isc.auth.server.param.LocaleParam;
import com.midea.isc.auth.server.mapper.LocaleMapper;
import com.midea.isc.common.biz.BaseBiz;

@Service
public class LocaleBiz extends BaseBiz<LocaleMapper, Locale, LocaleParam> {
	/**
	 *	获取系统语言
	 * @return
	 */
	public List<String> languages(String app) {
		return mapper.languages(app);
	}

	/**
	 *	获取系统语言
	 * @param param
	 * @return
	 */
	public List<LocaleVo> listLanguage(String application) {
		return mapper.listLanguage(application);
	}
}
