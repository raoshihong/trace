package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 对整个表的描述
 */
@Getter
@Setter
public class TableConfigDto implements Serializable {
    /**
     * 应用id
     */
    private String appId;
    /**
     * 表名名称
     */
    private String tableName;
    /**
     * 表的描述
     */
    private String tableNameDesc;

    /**
     * 表的属性的映射
     */
    private List<TablePropertyConfigDto> properties;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
