package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.ClientServiceBiz;
import com.midea.isc.admin.model.ClientService;
import com.midea.isc.admin.param.ClientServiceParam;

@RestController
@RequestMapping(value="/clientService")
public class ClientServiceController extends BasicController<ClientServiceBiz, ClientService, ClientServiceParam> {
	
}