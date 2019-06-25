package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.CodeConfigcache;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeConfigcacheParam extends CodeConfigcache {
	// 字段比较符
	private java.lang.String cacheIdCond;   	private java.lang.String configIdCond;   	private java.lang.String configCodeCond;   	private java.lang.String expressionCond;   	private java.lang.String sequenceCond;   	public java.lang.String getCacheIdCond() {	    return this.cacheIdCond;	}	public void setCacheIdCond(java.lang.String cacheIdCond) {	    this.cacheIdCond=cacheIdCond;	}	public java.lang.String getConfigIdCond() {	    return this.configIdCond;	}	public void setConfigIdCond(java.lang.String configIdCond) {	    this.configIdCond=configIdCond;	}	public java.lang.String getConfigCodeCond() {	    return this.configCodeCond;	}	public void setConfigCodeCond(java.lang.String configCodeCond) {	    this.configCodeCond=configCodeCond;	}	public java.lang.String getExpressionCond() {	    return this.expressionCond;	}	public void setExpressionCond(java.lang.String expressionCond) {	    this.expressionCond=expressionCond;	}	public java.lang.String getSequenceCond() {	    return this.sequenceCond;	}	public void setSequenceCond(java.lang.String sequenceCond) {	    this.sequenceCond=sequenceCond;	}	
}
