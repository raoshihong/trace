package com.daoyuan.trace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 事件操作内容记录表
 * </p>
 *
 * @author 
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("event_content")
public class EventContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件内容记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件id
     */
    private String spanId;

    /**
     * 操作数据唯一标识id
     */
    private Long dataId;

    /**
     * 操作数据标识名称
     */
    private String dataName;

    /**
     * 数据操作类型update,insert,delete
     */
    private String dataActionType;

    /**
     * 操作表名
     */
    private String tableName;

    /**
     * 表名描述
     */
    private String tableNameDesc;

    /**
     * 操作记录id
     */
    private Long recordId;

    /**
     * 主表名
     */
    private String dataTableName;

    /**
     * 修改前的实体
     */
    private String beforeBody;

    /**
     * 修改后的实体
     */
    private String afterBody;


}
