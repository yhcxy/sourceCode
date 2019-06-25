package com.midea.isc.auth.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.client.interceptor.ServiceFeignInterceptor;
import com.midea.isc.auth.common.jwt.TokenManager;

@Configuration
@ComponentScan({ "com.midea.isc.auth.client", "com.midea.isc.auth.common.event" })
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig() {
        return new ServiceAuthConfig();
    }

    @Bean
    TokenManager getTokenManager() {
        return new TokenManager();
    }

    @Bean
    ServiceFeignInterceptor getFeignInterceptor() {
        return new ServiceFeignInterceptor();
    }
}
