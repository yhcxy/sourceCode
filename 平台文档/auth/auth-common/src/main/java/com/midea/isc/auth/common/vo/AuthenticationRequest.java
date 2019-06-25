package com.midea.isc.auth.common.vo;

import java.io.Serializable;

/**
 * @author WANGXK7
 *
 */
public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = 6816875382638975293L;

	private String app;
	private String token;
	private String username;
	private String password;
	private String language;
	private String ipAddress;

	public AuthenticationRequest(String app,String token, String username, String password, String language, String ipAddress) {
		this.app = app;
		this.token = token;
		this.username = username;
		this.password = password;
		this.language = language;
		this.ipAddress = ipAddress;
	}

	public AuthenticationRequest() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
