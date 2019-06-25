package com.midea.isc.auth.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxk7
 * @create 2018/11/28.
 */
@Configuration
@Data
public class KeyConfig {
    @Value("${client.rsa-secret}")
    private String serviceSecret;
    private byte[] servicePriKey;
    private byte[] servicePubKey;
}
