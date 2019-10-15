package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class EventContentDto implements Serializable {
    private Long id;
    private String spanId;

    private Long dataId;

    private String dataName;

    /**
     * 数据操作类型update,insert,delete
     */
    private String dataActionType;

    /**
     * 修改字段英文名
     */
    private String keyEn;

    /**
     * 修改字段中文名
     */
    private String keyZh;

    /**
     * 修改前的值
     */
    private String olValue;

    /**
     * 修改后的值
     */
    private String neValue;


    private String tableName;

    private String tableNameDesc;

    private Long recordId;

    private String dataTableName;

    private String beforeBody;
    private String afterBody;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
