package com.midea.isc.auth.server;

import com.midea.isc.common.config.BeanConfig;
import com.midea.isc.common.config.ContextConfig;
import com.midea.isc.common.config.RedisConfig;
import com.midea.isc.common.web.config.SwaggerConfig;
import com.midea.isc.common.web.config.WebBeanConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * springboot入口类,此类需要在所有用到的package上层 exclude =
 * {DataSourceAutoConfiguration.class}
 * 禁用springboot默认加载的application.properties单数据源配置
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableFeignClients
@EnableScheduling
@RemoteApplicationEventScan(basePackages = { "com.midea.isc.auth.common.event", "com.midea.isc.common.event" })
@Import({ RedisConfig.class, BeanConfig.class, WebBeanConfig.class, SwaggerConfig.class, ContextConfig.class })
@EnableCircuitBreaker
public class AuthServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerBootstrap.class, args);
    }
}
