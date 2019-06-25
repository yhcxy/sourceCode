package com.midea.isc.auth.client.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by wangxk7 on 2018/11/29.
 */

public class ServiceAuthConfig {
	private byte[] pubKeyByte;
	
	@Value("${auth.client.secret}")
	private String clientSecret;
	@Value("${spring.application.name}")
	private String applicationName;

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public byte[] getPubKeyByte() {
		return pubKeyByte;
	}

	public void setPubKeyByte(byte[] pubKeyByte) {
		this.pubKeyByte = pubKeyByte;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
