package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class ViewLayout extends BasicModel {	
		private java.lang.Integer layoutId;   	private java.lang.Integer viewId;   	private java.lang.String columnName;   //列名	private java.lang.String filterable;   	private java.lang.String sortable;   	private java.lang.String fixed;   //对齐方式 none/left/right	private java.lang.Integer columnWidth;   //列宽(px) 0就是不限制	private java.lang.Integer seq;   //排序  小的排前边	public java.lang.Integer getLayoutId() {	    return this.layoutId;	}	public void setLayoutId(java.lang.Integer layoutId) {	    this.layoutId=layoutId;	}	public java.lang.Integer getViewId() {	    return this.viewId;	}	public void setViewId(java.lang.Integer viewId) {	    this.viewId=viewId;	}	public java.lang.String getColumnName() {	    return this.columnName;	}	public void setColumnName(java.lang.String columnName) {	    this.columnName=columnName;	}	public java.lang.String getFilterable() {	    return this.filterable;	}	public void setFilterable(java.lang.String filterable) {	    this.filterable=filterable;	}	public java.lang.String getSortable() {	    return this.sortable;	}	public void setSortable(java.lang.String sortable) {	    this.sortable=sortable;	}	public java.lang.String getFixed() {	    return this.fixed;	}	public void setFixed(java.lang.String fixed) {	    this.fixed=fixed;	}	public java.lang.Integer getColumnWidth() {	    return this.columnWidth;	}	public void setColumnWidth(java.lang.Integer columnWidth) {	    this.columnWidth=columnWidth;	}	public java.lang.Integer getSeq() {	    return this.seq;	}	public void setSeq(java.lang.Integer seq) {	    this.seq=seq;	}
}

