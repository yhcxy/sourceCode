package com.midea.isc.common.model;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.constant.CommonConstants;
import com.midea.isc.common.util.CommUtil;
import com.midea.isc.common.model.UserInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile implements java.io.Serializable {
	@JsonIgnore
	private static final long serialVersionUID = -5287571881139932275L;
	private String __token;
	private String __app;
	private long __sessionExpired;

	private String __userId;
	private String __userName;
	private String __fullName;
	private String __country;
	private String __timezone;
	private String __language;// 当前语言
	private String __currency;// 默认币种
	private String __sobId;// 当前账套
	private String __divisionId;// 当前事业部Id
	private String __organizationId;// 当前组织Id

	private JSONObject __infos; // 业务系统自定义扩展信息

	private long __timestamp;// profile的生成时间，用来做超时判断

	private List<String> languages; // setWho时初始化系统语言，一般插入数据时考虑多语言

	public Profile() {
	}

	public Profile(String userId, String userName) {
		__userId = userId;
		__userName = userName;
	}

	public Profile(UserInfo userInfo) {
		__userId = userInfo.getUserId();
		__userName = userInfo.getUserName();
		__fullName = userInfo.getFullName();
		__country = CommUtil.nvl(userInfo.getCountry(), CommonConstants.SYSTEM_TERRITORY);
		__timezone = CommUtil.nvl(userInfo.getTimezone(), CommonConstants.SYSTEM_TIMEZONE);
		__language = userInfo.getLanguage();
	}

	public String get__token() {
		return __token;
	}

	public void set__token(String __token) {
		this.__token = __token;
	}
	
	public String get__app() {
		return __app;
	}

	public void set__app(String __app) {
		this.__app = __app;
	}
	
	public long get__sessionExpired() {
		return __sessionExpired;
	}

	public void set__sessionExpired(long __sessionExpired) {
		this.__sessionExpired = __sessionExpired;
	}

	public String get__userId() {
		return __userId;
	}

	public void set__userId(String __userId) {
		this.__userId = __userId;
	}

	public String get__userName() {
		return __userName;
	}

	public void set__userName(String __userName) {
		this.__userName = __userName;
	}

	public String get__fullName() {
		return __fullName;
	}

	public void set__fullName(String __fullName) {
		this.__fullName = __fullName;
	}

	public String get__country() {
		return __country;
	}

	public void set__country(String __country) {
		this.__country = __country;
	}

	public String get__timezone() {
		return __timezone;
	}

	public void set__timezone(String __timezone) {
		this.__timezone = __timezone;
	}

	public long get__timestamp() {
		return __timestamp;
	}

	public void set__timestamp(long __timestamp) {
		this.__timestamp = __timestamp;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String get__language() {
		return __language;
	}

	public void set__language(String __language) {
		this.__language = __language;
	}

	public String get__currency() {
		return __currency;
	}

	public void set__currency(String __currency) {
		this.__currency = __currency;
	}

	public String get__sobId() {
		return __sobId;
	}

	public void set__sobId(String __sobId) {
		this.__sobId = __sobId;
	}

	public String get__divisionId() {
		return __divisionId;
	}

	public void set__divisionId(String __divisionId) {
		this.__divisionId = __divisionId;
	}

	public String get__organizationId() {
		return __organizationId;
	}

	public void set__organizationId(String __organizationId) {
		this.__organizationId = __organizationId;
	}

	public JSONObject get__infos() {
		return __infos;
	}

	public void set__infos(JSONObject __infos) {
		this.__infos = __infos;
	}

}