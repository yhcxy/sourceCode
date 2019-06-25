package com.midea.isc.auth.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.midea.isc.auth.common.jwt.TokenManager;
import com.midea.isc.auth.server.sys.LanguageHelper;

@Configuration
public class AutoConfiguration {
	@Bean
	TokenManager getTokenManager() {
		return new TokenManager();
	}
	
	@Bean
	LanguageHelper getLanguageHelper() {
		return new LanguageHelper();
	}
}
