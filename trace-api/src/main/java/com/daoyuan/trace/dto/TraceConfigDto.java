package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TraceConfigDto implements Serializable {
    /**
     * 事件名
     */
    private String name;
    /**
     * 发生地
     */
    private String pageCode;
    /**
     * 发生地名称
     */
    private String pageName;
    /**
     * 系统(根据系统来获取对应的用户id)
     */
    private String platformCode;

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
     * 请求地址
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
