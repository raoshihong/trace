package com.daoyuan.trace.client.interceptors.mybatis;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MybatisParameterUtils {
	/**
     * 获取更新语句中的用户入参字段
	 * @param mappedStatement
     * @param boundSql
     * @param updateParameterObject
     * @return
     */
	public static Map<String, Object> getParameter(MappedStatement mappedStatement, BoundSql boundSql, Object updateParameterObject){
		Configuration configuration = mappedStatement.getConfiguration();
		//获取sql中传递的参数
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Map<String, Object> paramMap = new HashMap<>();
		if(mappedStatement.getStatementType() == StatementType.PREPARED){
			if (parameterMappings != null) {
			      for (int i = 0; i < parameterMappings.size(); i++) {
			    	  ParameterMapping parameterMapping = parameterMappings.get(i);
			    	  if (parameterMapping.getMode() != ParameterMode.OUT) {
			    		  Object value;
			              String propertyName = parameterMapping.getProperty();
			              String finalPropertyName = propertyName;
			              if (boundSql.hasAdditionalParameter(propertyName)) { 
			                  value = boundSql.getAdditionalParameter(propertyName);
			                } else if (updateParameterObject == null) {
			                  value = null;
			                } else if (mappedStatement.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(updateParameterObject.getClass())) {
			                  value = updateParameterObject;
			                } else {
			                  MetaObject metaObject = configuration.newMetaObject(updateParameterObject);
			                  value = metaObject.getValue(propertyName);
			                  if (propertyName.indexOf(".") > - 1) {
			                	  finalPropertyName = propertyName.substring(propertyName.indexOf(".")+1);
			                  }
			                }
			              
			              paramMap.put(finalPropertyName, value);
			    	  }
			      }
			}
		}
		return paramMap;
	}
}

