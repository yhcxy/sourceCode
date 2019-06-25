package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.Module;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleParam extends Module {
	// 字段比较符
	private java.lang.String moduleIdCond;   	private java.lang.String codeCond;   	private java.lang.String applicationCond;   	private java.lang.String visibleCond;   	private java.lang.String descriptionCond;   	public java.lang.String getModuleIdCond() {	    return this.moduleIdCond;	}	public void setModuleIdCond(java.lang.String moduleIdCond) {	    this.moduleIdCond=moduleIdCond;	}	public java.lang.String getCodeCond() {	    return this.codeCond;	}	public void setCodeCond(java.lang.String codeCond) {	    this.codeCond=codeCond;	}	public java.lang.String getApplicationCond() {	    return this.applicationCond;	}	public void setApplicationCond(java.lang.String applicationCond) {	    this.applicationCond=applicationCond;	}	public java.lang.String getVisibleCond() {	    return this.visibleCond;	}	public void setVisibleCond(java.lang.String visibleCond) {	    this.visibleCond=visibleCond;	}	public java.lang.String getDescriptionCond() {	    return this.descriptionCond;	}	public void setDescriptionCond(java.lang.String descriptionCond) {	    this.descriptionCond=descriptionCond;	}	
}
