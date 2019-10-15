package com.daoyuan.trace.client.interceptors.parse;

import com.daoyuan.trace.client.interceptors.enums.DBActionTypeEnum;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeDataUtil;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ParseInsertData extends ParseContext {

	@Override
	public List<ChangeEntity> parseBefore(MybatisInvocation mybatisInvocation) throws Throwable {
		return new ArrayList<>();
	}

	@Override
	public List<ChangeEntity> parseAfter(MybatisInvocation mybatisInvocation, List<ChangeEntity> changeDatas) throws Exception {
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();

		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.INSERT);

		List<HashMap<String, Object>> updateAfterResults = queryAtInsert(mybatisInvocation,boundSql,sqlParserInfo);

		return buildChangeDatas(updateAfterResults,sqlParserInfo);
	}

	private List<ChangeEntity> buildChangeDatas(final List<HashMap<String, Object>> afterResults, SqlParserInfo sqlParserInfo) {
		List<ChangeEntity> changeDatas = new ArrayList<>();
		if (afterResults != null && !afterResults.isEmpty()) {
			for (HashMap<String, Object> afterDataMap : afterResults) {
				changeDatas.add(ChangeDataUtil.buildChangeDataForAfter(afterDataMap,sqlParserInfo));
			}
		}
		return changeDatas;
	}

}
