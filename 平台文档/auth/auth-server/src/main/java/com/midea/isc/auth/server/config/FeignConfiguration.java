package com.midea.isc.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.server.interceptor.ClientTokenInterceptor;

/**
 * Created by wangxk7 on 2018/11/28.
 */
@Configuration
public class FeignConfiguration {
    @Bean
    ClientTokenInterceptor getClientTokenInterceptor(){
        return new ClientTokenInterceptor();
    }
}
