package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.AssigmentBiz;
import com.midea.isc.auth.server.model.Assigment;
import com.midea.isc.auth.server.param.AssigmentParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/assigment")
public class AssigmentController extends BasicController<AssigmentBiz, Assigment, AssigmentParam> {
	
}