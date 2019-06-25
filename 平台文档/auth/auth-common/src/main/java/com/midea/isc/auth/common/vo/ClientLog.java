package com.midea.isc.auth.common.vo;

public class ClientLog {
	private java.lang.String sourceService;   
	private java.lang.String targetService;   
	private java.lang.String token;   
	private java.lang.String method;   
	private java.lang.String uri;   
	private java.lang.String ipAddress;   
	private java.lang.String requestBody;   
	private java.lang.String responseBody;   
	private java.lang.Integer duration;   
	private java.lang.String exception;   
	private java.util.Date accessTime;   

	public java.lang.String getSourceService() {
		return sourceService;
	}

	public void setSourceService(java.lang.String sourceService) {
		this.sourceService = sourceService;
	}

	public java.lang.String getTargetService() {
		return targetService;
	}

	public void setTargetService(java.lang.String targetService) {
		this.targetService = targetService;
	}

	public java.lang.String getMethod() {
		return method;
	}

	public void setMethod(java.lang.String method) {
		this.method = method;
	}

	public java.lang.String getUri() {
		return uri;
	}

	public void setUri(java.lang.String uri) {
		this.uri = uri;
	}

	public java.lang.String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(java.lang.String requestBody) {
		this.requestBody = requestBody;
	}

	public java.lang.String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(java.lang.String responseBody) {
		this.responseBody = responseBody;
	}

	public java.lang.Integer getDuration() {
		return duration;
	}

	public void setDuration(java.lang.Integer duration) {
		this.duration = duration;
	}

	public java.util.Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(java.util.Date accessTime) {
		this.accessTime = accessTime;
	}

	public java.lang.String getToken() {
		return token;
	}

	public void setToken(java.lang.String token) {
		this.token = token;
	}

	public java.lang.String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(java.lang.String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public java.lang.String getException() {
		return exception;
	}

	public void setException(java.lang.String exception) {
		this.exception = exception;
	}

}
