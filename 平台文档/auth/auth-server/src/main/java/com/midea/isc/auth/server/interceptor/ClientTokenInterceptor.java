package com.midea.isc.auth.server.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.midea.isc.auth.common.constants.CommonConstants;
import com.midea.isc.auth.server.biz.GateClientBiz;
import com.midea.isc.auth.server.config.ClientConfig;

/**
 * Created by wangxk7 on 2018/11/28.
 */
public class ClientTokenInterceptor implements RequestInterceptor {
	@Autowired
	private ClientConfig clientConfig;
	@Autowired
	private GateClientBiz clientService;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		try {
			requestTemplate.header(CommonConstants.CLIENT_TOKEN_HEAD,
					clientService.apply(clientConfig.getClientId(), clientConfig.getClientSecret()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}