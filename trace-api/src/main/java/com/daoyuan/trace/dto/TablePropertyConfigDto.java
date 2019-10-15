package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TablePropertyConfigDto implements Serializable {
    /**
     * 是否是主键
     */
    private Boolean primaryKey;
    /**
     * 是否是外键
     */
    private Boolean foreignKey;
    /**
     * 外键对应的主表的字段
     */
    private String fKeyRefProperty;
    /**
     * 外键对应的主表
     */
    private String fKeyRefTableName;
    /**
     * 字段英文名
     */
    private String proEnName;
    /**
     * 字段中文名
     */
    private String proCnName;

    /**
     * 是否是加密字段
     */
    private Boolean encrypt;

    /**
     * 加密字段对应的原字段
     */
    private String refOriginProperty;

    /**
     * 字段类型,对应数据库字段类型映射到java中的类型 String,Long,Date
     */
    private String type;

    /**
     * 数据类型,address,状态
     */
    private String dataType;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
