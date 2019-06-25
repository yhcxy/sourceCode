package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.OperationBiz;
import com.midea.isc.admin.model.Operation;
import com.midea.isc.admin.param.OperationParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/operation")
public class OperationController extends BasicController<OperationBiz, Operation, OperationParam> {
	
}