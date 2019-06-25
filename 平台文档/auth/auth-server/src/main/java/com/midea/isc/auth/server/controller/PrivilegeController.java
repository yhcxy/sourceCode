package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.PrivilegeBiz;
import com.midea.isc.auth.server.model.Privilege;
import com.midea.isc.auth.server.param.PrivilegeParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/privilege")
public class PrivilegeController extends BasicController<PrivilegeBiz, Privilege, PrivilegeParam> {
	
}