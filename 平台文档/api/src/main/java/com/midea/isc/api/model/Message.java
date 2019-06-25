package com.midea.isc.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = {
		"attribute1", "attribute2", "attribute3", "attribute4", "attribute5"} , ignoreUnknown = true)
public class Message extends BasicModel {	
		private java.lang.Integer messageId;   	private java.lang.String application;   	private java.lang.String module;   	private java.lang.String type;   	private java.lang.String msgCode;   	private java.lang.String autoDisappear;   	private java.lang.String remark;   	private java.lang.String msgContent;   	public java.lang.Integer getMessageId() {	    return this.messageId;	}	public void setMessageId(java.lang.Integer messageId) {	    this.messageId=messageId;	}	public java.lang.String getApplication() {	    return this.application;	}	public void setApplication(java.lang.String application) {	    this.application=application;	}	public java.lang.String getModule() {	    return this.module;	}	public void setModule(java.lang.String module) {	    this.module=module;	}	public java.lang.String getType() {	    return this.type;	}	public void setType(java.lang.String type) {	    this.type=type;	}	public java.lang.String getMsgCode() {	    return this.msgCode;	}	public void setMsgCode(java.lang.String msgCode) {	    this.msgCode=msgCode;	}	public java.lang.String getAutoDisappear() {	    return this.autoDisappear;	}	public void setAutoDisappear(java.lang.String autoDisappear) {	    this.autoDisappear=autoDisappear;	}	public java.lang.String getRemark() {	    return this.remark;	}	public void setRemark(java.lang.String remark) {	    this.remark=remark;	}	public java.lang.String getMsgContent() {	    return this.msgContent;	}	public void setMsgContent(java.lang.String msgContent) {	    this.msgContent=msgContent;	}
}

