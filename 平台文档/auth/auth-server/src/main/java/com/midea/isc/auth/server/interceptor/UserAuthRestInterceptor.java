package com.midea.isc.auth.server.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.midea.isc.auth.client.annotation.IgnoreUserToken;
import com.midea.isc.auth.common.constants.CommonConstants;
import com.midea.isc.auth.common.jwt.IJWTInfo;
import com.midea.isc.auth.common.jwt.JWTHelper;
import com.midea.isc.auth.common.jwt.TokenManager;
import com.midea.isc.auth.server.config.KeyConfig;
import com.midea.isc.common.model.Profile;
import com.midea.isc.common.sys.IscException;
import com.midea.isc.common.util.BaseContextHandler;
import com.midea.isc.common.web.sys.UserHelper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangxk7 on 2018/11/29.
 */
@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {

	private static final String webClient = "isc-gate-web";

	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Autowired
	private TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Profile profile = getProfile(request); // 优先获取Profile，可以获取不到，但是一定要先在这里处理
		if (!envTag.equalsIgnoreCase("dev")) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;// 这里报异常说明url匹配不到，springboot2.0修改为资源访问
			// 获取请求来源(需要适配共享的网关)
			String clienttoken = request.getHeader(CommonConstants.CLIENT_TOKEN_HEAD);
			IJWTInfo infoFromToken = getInfoFromToken(clienttoken);
			String uniqueName = infoFromToken.getUniqueName();
			// String uniqueName = "isc-gate-web"; // 与共享网关联调测试
			if (webClient.equalsIgnoreCase(uniqueName)) { // 通过gate-web过来的请求要获取用户信息，网关已经进行了用户登录认证(适配共享的网关，修改识别网关的方式)
				// 配置该注解，说明不进行用户拦截
				IgnoreUserToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserToken.class);
				if (annotation == null) {
					annotation = handlerMethod.getMethodAnnotation(IgnoreUserToken.class);
				}
				if (annotation != null) {
					return super.preHandle(request, response, handler);
				}

				// 来自网关的请求才有用户信息，来自其他服务的请求没有用户信息
				if (profile == null) {
					throw new IscException("ISC-920", "非DEV环境下的无用户登录访问");
				}
			}

			return super.preHandle(request, response, handler);
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	private Profile getProfile(HttpServletRequest request) throws IscException {
		Profile profile = null;
		try {
			String token = UserHelper.getUserToken(request, envTag);
			String app = request.getHeader(CommonConstants.APP_HEAD); // 非网关决定，由前端存在header中
			profile = tokenManager.getAndSetExpire(app, token); // 获取不到用户则抛异常
			BaseContextHandler.setProfile(profile);
			BaseContextHandler.setUsername(profile.get__userName());
			BaseContextHandler.setName(profile.get__fullName());
			BaseContextHandler.setUserID(profile.get__userId());
		} catch (Exception e) {
			profile = null;
			log.warn(e.getMessage());
		}

		return profile;
	}

	@Autowired
	private KeyConfig keyConfig;

	private IJWTInfo getInfoFromToken(String token) throws Exception {
		try {
			return JWTHelper.getInfoFromToken(token, keyConfig.getServicePubKey());
		} catch (ExpiredJwtException ex) {
			throw new IscException("ISC-900", "Client token expired!");
		} catch (SignatureException ex) {
			throw new IscException("ISC-901", "Client token signature error!");
		} catch (IllegalArgumentException ex) {
			throw new IscException("ISC-902", "Client token is null or empty!");
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		BaseContextHandler.remove();
		super.afterCompletion(request, response, handler, ex);
	}
}
