package com.daoyuan.trace.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

public interface DynamicSqlMapper {
    List<LinkedHashMap<String,Object>> executeQuerySql(@Param("sql") String sql);
}
