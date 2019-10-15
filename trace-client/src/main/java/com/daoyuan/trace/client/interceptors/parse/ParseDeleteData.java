package com.daoyuan.trace.client.interceptors.parse;

import com.daoyuan.trace.client.interceptors.enums.DBActionTypeEnum;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeDataUtil;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseDeleteData extends ParseContext {

	@Override
	public List<ChangeEntity> parseBefore(MybatisInvocation mybatisInvocation) throws Throwable {
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();

		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.DELETE);

		// 获取要删除前数据
		List<HashMap<String, Object>> beforeRsults = query(mybatisInvocation, boundSql, sqlParserInfo);

		List<ChangeEntity> results = buildChangeDatas(beforeRsults, sqlParserInfo);

		return results;
	}

	private List<ChangeEntity> buildChangeDatas(final List<HashMap<String, Object>> beforeResults, SqlParserInfo sqlParserInfo) {
		List<ChangeEntity> changeDatas = new ArrayList<>();
		if (beforeResults != null && !beforeResults.isEmpty()) {
			for (HashMap<String, Object> beforeDataMap : beforeResults) {
				changeDatas.add(ChangeDataUtil.buildChangeDataForBefore(beforeDataMap,sqlParserInfo));
			}
		}
		return changeDatas;
	}

	@Override
	public List<ChangeEntity> parseAfter(MybatisInvocation mybatisInvocation, List<ChangeEntity> changeDatas) throws Exception {

		return changeDatas;
	}

}
