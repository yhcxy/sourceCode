package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class Module extends BasicModel {	
		private java.lang.Integer moduleId;   	private java.lang.String code;   	private java.lang.String application;   	private java.lang.String visible;   	private java.lang.String description;   	public java.lang.Integer getModuleId() {	    return this.moduleId;	}	public void setModuleId(java.lang.Integer moduleId) {	    this.moduleId=moduleId;	}	public java.lang.String getCode() {	    return this.code;	}	public void setCode(java.lang.String code) {	    this.code=code;	}	public java.lang.String getApplication() {	    return this.application;	}	public void setApplication(java.lang.String application) {	    this.application=application;	}	public java.lang.String getVisible() {	    return this.visible;	}	public void setVisible(java.lang.String visible) {	    this.visible=visible;	}	public java.lang.String getDescription() {	    return this.description;	}	public void setDescription(java.lang.String description) {	    this.description=description;	}
}

