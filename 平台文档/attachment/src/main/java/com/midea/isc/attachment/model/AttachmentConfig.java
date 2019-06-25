package com.midea.isc.attachment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class AttachmentConfig extends BasicModel {	
		private java.lang.Integer configId;   	private java.lang.String application;   	private java.lang.String appId;   	private java.lang.String appKey;   	private java.lang.String defaultBucket;   	public java.lang.Integer getConfigId() {	    return this.configId;	}	public void setConfigId(java.lang.Integer configId) {	    this.configId=configId;	}	public java.lang.String getApplication() {	    return this.application;	}	public void setApplication(java.lang.String application) {	    this.application=application;	}	public java.lang.String getAppId() {	    return this.appId;	}	public void setAppId(java.lang.String appId) {	    this.appId=appId;	}	public java.lang.String getAppKey() {	    return this.appKey;	}	public void setAppKey(java.lang.String appKey) {	    this.appKey=appKey;	}	public java.lang.String getDefaultBucket() {	    return this.defaultBucket;	}	public void setDefaultBucket(java.lang.String defaultBucket) {	    this.defaultBucket=defaultBucket;	}
}

