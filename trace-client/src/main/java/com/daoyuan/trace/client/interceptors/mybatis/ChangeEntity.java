package com.daoyuan.trace.client.interceptors.mybatis;

import java.util.List;
import java.util.Map;

public class ChangeEntity {
	// 更改后的实体
	private List<ChangeColumn> afterColumnList;
	// 更改前的实体
	private List<ChangeColumn> beforeColumnList;
	// 数据库表名
	private String tableName;
	// 对应的数据库表的主键ID
	private Long entityId;
	// 更新之后的实体
	private Map<String, Object> currentEntityMap;

	public Map<String, Object> getCurrentEntityMap() {
		return currentEntityMap;
	}

	public void setCurrentEntityMap(Map<String, Object> currentEntityMap) {
		this.currentEntityMap = currentEntityMap;
	}

	public List<ChangeColumn> getAfterColumnList() {
		return afterColumnList;
	}

	public void setAfterColumnList(List<ChangeColumn> afterColumnList) {
		this.afterColumnList = afterColumnList;
	}

	public List<ChangeColumn> getBeforeColumnList() {
		return beforeColumnList;
	}

	public void setBeforeColumnList(List<ChangeColumn> beforeColumnList) {
		this.beforeColumnList = beforeColumnList;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

}
