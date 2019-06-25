package com.midea.isc.admin.controller;

import com.midea.isc.admin.biz.ResourceRoleBiz;
import com.midea.isc.admin.model.ResourceRole;
import com.midea.isc.admin.param.ResourceRoleParam;
import com.midea.isc.common.web.contoller.BasicController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/resourceRole")
public class ResourceRoleController extends BasicController<ResourceRoleBiz, ResourceRole, ResourceRoleParam> {
	
}