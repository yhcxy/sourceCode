package com.midea.isc.auth.client.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.midea.isc.auth.client.config.ServiceAuthConfig;
import com.midea.isc.auth.client.feign.ServiceAuthFeign;
import com.midea.isc.auth.common.event.AuthRemoteEvent;
import com.midea.isc.auth.common.jwt.IJWTInfo;
import com.midea.isc.auth.common.jwt.JWTHelper;
import com.midea.isc.auth.common.vo.ServiceAuthority;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.sys.IscException;

import java.util.ArrayList;

/**
 * Created by wangxk7 on 2018/11/29.
 */
@Configuration
@Slf4j
@EnableScheduling
public class ServiceAuthUtil implements ApplicationListener<AuthRemoteEvent> {
    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    private ServiceAuthority serviceAuth = new ServiceAuthority(true, new ArrayList<String>());

    private String clientToken;

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        try {
            log.debug(token + ":" + serviceAuthConfig.getPubKeyByte());
            return JWTHelper.getInfoFromToken(token, serviceAuthConfig.getPubKeyByte());
        }
        catch (ExpiredJwtException ex) {
            throw new IscException("ISC-900", "Client token expired!");
        }
        catch (SignatureException ex) {
            throw new IscException("ISC-901", "Client token signature error!");
        }
        catch (IllegalArgumentException ex) {
            throw new IscException("ISC-902", "Client token is null or empty!");
        }
    }

    // 每隔一小时刷新一次，因为JWT的过期时间为1小时
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void refreshClientToken() {
        log.info("refresh client token.....");
        BasicResult<String> resp = serviceAuthFeign.getAccessToken(serviceAuthConfig.getApplicationName(),
                serviceAuthConfig.getClientSecret());
        if (resp.getResultCode().equals("ISC-000")) {
            this.clientToken = resp.getData();
        }
        else {
            log.error(resp.getResultCode() + ":" + resp.getResultMsg());
        }
    }

    public String getClientToken() {
        if (this.clientToken == null) {
            this.refreshClientToken();
        }
        return clientToken;
    }

    @Override
    public void onApplicationEvent(AuthRemoteEvent authRemoteEvent) {
        serviceAuth.setAllowedClient(authRemoteEvent.getAllowedClient());
        serviceAuth.setNeedAuth(authRemoteEvent.isNeedAuth());
        log.info("service authority listener,needauth:{},allowedclient:{}", authRemoteEvent.isNeedAuth(),
                String.join(",", authRemoteEvent.getAllowedClient()));
    }

    public ServiceAuthority getServiceAuth() {
        return serviceAuth;
    }

    public void setServiceAuth(ServiceAuthority serviceAuth) {
        this.serviceAuth = serviceAuth;
    }
}
