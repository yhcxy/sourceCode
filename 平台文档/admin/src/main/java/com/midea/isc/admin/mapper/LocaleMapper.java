package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Locale;
import com.midea.isc.admin.param.LocaleParam;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface LocaleMapper extends BaseMapper<Locale, LocaleParam>{
	int countDuplicate(LocaleParam param);

	List<Locale> list(LocaleParam param);
}

