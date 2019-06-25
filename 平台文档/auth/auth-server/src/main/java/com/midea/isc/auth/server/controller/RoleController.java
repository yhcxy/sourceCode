package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.RoleBiz;
import com.midea.isc.auth.server.model.Role;
import com.midea.isc.auth.server.param.RoleParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/role")
public class RoleController extends BasicController<RoleBiz, Role, RoleParam> {
	
}