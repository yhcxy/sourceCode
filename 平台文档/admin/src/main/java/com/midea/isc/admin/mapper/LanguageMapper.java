package com.midea.isc.admin.mapper;

import com.midea.isc.admin.model.Language;
import com.midea.isc.admin.param.LanguageParam;
import com.midea.isc.common.mapper.BaseMapper;

import java.util.List;

public interface LanguageMapper extends BaseMapper<Language, LanguageParam> {
	List<Language> list(String application);

	int updateTest(String attribute1);
}
