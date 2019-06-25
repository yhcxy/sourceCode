package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class LovType extends BasicModel {	
		private java.lang.Integer typeId;   	private java.lang.String application;   	private java.lang.String code;   	private java.lang.Integer parentId;   	private java.lang.String parentCode;   	private java.lang.String enable;   	private java.lang.String description;   	public java.lang.Integer getTypeId() {	    return this.typeId;	}	public void setTypeId(java.lang.Integer typeId) {	    this.typeId=typeId;	}	public java.lang.String getApplication() {	    return this.application;	}	public void setApplication(java.lang.String application) {	    this.application=application;	}	public java.lang.String getCode() {	    return this.code;	}	public void setCode(java.lang.String code) {	    this.code=code;	}	public java.lang.Integer getParentId() {	    return this.parentId;	}	public void setParentId(java.lang.Integer parentId) {	    this.parentId=parentId;	}	public java.lang.String getParentCode() {	    return this.parentCode;	}	public void setParentCode(java.lang.String parentCode) {	    this.parentCode=parentCode;	}	public java.lang.String getEnable() {	    return this.enable;	}	public void setEnable(java.lang.String enable) {	    this.enable=enable;	}	public java.lang.String getDescription() {	    return this.description;	}	public void setDescription(java.lang.String description) {	    this.description=description;	}
}

