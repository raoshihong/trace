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
 * 事件用户体系表
 * </p>
 *
 * @author 
 * @since 2019-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("event_user")
public class EventUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件用户记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件id
     */
    private String spanId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userMobile;

    /**
     * 用户来源
     */
    private String userOrigin;

    /**
     * 商户id
     */
    private Integer appId;

}
