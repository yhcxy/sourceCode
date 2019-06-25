package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.CodeConfigitem;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeConfigitemParam extends CodeConfigitem {
	// 字段比较符
	private java.lang.String itemIdCond;   	private java.lang.String configIdCond;   	private java.lang.String typeCond;   	private java.lang.String valueCond;   	private java.lang.String seqCond;   	private java.lang.String descriptionCond;   	public java.lang.String getItemIdCond() {	    return this.itemIdCond;	}	public void setItemIdCond(java.lang.String itemIdCond) {	    this.itemIdCond=itemIdCond;	}	public java.lang.String getConfigIdCond() {	    return this.configIdCond;	}	public void setConfigIdCond(java.lang.String configIdCond) {	    this.configIdCond=configIdCond;	}	public java.lang.String getTypeCond() {	    return this.typeCond;	}	public void setTypeCond(java.lang.String typeCond) {	    this.typeCond=typeCond;	}	public java.lang.String getValueCond() {	    return this.valueCond;	}	public void setValueCond(java.lang.String valueCond) {	    this.valueCond=valueCond;	}	public java.lang.String getSeqCond() {	    return this.seqCond;	}	public void setSeqCond(java.lang.String seqCond) {	    this.seqCond=seqCond;	}	public java.lang.String getDescriptionCond() {	    return this.descriptionCond;	}	public void setDescriptionCond(java.lang.String descriptionCond) {	    this.descriptionCond=descriptionCond;	}	
}
