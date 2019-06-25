package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.ResourceRoleBiz;
import com.midea.isc.auth.server.model.ResourceRole;
import com.midea.isc.auth.server.param.ResourceRoleParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/resourceRole")
public class ResourceRoleController extends BasicController<ResourceRoleBiz, ResourceRole, ResourceRoleParam> {
	
}