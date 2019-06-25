package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.ClientServiceBiz;
import com.midea.isc.auth.server.model.ClientService;
import com.midea.isc.auth.server.param.ClientServiceParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value="/clientService")
public class ClientServiceController extends BasicController<ClientServiceBiz, ClientService, ClientServiceParam> {
	
}