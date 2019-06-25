package com.midea.isc.auth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.common.jwt.RsaKeyHelper;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;

/**
 * @author wangxk
 * @create 2018/11/28.
 */
@Configuration
@Slf4j
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private KeyConfig keyConfig;
    @Override
    public void run(String... args) throws Exception {
        Map<String, byte[]> keyMap = RsaKeyHelper.generateKey(keyConfig.getServiceSecret());
        keyConfig.setServicePriKey(keyMap.get("pri"));
        keyConfig.setServicePubKey(keyMap.get("pub"));
        log.warn("priKey"+Arrays.toString(keyMap.get("pri")));
        log.warn("pulKey"+Arrays.toString(keyMap.get("pub")));
    }
}

