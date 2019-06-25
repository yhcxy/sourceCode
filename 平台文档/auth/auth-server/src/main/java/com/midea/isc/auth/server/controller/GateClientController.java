package com.midea.isc.auth.server.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.midea.isc.auth.server.biz.GateClientBiz;
import com.midea.isc.auth.server.model.GateClient;
import com.midea.isc.auth.server.param.GateClientParam;
import com.midea.isc.common.web.contoller.BasicController;

@RestController
@RequestMapping(value = "/gateClient")
public class GateClientController extends BasicController<GateClientBiz, GateClient, GateClientParam> {
    @RequestMapping(value = "/lockService", method = RequestMethod.POST)
    public boolean lockService(@RequestBody GateClientParam param) {
        return baseBiz.lockService(param);
    }

    @RequestMapping(value = "/pushAuthority", method = RequestMethod.POST)
    public void pushAuthority(@RequestBody GateClientParam param) {
        baseBiz.pushAuthority(param);
    }
}