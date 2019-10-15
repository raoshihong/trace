package com.daoyuan.trace.client.interceptors.parse;

import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.interceptors.enums.DBActionTypeEnum;
import com.google.common.collect.Lists;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeDataUtil;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ParseUpdateData extends ParseContext {

	//可能批量更新
	@Override
	public List<ChangeEntity> parseBefore(MybatisInvocation mybatisInvocation) throws Throwable {
		log.info("ParseUpdateData.parseBefore start");
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();

		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.UPDATE);

		//查询更新前的数据
		List<HashMap<String, Object>> updateBeforeResults = query(mybatisInvocation,boundSql, sqlParserInfo);

		log.info("ParseUpdateData.parseBefore updateBeforeResults:{}", JSON.toJSONString(updateBeforeResults));

		return buildUpdateBeforeEvents(updateBeforeResults,sqlParserInfo);
	}

	@Override
	public List<ChangeEntity> parseAfter(MybatisInvocation mybatisInvocation, List<ChangeEntity> updateBeforeDatas) throws Throwable {
		log.info("ParseUpdateData.parseAfter start");
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();

		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.UPDATE);

		//查询更新前的数据
		List<HashMap<String, Object>> updateAfterResults = query(mybatisInvocation,boundSql, sqlParserInfo);

		List<ChangeEntity> updateAfterDatas = buildUpdateAfterEvents(updateAfterResults,sqlParserInfo);

		log.info("ParseUpdateData.parseAfter updateAfterDatas:{}", JSON.toJSONString(updateAfterDatas));

		return buildUpdateBeforeAndAfterEvents(updateBeforeDatas,updateAfterDatas);
	}


	private List<ChangeEntity> buildUpdateBeforeEvents(final List<HashMap<String, Object>> updateBeforeResults, SqlParserInfo sqlParserInfo){
		log.info("ParseUpdateData.buildUpdateBeforeEvents start");
		List<ChangeEntity> changeDatas = Lists.newArrayList();
		if(!CollectionUtils.isEmpty(updateBeforeResults)){
			for(HashMap<String, Object> beforeDataMap : updateBeforeResults){
				changeDatas.add(ChangeDataUtil.buildChangeDataForBefore(beforeDataMap,sqlParserInfo));
			}
		}
		return changeDatas;
	}

	private List<ChangeEntity> buildUpdateAfterEvents(final List<HashMap<String, Object>> updateAfterResults, SqlParserInfo sqlParserInfo){

		List<ChangeEntity> changeDatas = Lists.newArrayList();
		if(!CollectionUtils.isEmpty(updateAfterResults)){
			for(HashMap<String, Object> afterDataMap : updateAfterResults){
				changeDatas.add(ChangeDataUtil.buildChangeDataForAfter(afterDataMap,sqlParserInfo));
			}
		}

		return changeDatas;
	}


	private List<ChangeEntity> buildUpdateBeforeAndAfterEvents(List<ChangeEntity> updateBeforeDatas, List<ChangeEntity> updateAfterDatas){
		log.info("buildUpdateBeforeAndAfterEvents updateBeforeDatas:{},updateAfterDatas:{}",JSON.toJSONString(updateBeforeDatas),JSON.toJSONString(updateAfterDatas));
		updateBeforeDatas.stream().forEach(changeData -> {
			Optional<ChangeEntity> optionalChangeData = updateAfterDatas.stream().filter(afterChangeData -> afterChangeData.getEntityId().equals(changeData.getEntityId())).findAny();
			if (optionalChangeData.isPresent()) {
				ChangeEntity afterChangeData = optionalChangeData.get();
				changeData.setAfterColumnList(afterChangeData.getAfterColumnList());
				changeData.setCurrentEntityMap(afterChangeData.getCurrentEntityMap());
			}
		});

		return updateBeforeDatas;
	}
	
}
