package com.midea.isc.auth.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midea.isc.auth.server.interceptor.UserAuthRestInterceptor;
import com.midea.isc.common.util.MyObjectMapper;
import com.midea.isc.common.web.config.MyMappingJackson2HttpMessageConverter;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangxk7 on 2018/12/24.
 */
@Configuration("authWebConfig")
@Primary
@Slf4j
public class WebConfig implements WebMvcConfigurer {
	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ArrayList<String> commonPathPatterns = getExcludeCommonPathPatterns();
		log.info("添加用户验证拦截器");
		registry.addInterceptor(getUserAuthRestInterceptor()).addPathPatterns("/**")
				.excludePathPatterns(commonPathPatterns.toArray(new String[] {}));
	}

	@Bean
	UserAuthRestInterceptor getUserAuthRestInterceptor() {
		return new UserAuthRestInterceptor();
	}

	// getSetProfile可能是登录，所以需要放过
	private ArrayList<String> getExcludeCommonPathPatterns() {
		ArrayList<String> list = new ArrayList<>();
		String[] urls = { "/v2/api-docs", "/swagger-resources/**", "/cache/**", "/api/log/save", "/client/token",
				"/client/myClient", "/client/servicePubKey" };
		Collections.addAll(list, urls);
		return list;
	}

	/**
	 * 配置自定义的HttpMessageConverter 的Bean ，在Spring MVC
	 * 里注册HttpMessageConverter有两个方法： 1、configureMessageConverters ：重载会覆盖掉Spring
	 * MVC 默认注册的多个HttpMessageConverter 2、extendMessageConverters
	 * ：仅添加一个自定义的HttpMessageConverter ，不覆盖默认注册的HttpMessageConverter
	 * 在这里重写extendMessageConverters
	 */
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(converter());
	}

	@Bean
	public MyMappingJackson2HttpMessageConverter converter() {
		ObjectMapper mapper = MyObjectMapper.getMapper();

		return new MyMappingJackson2HttpMessageConverter(mapper);
	}
}
