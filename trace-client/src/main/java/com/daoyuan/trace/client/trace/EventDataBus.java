package com.daoyuan.trace.client.trace;

import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.custom.GetTraceUserService;
import com.daoyuan.trace.client.custom.TraceEventUser;
import com.daoyuan.trace.client.utils.SpanContextManager;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import com.daoyuan.trace.dto.*;
import com.daoyuan.trace.service.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class EventDataBus {

    public void build(TraceData traceData){
        log.info("EventDataBus.build:{}", JSON.toJSONString(traceData));
        AbstractSpan span = SpanContextManager.get();
        EventDto eventDto = new EventDto();

        IEventFaced iEventFaced = SpringApplicationContextManager.getBean(IEventFaced.class);
        IEventUserFaced iEventUserFaced = SpringApplicationContextManager.getBean(IEventUserFaced.class);
        IEventPlaceFaced iEventPlaceFaced = SpringApplicationContextManager.getBean(IEventPlaceFaced.class);

        //保存基本事件
        eventDto.setName(traceData.getName());
        eventDto.setType(traceData.getType());
        eventDto.setSpanId(span.getSpanId());
        iEventFaced.save(eventDto);

        //保存事件用户
        try {
            GetTraceUserService getTraceEventUser = SpringApplicationContextManager.getBean(GetTraceUserService.class);
            if (Objects.nonNull(getTraceEventUser)) {
                TraceEventUser traceUser = getTraceEventUser.getTraceEventUser();
                if (Objects.nonNull(traceUser)) {
                    EventUserDto eventUserDto = new EventUserDto();
                    eventUserDto.setSpanId(span.getSpanId());
                    eventUserDto.setUserId(traceUser.getId());
                    eventUserDto.setUserName(traceUser.getName());
                    eventUserDto.setUserMobile(traceUser.getMobile());
                    eventUserDto.setUserOrigin(traceUser.getUserOrigin());
                    eventUserDto.setAppId(traceUser.getAppId());
                    iEventUserFaced.save(eventUserDto);
                }
            }
        }catch (Exception e){
            log.info("用户信息获取失败:{}",e);
        }

        EventPlaceDto eventPlaceDto = new EventPlaceDto();
        eventPlaceDto.setSpanId(span.getSpanId());
        eventPlaceDto.setPlacePageCode(traceData.getPageCode());
        eventPlaceDto.setPlacePageName(traceData.getPageName());
        eventPlaceDto.setPlacePlatformCode(traceData.getPlatformCode());
        eventPlaceDto.setPlacePlatformName(traceData.getPlatformName());
        eventPlaceDto.setPlaceUrl(traceData.getUrl());
        iEventPlaceFaced.save(eventPlaceDto);

    }

    public void buildEventContent(EventContentDto eventContentDto){
        IEventContentFaced iEventContentFaced = SpringApplicationContextManager.getBean(IEventContentFaced.class);
        iEventContentFaced.save(eventContentDto);
    }

    public void notifyFinish(){
        AbstractSpan span = SpanContextManager.get();
        TracingContextListener tracingContextListener = SpringApplicationContextManager.getBean(TracingContextListener.class);
        TraceSegment traceSegment = span.getTraceSegment();
        tracingContextListener.notifyFinish(traceSegment);
    }
}
