package com.daoyuan.trace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2019-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TablePropertyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long tableId;

    /**
     * 是否主键
     */
    private Boolean primaryKey;

    /**
     * 是否外键
     */
    private Boolean foreignKey;

    /**
     * 依赖外键名称
     */
    @TableField("fKey_ref_property")
    private String fkeyRefProperty;

    /**
     * 依赖外键表名
     */
    @TableField("fKey_ref_table_name")
    private String fkeyRefTableName;

    /**
     * 字段英文名
     */
    private String proEnName;

    /**
     * 字段中文名
     */
    private String proCnName;

    /**
     * 是否加密字段
     */
    private Boolean encrypt;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 加密字段对应的原字段
     */
    private String refOriginProperty;

    /**
     * 数据类型
     */
    private String dataType;


}
