package com.midea.isc.common.param;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.midea.isc.common.model.Profile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicParam {

	private Profile profile;

	private java.lang.Integer __page;// 当前页（从1开始）
	private java.lang.Integer __pagesize;
	private HashMap<String, String> orderFields;
	private java.lang.String limitClause;// 注意设置set__page或set__pagesize会修改此字段

	@JsonIgnore
	public Profile getProfile() {
		return profile;
	}

	@JsonProperty
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@JsonIgnore
	public java.lang.Integer get__page() {
		return __page;
	}

	/**
	 * 设置当前页（从1开始）
	 * 
	 * @param __page
	 */
	@JsonProperty
	public void set__page(java.lang.Integer __page) {
		this.__page = __page;
		setLimitClause(null);
	}

	@JsonIgnore
	public java.lang.Integer get__pagesize() {
		return __pagesize;
	}

	@JsonProperty
	public void set__pagesize(java.lang.Integer __pagesize) {
		this.__pagesize = __pagesize;
		setLimitClause(null);
	}
	
	@JsonIgnore
	public HashMap<String,String> getOrderFields() {
		return orderFields;
	}

	/**
	 * 设置排序字段（不需要order by 关键字）
	 * 
	 * @param orderByClause
	 *            例如：column1 asc,column2 desc
	 */
	@JsonProperty
	public void setOrderFields(HashMap<String,String> orderFields) {
		this.orderFields = orderFields;
	}

	@JsonIgnore
	public java.lang.String getLimitClause() {
		return limitClause;
	}

	/**
	 * 设置分页查询范围
	 * 
	 * @param limitClause
	 *            分页语句，为空则自动根据page和pagesize计算
	 */
	@JsonProperty
	public void setLimitClause(java.lang.String limitClause) {
		this.limitClause = limitClause == null ? //
				((this.__page == null || this.__pagesize == null) ? null
						: ("limit " + ((this.__page - 1) * this.__pagesize) + "," + this.__pagesize))
				: limitClause;
		// LOG.println("limitClause = " + this.limitClause);
	}

	/**
	 * 设置分页查询范围
	 * 
	 * @param page
	 *            从1开始
	 * @param pagesize
	 */
	public void setLimitClause(int page, int pagesize) {
		setPageAndPagesize(page, pagesize);
	}

	/**
	 * 设置分页查询范围
	 * 
	 * @param page
	 *            从1开始
	 * @param pagesize
	 */
	public void setPageAndPagesize(int page, int pagesize) {
		this.set__page(page);
		this.set__pagesize(pagesize);
		this.setLimitClause(null);
	}
}
