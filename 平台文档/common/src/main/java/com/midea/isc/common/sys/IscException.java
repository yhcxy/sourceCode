package com.midea.isc.common.sys;

public class IscException extends RuntimeException {
	private static final long serialVersionUID = -6523915997738778189L;

	private String errorCode = "";
	private String errorMsg = "";
	private String params = "";

	public IscException(String errorCode) {
		super("{\"errorCode\":\"" + errorCode + "\"}");
		this.errorCode = errorCode;
	}

	public IscException(String errorCode, String errorMsg) {
		super("{\"errorCode\":\"" + errorCode + "\",\"errorMsg\":\"" + errorMsg + "\"}");
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public IscException(String errorCode, String params, String errorMsg) {
		super("{\"errorCode\":\"" + errorCode + "\",\"params\":\"" + params + "\",\"errorMsg\":\"" + errorMsg + "\"}");
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.params = params;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
