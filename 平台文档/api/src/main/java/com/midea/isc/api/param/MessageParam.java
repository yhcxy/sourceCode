package com.midea.isc.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.midea.isc.api.model.Message;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageParam extends Message {
	// 字段比较符
	private java.lang.String messageIdCond;   	private java.lang.String applicationCond;   	private java.lang.String moduleCond;   	private java.lang.String typeCond;   	private java.lang.String msgCodeCond;   	private java.lang.String autoDisappearCond;   	private java.lang.String remarkCond;   	private java.lang.String msgContentCond;   	public java.lang.String getMessageIdCond() {	    return this.messageIdCond;	}	public void setMessageIdCond(java.lang.String messageIdCond) {	    this.messageIdCond=messageIdCond;	}	public java.lang.String getApplicationCond() {	    return this.applicationCond;	}	public void setApplicationCond(java.lang.String applicationCond) {	    this.applicationCond=applicationCond;	}	public java.lang.String getModuleCond() {	    return this.moduleCond;	}	public void setModuleCond(java.lang.String moduleCond) {	    this.moduleCond=moduleCond;	}	public java.lang.String getTypeCond() {	    return this.typeCond;	}	public void setTypeCond(java.lang.String typeCond) {	    this.typeCond=typeCond;	}	public java.lang.String getMsgCodeCond() {	    return this.msgCodeCond;	}	public void setMsgCodeCond(java.lang.String msgCodeCond) {	    this.msgCodeCond=msgCodeCond;	}	public java.lang.String getAutoDisappearCond() {	    return this.autoDisappearCond;	}	public void setAutoDisappearCond(java.lang.String autoDisappearCond) {	    this.autoDisappearCond=autoDisappearCond;	}	public java.lang.String getRemarkCond() {	    return this.remarkCond;	}	public void setRemarkCond(java.lang.String remarkCond) {	    this.remarkCond=remarkCond;	}	public java.lang.String getMsgContentCond() {	    return this.msgContentCond;	}	public void setMsgContentCond(java.lang.String msgContentCond) {	    this.msgContentCond=msgContentCond;	}	
}
