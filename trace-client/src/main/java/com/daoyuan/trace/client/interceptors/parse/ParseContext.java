package com.daoyuan.trace.client.interceptors.parse;

import com.daoyuan.trace.client.interceptors.visitor.TraceExpressionDeParser;
import com.daoyuan.trace.dto.TablePropertyConfigDto;
import com.google.common.collect.Lists;
import com.daoyuan.trace.client.interceptors.mybatis.MSUtils;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import com.daoyuan.trace.client.manager.TraceManager;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

public abstract class ParseContext implements DataPaser {
	
	/**
	 * 查询操作前的数据
	 * @param mybatisInvocation
	 * @param boundSql
	 * @param sqlParserInfo
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, Object>> query(MybatisInvocation mybatisInvocation, BoundSql boundSql, SqlParserInfo sqlParserInfo) throws SQLException {
		Table table = sqlParserInfo.getTable();
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		//构建一个查询的statement
		MappedStatement selectMappedStatement = MSUtils.newHashMapMappedStatement(mappedStatement);

		//通过jsqlparse构建查询语句
		List<Column> updateColumns = new ArrayList<>();
		Column column = new Column();
		column.setColumnName("*");
		updateColumns.add(column);
		Expression whereExpression = sqlParserInfo.getWhereExpression();

		//构建 select * from user where id = ?
		Select select = JsqlParserHelper.getSelect(table, updateColumns, whereExpression);
		String selectSqlString = select.toString();


		List<Integer> whereColumnIndexs = Lists.newArrayList();
		//解析where
		TraceExpressionDeParser traceExpressionDeParser = new TraceExpressionDeParser(whereColumnIndexs);
		whereExpression.accept(traceExpressionDeParser);

		MetaObject metaObjectBoundSql = SystemMetaObject.forObject(boundSql);
		Map<String, Object> oldAdditionalParameters = (Map)metaObjectBoundSql.getValue("additionalParameters");
		Map<String, Object> additionalParameters = new HashMap();

		// 将老的值赋值到新的集合中一份(供下面直接操作新的这份数据)
		additionalParameters.putAll(oldAdditionalParameters);

		//update user set name = ?,age = ? where mobile = ? and app_id = ?  ,
		// 构建查询条件的入参 select * from user where mobile = ? and app_id = ?  ,所以mobile和app_id为入参,他们对应在update语句中对应的位置是3和4
		List<ParameterMapping> selectParamMap = Lists.newArrayList();
		whereColumnIndexs.stream().forEach(index -> {
			int mappingIndex = index-1;
			ParameterMapping parameterMapping = boundSql.getParameterMappings().get(mappingIndex);
			selectParamMap.add(parameterMapping);

			//根据属性名获取value值
			Object originValue = this.getParameterValue(selectMappedStatement.getConfiguration(), boundSql, parameterMapping);
			//将值覆盖
			additionalParameters.put(parameterMapping.getProperty(), originValue);

		});


		//构建查询语句
		BoundSql queryBoundSql = new BoundSql(selectMappedStatement.getConfiguration(), selectSqlString, selectParamMap, mybatisInvocation.getParameter());

		//进行反射
		MetaObject metaObjectNewBoundSql = SystemMetaObject.forObject(queryBoundSql);
		additionalParameters.forEach((key, value) -> {
			//将添加的参数及值加到新的newBoundSql中
			queryBoundSql.setAdditionalParameter(key,value);
		});

		//通过反射的形式设置
		metaObjectNewBoundSql.setValue("additionalParameters", additionalParameters);

		List<HashMap<String, Object>> queryResultList = mybatisInvocation.getExecutor().query(selectMappedStatement, mybatisInvocation.getParameter(), RowBounds.DEFAULT, null, null, queryBoundSql);

		return queryResultList;
	}

	private Object getParameterValue(Configuration configuration, BoundSql boundSql, ParameterMapping parameterMapping) {
		Object value = null;
		if (parameterMapping.getMode() != ParameterMode.OUT) {
			String propertyName = parameterMapping.getProperty();
			if (boundSql.hasAdditionalParameter(propertyName)) {
				value = boundSql.getAdditionalParameter(propertyName);
			} else if (boundSql.getParameterObject() == null) {
				value = null;
			} else if (configuration.getTypeHandlerRegistry().hasTypeHandler(boundSql.getParameterObject().getClass())) {
				value = boundSql.getParameterObject();
			} else {
				MetaObject metaObject = configuration.newMetaObject(boundSql.getParameterObject());
				value = metaObject.getValue(propertyName);
			}
		}

		return value;
	}

	public List<HashMap<String, Object>> queryAtInsert(MybatisInvocation mybatisInvocation, BoundSql boundSql, SqlParserInfo sqlParserInfo) throws SQLException, JSQLParserException {
		Table table = sqlParserInfo.getTable();
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		//构建一个查询的statement
		MappedStatement selectMappedStatement = MSUtils.newHashMapMappedStatement(mappedStatement);
		Statement stmt = CCJSqlParserUtil.parse("select LAST_INSERT_ID() as id");
		Select select = (Select) stmt;
		BoundSql queryBoundSql = new BoundSql(mybatisInvocation.getMappedStatement().getConfiguration(), select.toString(), null, null);
		List<HashMap<String, BigInteger>> queryLastIdResultList = mybatisInvocation.getExecutor().query(selectMappedStatement, mybatisInvocation.getParameter(), RowBounds.DEFAULT, null, null, queryBoundSql);

		List<HashMap<String, Object>> queryResultList = Lists.newArrayList();

		TraceManager traceManager = SpringApplicationContextManager.getBean(TraceManager.class);
		TablePropertyConfigDto tablePropertyConfig = traceManager.getTablePrimaryProperty(sqlParserInfo.getTableName());
		if (Objects.nonNull(tablePropertyConfig)) {
			queryLastIdResultList.stream().forEach(stringObjectHashMap -> {
				stringObjectHashMap.forEach((key, value) -> {
					List<Column> updateColumns = new ArrayList<>();
					Column column = new Column();
					column.setColumnName("*");
					updateColumns.add(column);
					EqualsTo equalsTo = new EqualsTo();
					equalsTo.setLeftExpression(new Column(tablePropertyConfig.getProEnName()));
					equalsTo.setRightExpression(new LongValue(value.longValue()));
					Select select1 = JsqlParserHelper.getSelect(table, updateColumns, equalsTo);

					try {
						BoundSql queryBoundSql1 = new BoundSql(selectMappedStatement.getConfiguration(), select1.toString(), null, null);
						List<HashMap<String, Object>> queryResultList2 = mybatisInvocation.getExecutor().query(selectMappedStatement, mybatisInvocation.getParameter(), RowBounds.DEFAULT, null, null, queryBoundSql1);
						queryResultList.addAll(queryResultList2);
					}catch (Exception e){

					}

				});
			});
		}

		return queryResultList;
	}
	
}
