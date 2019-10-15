package com.daoyuan.trace.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EventPlaceDto implements Serializable {

    private Long id;

    /**
     * 链路标识
     */
    private String spanId;

    private String placePageCode;

    private String placePageName;

    private String placePlatformCode;

    private String placePlatformName;

    private String placeUrl;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
