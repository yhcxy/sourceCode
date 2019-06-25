package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class CodeConfig extends BasicModel {	
		private java.lang.Integer configId;   	private java.lang.String application;   //系统	private java.lang.String code;   //编码	private java.lang.String cycle;   //序列重置周期	private java.lang.String cacheMode;   //序列缓存类型	private java.lang.String description;   //描述	public java.lang.Integer getConfigId() {	    return this.configId;	}	public void setConfigId(java.lang.Integer configId) {	    this.configId=configId;	}	public java.lang.String getApplication() {	    return this.application;	}	public void setApplication(java.lang.String application) {	    this.application=application;	}	public java.lang.String getCode() {	    return this.code;	}	public void setCode(java.lang.String code) {	    this.code=code;	}	public java.lang.String getCycle() {	    return this.cycle;	}	public void setCycle(java.lang.String cycle) {	    this.cycle=cycle;	}	public java.lang.String getCacheMode() {	    return this.cacheMode;	}	public void setCacheMode(java.lang.String cacheMode) {	    this.cacheMode=cacheMode;	}	public java.lang.String getDescription() {	    return this.description;	}	public void setDescription(java.lang.String description) {	    this.description=description;	}
}

