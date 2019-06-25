package com.midea.isc.auth.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangxk7 on 2018/11/28.
 */
@Configuration
public class ClientConfig {
    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientId() {
        return clientId;
    }


}
