package com.midea.isc.auth.client.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.client.feign.ServiceAuthFeign;
import com.midea.isc.auth.client.jwt.ServiceAuthUtil;
import com.midea.isc.auth.common.vo.ServiceAuthority;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.util.CommUtil;

/**
 * 监听完成时触发
 *
 * @author ace
 * @create 2017/11/29.
 */
@Configuration
@Slf4j
public class AuthClientRunner implements CommandLineRunner {
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    @Autowired
    private ApplicationContext appContext;

    @Value("${spring.cloud.config.profile}")
    private String envTag;

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化加载pubKey");
        BasicResult<Byte[]> resp = serviceAuthFeign.getServicePublicKey(serviceAuthConfig.getApplicationName(),
                serviceAuthConfig.getClientSecret());
        if (resp.getResultCode().equals("ISC-000")) {
            log.info("加载客户pubKey succeed!");
            this.serviceAuthConfig.setPubKeyByte(CommUtil.toPrimitivesByte(resp.getData()));
        }
        else {
            log.error("加载pubKey失败{}", resp.getResultCode());
            if (!envTag.equalsIgnoreCase("dev")) {
                log.error("关闭应用");
                System.exit(SpringApplication.exit(appContext, () -> 200));
            }
        }

        log.info("初始化加载权限");
        BasicResult<ServiceAuthority> respAuth = serviceAuthFeign
                .serviceAuthority(serviceAuthConfig.getApplicationName(), serviceAuthConfig.getClientSecret());
        if (respAuth.getResultCode().equals("ISC-000")) {
            this.serviceAuthUtil.setServiceAuth(respAuth.getData());
            log.info("service authority listener,needauth:{},allowedclient:{}", respAuth.getData().isNeedAuth(),
                    String.join(",", respAuth.getData().getAllowedClient()));
        }
        else {
            log.warn("初始化加载权限失败");
        }
    }
}
