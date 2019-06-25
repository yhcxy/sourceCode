package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.ServiceLogBiz;
import com.midea.isc.admin.model.ServiceLog;
import com.midea.isc.admin.param.ServiceLogParam;

@RestController
@RequestMapping(value="/serviceLog")
public class ServiceLogController extends BasicController<ServiceLogBiz, ServiceLog, ServiceLogParam> {
	
}