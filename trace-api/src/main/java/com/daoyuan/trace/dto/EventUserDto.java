package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EventUserDto implements Serializable {
    private Long id;

    /**
     * 链路标识
     */
    private String spanId;

    private Long userId;

    private String userName;

    private String userMobile;

    private String userOrigin;

    private String appCode;

    private Integer appId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
