package com.daoyuan.trace.client.interceptors.mybatis;

public class ChangeColumn {

	/**
	 * 英文名
	 */
	private String name;
	/**
	 * 中文名
	 */
	private String cnName;
	/**
	 * 字段的值
	 */
	private Object value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
}
