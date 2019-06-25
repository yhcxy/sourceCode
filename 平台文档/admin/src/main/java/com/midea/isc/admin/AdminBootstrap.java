package com.midea.isc.admin;

import com.midea.isc.auth.client.EnableAuthClient;
import com.midea.isc.common.config.BeanConfig;
import com.midea.isc.common.config.ContextConfig;
import com.midea.isc.common.config.RedisConfig;
import com.midea.isc.common.web.config.ServiceLogConfig;
import com.midea.isc.common.web.config.SwaggerConfig;
import com.midea.isc.common.web.config.WebBeanConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * springboot入口类,此类需要在所有用到的package上层 exclude =
 * {DataSourceAutoConfiguration.class}
 * 禁用springboot默认加载的application.properties单数据源配置
 */
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableFeignClients({ "com.midea.isc.auth.client.feign", "com.midea.isc.admin.feign" })
@EnableScheduling
@EnableTransactionManagement
@EnableAuthClient
@Import({ RedisConfig.class, BeanConfig.class, WebBeanConfig.class, ServiceLogConfig.class, SwaggerConfig.class,
        ContextConfig.class })
@RemoteApplicationEventScan(basePackages = { "com.midea.isc.auth.common.event" })
@EnableCircuitBreaker
public class AdminBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AdminBootstrap.class, args);
    }
}
