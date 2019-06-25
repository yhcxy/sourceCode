package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.FtpLogDetailBiz;
import com.midea.isc.admin.model.FtpLogDetail;
import com.midea.isc.admin.param.FtpLogDetailParam;

@RestController
@RequestMapping(value="/ftpLogDetail")
public class FtpLogDetailController extends BasicController<FtpLogDetailBiz, FtpLogDetail, FtpLogDetailParam> {
	
}