package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.ViewFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewFilterParam extends ViewFilter {
	// 字段比较符
	private java.lang.String filterIdCond;   	private java.lang.String viewIdCond;   	private java.lang.String columnNameCond;   	private java.lang.String filterConditionCond;   	private java.lang.String filterValueCond;   	public java.lang.String getFilterIdCond() {	    return this.filterIdCond;	}	public void setFilterIdCond(java.lang.String filterIdCond) {	    this.filterIdCond=filterIdCond;	}	public java.lang.String getViewIdCond() {	    return this.viewIdCond;	}	public void setViewIdCond(java.lang.String viewIdCond) {	    this.viewIdCond=viewIdCond;	}	public java.lang.String getColumnNameCond() {	    return this.columnNameCond;	}	public void setColumnNameCond(java.lang.String columnNameCond) {	    this.columnNameCond=columnNameCond;	}	public java.lang.String getFilterConditionCond() {	    return this.filterConditionCond;	}	public void setFilterConditionCond(java.lang.String filterConditionCond) {	    this.filterConditionCond=filterConditionCond;	}	public java.lang.String getFilterValueCond() {	    return this.filterValueCond;	}	public void setFilterValueCond(java.lang.String filterValueCond) {	    this.filterValueCond=filterValueCond;	}	
}
