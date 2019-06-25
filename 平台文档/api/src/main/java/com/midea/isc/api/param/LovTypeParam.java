package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.LovType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LovTypeParam extends LovType {
	// 字段比较符
	private java.lang.String typeIdCond;   	private java.lang.String applicationCond;   	private java.lang.String codeCond;   	private java.lang.String parentIdCond;   	private java.lang.String parentCodeCond;   	private java.lang.String enableCond;   	private java.lang.String descriptionCond;   	public java.lang.String getTypeIdCond() {	    return this.typeIdCond;	}	public void setTypeIdCond(java.lang.String typeIdCond) {	    this.typeIdCond=typeIdCond;	}	public java.lang.String getApplicationCond() {	    return this.applicationCond;	}	public void setApplicationCond(java.lang.String applicationCond) {	    this.applicationCond=applicationCond;	}	public java.lang.String getCodeCond() {	    return this.codeCond;	}	public void setCodeCond(java.lang.String codeCond) {	    this.codeCond=codeCond;	}	public java.lang.String getParentIdCond() {	    return this.parentIdCond;	}	public void setParentIdCond(java.lang.String parentIdCond) {	    this.parentIdCond=parentIdCond;	}	public java.lang.String getParentCodeCond() {	    return this.parentCodeCond;	}	public void setParentCodeCond(java.lang.String parentCodeCond) {	    this.parentCodeCond=parentCodeCond;	}	public java.lang.String getEnableCond() {	    return this.enableCond;	}	public void setEnableCond(java.lang.String enableCond) {	    this.enableCond=enableCond;	}	public java.lang.String getDescriptionCond() {	    return this.descriptionCond;	}	public void setDescriptionCond(java.lang.String descriptionCond) {	    this.descriptionCond=descriptionCond;	}	
}
