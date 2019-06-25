package com.midea.isc.common.model;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author wangxk7
 * @create 2018-12-4
 */
public class PermissionInfo implements Serializable {
	private static final long serialVersionUID = 8124827283480050355L;

	private String code;
	private String type;
	private String uri;
	private String method;
	private String name;
	private String menu;

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
