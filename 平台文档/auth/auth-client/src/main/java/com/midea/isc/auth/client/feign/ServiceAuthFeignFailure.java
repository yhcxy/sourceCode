package com.midea.isc.auth.client.feign;

import org.springframework.stereotype.Component;

import com.midea.isc.auth.common.vo.AuthenticationRequest;
import com.midea.isc.auth.common.vo.ClientLog;
import com.midea.isc.auth.common.vo.ServiceAuthority;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.model.Profile;

@Component
public class ServiceAuthFeignFailure implements ServiceAuthFeign {
    private String serviceName = "isc-auth";

    @Override
    public BasicResult<ServiceAuthority> serviceAuthority(String serviceId, String secret){
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }

    @Override
    public BasicResult<String> getAccessToken(String clientId, String secret) {
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }

    @Override
    public BasicResult<Byte[]> getServicePublicKey(String clientId, String secret) {
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }

    @Override
    public BasicResult<Profile> ssoLogin(AuthenticationRequest authenticationRequest) {
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }

    @Override
    public BasicResult<?> serviceLog(ClientLog clientLog) {
        return new BasicResult<>("ISC-930", serviceName); // 断路器
    }
}
