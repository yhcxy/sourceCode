package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.LanguageBiz;
import com.midea.isc.admin.model.Language;
import com.midea.isc.admin.param.LanguageParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/language")
public class LanguageController extends BasicController<LanguageBiz, Language, LanguageParam> {
	@RequestMapping("list")
	public List<Language> list(String application) {
		return baseBiz.list(application);
	}

	@RequestMapping("testTn")
	public int testTn(String attribute1) {
		try {
			int test1 = baseBiz.updateTest1(attribute1);
		} catch (Exception ex) {

		}
		try {
			int test2 = baseBiz.updateTest2("55555");
		} catch (Exception ex) {

		}

		return 0;
	}
}