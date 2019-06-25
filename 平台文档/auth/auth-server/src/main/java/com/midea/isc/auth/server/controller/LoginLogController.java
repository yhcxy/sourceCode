package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.auth.server.biz.LoginLogBiz;
import com.midea.isc.auth.server.model.LoginLog;
import com.midea.isc.auth.server.param.LoginLogParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/loginLog")
public class LoginLogController extends BasicController<LoginLogBiz, LoginLog, LoginLogParam> {
	
}