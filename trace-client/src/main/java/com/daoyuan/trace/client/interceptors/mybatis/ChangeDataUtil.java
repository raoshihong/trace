package com.daoyuan.trace.client.interceptors.mybatis;

import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.interceptors.parse.SqlParserInfo;
import com.daoyuan.trace.dto.TablePropertyConfigDto;
import com.google.common.collect.Lists;
import com.daoyuan.trace.client.manager.TraceManager;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ChangeDataUtil {

	/**
	 * 构建修改前的数据
	 * @param beforeDataMap
	 * @param sqlParserInfo
	 * @return
	 */
	public static ChangeEntity buildChangeDataForBefore(final Map<String, Object> beforeDataMap, SqlParserInfo sqlParserInfo) {
		log.info("ChangeDataUtil.buildChangeDataForBefore beforeDataMap:{}", JSON.toJSONString(beforeDataMap));
		ChangeEntity changeData = new ChangeEntity();
		List<ChangeColumn> afterColumnList = new ArrayList<>();
		changeData.setAfterColumnList(afterColumnList);
		changeData.setBeforeColumnList(buildChangeObject(beforeDataMap,sqlParserInfo));
		changeData.setCurrentEntityMap(beforeDataMap);
		changeData.setTableName(sqlParserInfo.getTableName());
		TraceManager traceManager = SpringApplicationContextManager.getBean(TraceManager.class);
		TablePropertyConfigDto tablePropertyConfig = traceManager.getTablePrimaryProperty(sqlParserInfo.getTableName());
		if (Objects.nonNull(tablePropertyConfig) && Objects.nonNull(tablePropertyConfig.getProEnName())) {
			//获取这个表的主键值
			changeData.setEntityId((Long) beforeDataMap.get(tablePropertyConfig.getProEnName()));
		}
		log.info("ChangeDataUtil.buildChangeDataForBefore changeData:{}", JSON.toJSONString(changeData));
		return changeData;
	}

	/**
	 * 构建修改后的数据
	 * @param afterDataMap
	 * @param sqlParserInfo
	 * @return
	 */
	public static ChangeEntity buildChangeDataForAfter(final Map<String, Object> afterDataMap, SqlParserInfo sqlParserInfo) {
		log.info("ChangeDataUtil.buildChangeDataForBefore afterDataMap:{}", JSON.toJSONString(afterDataMap));
		ChangeEntity changeData = new ChangeEntity();
		List<ChangeColumn> beforeColumnList = new ArrayList<>();
		changeData.setAfterColumnList(buildChangeObject(afterDataMap,sqlParserInfo));
		changeData.setBeforeColumnList(beforeColumnList);
		changeData.setCurrentEntityMap(afterDataMap);
		changeData.setTableName(sqlParserInfo.getTableName());
		TraceManager traceManager = SpringApplicationContextManager.getBean(TraceManager.class);
		TablePropertyConfigDto tablePropertyConfig = traceManager.getTablePrimaryProperty(sqlParserInfo.getTableName());
		if (Objects.nonNull(tablePropertyConfig) && Objects.nonNull(tablePropertyConfig.getProEnName())) {
			changeData.setEntityId((Long) afterDataMap.get(tablePropertyConfig.getProEnName()));
		}
		log.info("ChangeDataUtil.buildChangeDataForAfter changeData:{}", JSON.toJSONString(changeData));
		return changeData;
	}

	/**
	 * 对数据字段名做中文映射
	 * @param dataMap
	 * @param sqlParserInfo
	 * @return
	 */
	private static List<ChangeColumn> buildChangeObject(final Map<String, Object> dataMap, SqlParserInfo sqlParserInfo){
		log.info("buildChangeObject dataMap:{}",JSON.toJSONString(dataMap));
		List<ChangeColumn> changeObjects = Lists.newArrayList();
		dataMap.forEach((key, value) -> {
			//创建一个列对象
			ChangeColumn changeColumn = new ChangeColumn();
			changeColumn.setName(key);
			changeColumn.setValue(value);
			//通过表配置信息,获取对应表的对应字段的中文名
			TraceManager traceManager = SpringApplicationContextManager.getBean(TraceManager.class);
			TablePropertyConfigDto tablePropertyConfig = traceManager.getTablePropertyConfig(sqlParserInfo.getTableName(),key);
			changeColumn.setCnName(Objects.isNull(tablePropertyConfig)?"":tablePropertyConfig.getProCnName());
			changeObjects.add(changeColumn);
		});
		return changeObjects;
	}
}
