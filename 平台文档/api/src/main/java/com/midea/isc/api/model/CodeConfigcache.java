package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class CodeConfigcache extends BasicModel {	
		private java.lang.Integer cacheId;   	private java.lang.Integer configId;   //系统编码ID	private java.lang.String configCode;   //编码	private java.lang.String expression;   //表达式	private java.lang.Integer sequence;   //序号	public java.lang.Integer getCacheId() {	    return this.cacheId;	}	public void setCacheId(java.lang.Integer cacheId) {	    this.cacheId=cacheId;	}	public java.lang.Integer getConfigId() {	    return this.configId;	}	public void setConfigId(java.lang.Integer configId) {	    this.configId=configId;	}	public java.lang.String getConfigCode() {	    return this.configCode;	}	public void setConfigCode(java.lang.String configCode) {	    this.configCode=configCode;	}	public java.lang.String getExpression() {	    return this.expression;	}	public void setExpression(java.lang.String expression) {	    this.expression=expression;	}	public java.lang.Integer getSequence() {	    return this.sequence;	}	public void setSequence(java.lang.Integer sequence) {	    this.sequence=sequence;	}
}

