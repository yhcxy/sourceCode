package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.LoginLogBiz;
import com.midea.isc.admin.model.LoginLog;
import com.midea.isc.admin.param.LoginLogParam;

@RestController
@RequestMapping(value="/loginLog")
public class LoginLogController extends BasicController<LoginLogBiz, LoginLog, LoginLogParam> {
	
}