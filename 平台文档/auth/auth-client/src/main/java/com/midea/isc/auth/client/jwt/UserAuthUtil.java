package com.midea.isc.auth.client.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.client.feign.ServiceAuthFeign;
import com.midea.isc.auth.common.jwt.TokenManager;
import com.midea.isc.auth.common.vo.AuthenticationRequest;
import com.midea.isc.common.model.BasicResult;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ace on 2017/9/15.
 */
@Slf4j
@Configuration
public class UserAuthUtil {

	@Autowired
	private ServiceAuthFeign serviceAuthFeign;

	@Autowired
	private TokenManager tokenManager;

	public Profile getProfileFromSSOToken(String app, String token, String userName, String language, String ipAddress) throws Exception {
		Profile profile;
		if (!tokenManager.containsKey(app, token)) {
			log.info("SSO login by {} with token:{}", app, token);
			// SSO环境从4A获取登录用户名
			AuthenticationRequest authenticationRequest = new AuthenticationRequest(app, token, userName, "", language,
					ipAddress);
			BasicResult<Profile> authResult = serviceAuthFeign.ssoLogin(authenticationRequest);
			if (authResult.getResultCode().equals("ISC-000")) {
				profile = authResult.getData();
			} else {
				throw new IscException(authResult.getResultCode(), authResult.getResultMsg());
			}
		} else {
			profile = tokenManager.getAndSetExpire(app, token);
		}
		return profile;
	}

	public Profile getAndUpdateProfile(String app, String token) throws IscException {
		Profile profile = tokenManager.getAndSetExpire(app, token);
		return profile;
	}

	public Profile getProfileFromToken(String app, String token) throws IscException {
		Profile profile = tokenManager.get(app, token);
		return profile;
	}
}
