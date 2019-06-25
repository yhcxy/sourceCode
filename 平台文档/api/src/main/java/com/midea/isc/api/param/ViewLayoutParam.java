package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.ViewLayout;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewLayoutParam extends ViewLayout {
	// 字段比较符
	private java.lang.String layoutIdCond;   	private java.lang.String viewIdCond;   	private java.lang.String columnNameCond;   	private java.lang.String filterableCond;   	private java.lang.String sortableCond;   	private java.lang.String fixedCond;   	private java.lang.String columnWidthCond;   	private java.lang.String seqCond;   	public java.lang.String getLayoutIdCond() {	    return this.layoutIdCond;	}	public void setLayoutIdCond(java.lang.String layoutIdCond) {	    this.layoutIdCond=layoutIdCond;	}	public java.lang.String getViewIdCond() {	    return this.viewIdCond;	}	public void setViewIdCond(java.lang.String viewIdCond) {	    this.viewIdCond=viewIdCond;	}	public java.lang.String getColumnNameCond() {	    return this.columnNameCond;	}	public void setColumnNameCond(java.lang.String columnNameCond) {	    this.columnNameCond=columnNameCond;	}	public java.lang.String getFilterableCond() {	    return this.filterableCond;	}	public void setFilterableCond(java.lang.String filterableCond) {	    this.filterableCond=filterableCond;	}	public java.lang.String getSortableCond() {	    return this.sortableCond;	}	public void setSortableCond(java.lang.String sortableCond) {	    this.sortableCond=sortableCond;	}	public java.lang.String getFixedCond() {	    return this.fixedCond;	}	public void setFixedCond(java.lang.String fixedCond) {	    this.fixedCond=fixedCond;	}	public java.lang.String getColumnWidthCond() {	    return this.columnWidthCond;	}	public void setColumnWidthCond(java.lang.String columnWidthCond) {	    this.columnWidthCond=columnWidthCond;	}	public java.lang.String getSeqCond() {	    return this.seqCond;	}	public void setSeqCond(java.lang.String seqCond) {	    this.seqCond=seqCond;	}	
}
