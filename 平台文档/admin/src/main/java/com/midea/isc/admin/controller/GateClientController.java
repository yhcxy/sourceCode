package com.midea.isc.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.midea.isc.common.web.contoller.BasicController;
import com.midea.isc.admin.biz.GateClientBiz;
import com.midea.isc.admin.model.GateClient;
import com.midea.isc.admin.param.GateClientParam;
import com.midea.isc.admin.vo.AuthClientParamVo;
import com.midea.isc.admin.vo.GateClientParamVo;

@RestController
@RequestMapping(value = "/gateClient")
public class GateClientController extends BasicController<GateClientBiz, GateClient, GateClientParam> {
    @RequestMapping(value = "/discoveryService", method = RequestMethod.POST)
    public int discoveryService(@RequestBody GateClientParam param) {
        return baseBiz.discoveryService(param);
    }

    @RequestMapping(value = "/getAppClient", method = RequestMethod.POST)
    public List<GateClient> getAppClient(@RequestBody GateClientParamVo param) {
        return baseBiz.getAppClient(param);
    }

    @RequestMapping(value = "/totalAppClient", method = RequestMethod.POST)
    public int totalAppClient(@RequestBody GateClientParamVo param) {
        return baseBiz.totalAppClient(param);
    }

    @RequestMapping(value = "/initAuth", method = RequestMethod.POST)
    public AuthClientParamVo initAuth(@RequestBody GateClientParam param) {
        return baseBiz.initAuth(param);
    }

    @RequestMapping(value = "/updateAuth", method = RequestMethod.POST)
    public int updateAuth(@RequestBody AuthClientParamVo param) {
        return baseBiz.updateAuth(param);
    }
}