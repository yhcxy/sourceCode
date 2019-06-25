package com.midea.isc.attachment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midea.isc.common.util.MyObjectMapper;
import com.midea.isc.common.web.config.MyMappingJackson2HttpMessageConverter;
import com.midea.isc.common.web.interceptor.ServiceAuthRestInterceptor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangxk7 on 2018/12/3.
 */
@Configuration("attachmentWebConfig")
@Primary
@Data
public class WebConfig implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

	@Value("${spring.cloud.config.profile}")
	private String envTag;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ArrayList<String> commonPathPatterns = getExcludeCommonPathPatterns();
		log.info("添加服务验证拦截器");
		registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/**")
				.excludePathPatterns(commonPathPatterns.toArray(new String[] {}));
	}

	@Bean
	ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
		return new ServiceAuthRestInterceptor();
	}

	private ArrayList<String> getExcludeCommonPathPatterns() {
		ArrayList<String> list = new ArrayList<>();
		String[] urls = { "/v2/api-docs", "/swagger-resources/**", "/cache/**", "/api/log/save" };
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

