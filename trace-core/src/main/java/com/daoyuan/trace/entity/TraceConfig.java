package com.daoyuan.trace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TraceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 页面代码
     */
    private String pageCode;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 平台代码
     */
    private String platformCode;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 事件类型
     */
    private String type;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求地址
     */
    private String url;


}
