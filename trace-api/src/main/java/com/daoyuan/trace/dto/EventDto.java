package com.daoyuan.trace.dto;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EventDto implements BaseEvent, Serializable {
    private Long id;
    private String spanId;
    private String name;
    private EventUserDto user;
    private Date time;
    private EventPlaceDto place;
    private String type;
    private List<EventContentDto> contents;//一个事件里同时操作多个数据源

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
