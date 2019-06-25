package com.midea.isc.auth.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import com.midea.isc.auth.client.jwt.ServiceAuthUtil;
import com.midea.isc.auth.common.constants.CommonConstants;

/**
 *
 * @author wangxk7
 * @date 2018/11/29
 */
public class ServiceFeignInterceptor implements RequestInterceptor {

	@Autowired
	private ServiceAuthUtil serviceAuthUtil;

	public ServiceFeignInterceptor() {
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		if ("/client/token".equals(requestTemplate.url())) // 对于连接auth-server获取token的服务忽略，否则会造成死循环
			return;
		requestTemplate.header(CommonConstants.CLIENT_TOKEN_HEAD, serviceAuthUtil.getClientToken());
	}

	public void setServiceAuthUtil(ServiceAuthUtil serviceAuthUtil) {
		this.serviceAuthUtil = serviceAuthUtil;
	}
}
