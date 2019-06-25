package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class CodeConfigitem extends BasicModel {	
		private java.lang.Integer itemId;   	private java.lang.Integer configId;   //系统编码ID	private java.lang.String type;   //规则类型	private java.lang.String value;   //规则函数	private java.lang.Integer seq;   //排序	private java.lang.String description;   //描述	public java.lang.Integer getItemId() {	    return this.itemId;	}	public void setItemId(java.lang.Integer itemId) {	    this.itemId=itemId;	}	public java.lang.Integer getConfigId() {	    return this.configId;	}	public void setConfigId(java.lang.Integer configId) {	    this.configId=configId;	}	public java.lang.String getType() {	    return this.type;	}	public void setType(java.lang.String type) {	    this.type=type;	}	public java.lang.String getValue() {	    return this.value;	}	public void setValue(java.lang.String value) {	    this.value=value;	}	public java.lang.Integer getSeq() {	    return this.seq;	}	public void setSeq(java.lang.Integer seq) {	    this.seq=seq;	}	public java.lang.String getDescription() {	    return this.description;	}	public void setDescription(java.lang.String description) {	    this.description=description;	}
}

