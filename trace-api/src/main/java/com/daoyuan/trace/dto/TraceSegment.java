package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TraceSegment implements Serializable {
    private String spanId;
    /**
     * 这个pageCode需要配置映射,映射某个主表
     */
    private String pageCode;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
