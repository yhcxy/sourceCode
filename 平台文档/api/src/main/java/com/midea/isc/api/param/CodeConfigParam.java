package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.CodeConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeConfigParam extends CodeConfig {
	// 字段比较符
	private java.lang.String configIdCond;   	private java.lang.String applicationCond;   	private java.lang.String codeCond;   	private java.lang.String cycleCond;   	private java.lang.String cacheModeCond;   	private java.lang.String descriptionCond;   	public java.lang.String getConfigIdCond() {	    return this.configIdCond;	}	public void setConfigIdCond(java.lang.String configIdCond) {	    this.configIdCond=configIdCond;	}	public java.lang.String getApplicationCond() {	    return this.applicationCond;	}	public void setApplicationCond(java.lang.String applicationCond) {	    this.applicationCond=applicationCond;	}	public java.lang.String getCodeCond() {	    return this.codeCond;	}	public void setCodeCond(java.lang.String codeCond) {	    this.codeCond=codeCond;	}	public java.lang.String getCycleCond() {	    return this.cycleCond;	}	public void setCycleCond(java.lang.String cycleCond) {	    this.cycleCond=cycleCond;	}	public java.lang.String getCacheModeCond() {	    return this.cacheModeCond;	}	public void setCacheModeCond(java.lang.String cacheModeCond) {	    this.cacheModeCond=cacheModeCond;	}	public java.lang.String getDescriptionCond() {	    return this.descriptionCond;	}	public void setDescriptionCond(java.lang.String descriptionCond) {	    this.descriptionCond=descriptionCond;	}	
}
