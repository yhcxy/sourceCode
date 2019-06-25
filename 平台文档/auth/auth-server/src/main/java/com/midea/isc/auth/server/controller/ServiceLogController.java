package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.auth.server.biz.ServiceLogBiz;
import com.midea.isc.auth.server.model.ServiceLog;
import com.midea.isc.auth.server.param.ServiceLogParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/serviceLog")
public class ServiceLogController extends BasicController<ServiceLogBiz, ServiceLog, ServiceLogParam> {
	
}