package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class ViewFilter extends BasicModel {	
		private java.lang.Integer filterId;   	private java.lang.Integer viewId;   	private java.lang.String columnName;   //列名	private java.lang.String filterCondition;   //符号	private java.lang.String filterValue;   //值	public java.lang.Integer getFilterId() {	    return this.filterId;	}	public void setFilterId(java.lang.Integer filterId) {	    this.filterId=filterId;	}	public java.lang.Integer getViewId() {	    return this.viewId;	}	public void setViewId(java.lang.Integer viewId) {	    this.viewId=viewId;	}	public java.lang.String getColumnName() {	    return this.columnName;	}	public void setColumnName(java.lang.String columnName) {	    this.columnName=columnName;	}	public java.lang.String getFilterCondition() {	    return this.filterCondition;	}	public void setFilterCondition(java.lang.String filterCondition) {	    this.filterCondition=filterCondition;	}	public java.lang.String getFilterValue() {	    return this.filterValue;	}	public void setFilterValue(java.lang.String filterValue) {	    this.filterValue=filterValue;	}
}

