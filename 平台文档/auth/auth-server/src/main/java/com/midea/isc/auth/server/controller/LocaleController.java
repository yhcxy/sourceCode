package com.midea.isc.auth.server.controller;

import com.midea.isc.auth.client.annotation.IgnoreUserToken;
//import com.midea.isc.auth.client.annotation.IgnoreClientToken;
import com.midea.isc.auth.server.biz.LocaleBiz;
import com.midea.isc.auth.server.model.Locale;
import com.midea.isc.auth.server.param.LocaleParam;
import com.midea.isc.auth.server.vo.LocaleVo;
import com.midea.isc.common.web.contoller.BasicController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/locale")
public class LocaleController extends BasicController<LocaleBiz, Locale, LocaleParam> {
	@Autowired
	private LocaleBiz localeBiz;

	// @IgnoreClientToken
	@IgnoreUserToken
	@GetMapping(value = "/list")
	public List<LocaleVo> list(String application) throws Exception {
		return localeBiz.listLanguage(application);
	}
}