package com.midea.isc.auth.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.midea.isc.auth.common.vo.AuthenticationRequest;
import com.midea.isc.auth.common.vo.ClientLog;
import com.midea.isc.auth.common.vo.ServiceAuthority;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.model.Profile;

/**
 * Created by wangxk7 on 2018/11/30.
 */
@FeignClient(value = "${auth.serviceId}", configuration = {}, fallback = ServiceAuthFeignFailure.class)
public interface ServiceAuthFeign {
    @RequestMapping(value = "/client/serviceAuthority")
    public BasicResult<ServiceAuthority> serviceAuthority(@RequestParam("serviceId") String serviceId,
            @RequestParam("secret") String secret);

    @RequestMapping(value = "/client/token", method = RequestMethod.POST)
    public BasicResult<String> getAccessToken(@RequestParam("clientId") String clientId,
            @RequestParam("secret") String secret);

    @RequestMapping(value = "/client/servicePubKey", method = RequestMethod.POST)
    public BasicResult<Byte[]> getServicePublicKey(@RequestParam("clientId") String clientId,
            @RequestParam("secret") String secret);

    @RequestMapping(value = "/login/ssoLogin", method = RequestMethod.POST)
    public BasicResult<Profile> ssoLogin(@RequestBody AuthenticationRequest authenticationRequest);

    @RequestMapping(value = "/client/serviceLog", method = RequestMethod.POST)
    public BasicResult<?> serviceLog(ClientLog clientLog);
}