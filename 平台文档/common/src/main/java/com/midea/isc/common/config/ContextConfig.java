package com.midea.isc.common.config;

import com.midea.isc.common.sys.ApplicationContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
