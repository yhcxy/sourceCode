package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.CurrencyFormatBiz;
import com.midea.isc.admin.model.CurrencyFormat;
import com.midea.isc.admin.param.CurrencyFormatParam;

@RestController
@RequestMapping(value="/currencyFormat")
public class CurrencyFormatController extends BasicController<CurrencyFormatBiz, CurrencyFormat, CurrencyFormatParam> {
	
}