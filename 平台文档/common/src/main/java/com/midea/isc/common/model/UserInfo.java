package com.midea.isc.common.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = -3302484038910890833L;

	private java.lang.String userId;
	private java.lang.String userName;
	private java.util.Date disableDate;
	private java.lang.String fullName;
	private java.lang.String gender;
	private java.lang.String emailAddress;
	private java.lang.String mobile;
	private java.lang.String nickname;
	private java.lang.String emailAlias;
	private java.lang.String timezone;
	private java.lang.String country;
	private java.lang.String language;

	public java.lang.String getUserId() {
		return userId;
	}

	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	public java.lang.String getUserName() {
		return userName;
	}

	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	public java.util.Date getDisableDate() {
		return disableDate;
	}

	public void setDisableDate(java.util.Date disableDate) {
		this.disableDate = disableDate;
	}

	public java.lang.String getFullName() {
		return fullName;
	}

	public void setFullName(java.lang.String fullName) {
		this.fullName = fullName;
	}

	public java.lang.String getGender() {
		return gender;
	}

	public void setGender(java.lang.String gender) {
		this.gender = gender;
	}

	public java.lang.String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getNickname() {
		return nickname;
	}

	public void setNickname(java.lang.String nickname) {
		this.nickname = nickname;
	}

	public java.lang.String getEmailAlias() {
		return emailAlias;
	}

	public void setEmailAlias(java.lang.String emailAlias) {
		this.emailAlias = emailAlias;
	}

	public java.lang.String getTimezone() {
		return timezone;
	}

	public void setTimezone(java.lang.String timezone) {
		this.timezone = timezone;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getLanguage() {
		return language;
	}

	public void setLanguage(java.lang.String language) {
		this.language = language;
	}

}
