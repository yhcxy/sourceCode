package com.midea.isc.attachment.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.attachment.model.AttachmentConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentConfigParam extends AttachmentConfig {
	// 字段比较符
	private java.lang.String configIdCond;   	private java.lang.String applicationCond;   	private java.lang.String appIdCond;   	private java.lang.String appKeyCond;   	private java.lang.String defaultBucketCond;   	public java.lang.String getConfigIdCond() {	    return this.configIdCond;	}	public void setConfigIdCond(java.lang.String configIdCond) {	    this.configIdCond=configIdCond;	}	public java.lang.String getApplicationCond() {	    return this.applicationCond;	}	public void setApplicationCond(java.lang.String applicationCond) {	    this.applicationCond=applicationCond;	}	public java.lang.String getAppIdCond() {	    return this.appIdCond;	}	public void setAppIdCond(java.lang.String appIdCond) {	    this.appIdCond=appIdCond;	}	public java.lang.String getAppKeyCond() {	    return this.appKeyCond;	}	public void setAppKeyCond(java.lang.String appKeyCond) {	    this.appKeyCond=appKeyCond;	}	public java.lang.String getDefaultBucketCond() {	    return this.defaultBucketCond;	}	public void setDefaultBucketCond(java.lang.String defaultBucketCond) {	    this.defaultBucketCond=defaultBucketCond;	}	
}
