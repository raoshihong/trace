package com.daoyuan.trace.service.facade;

import com.daoyuan.trace.service.IEventService;
import com.daoyuan.trace.dto.EventDto;
import com.daoyuan.trace.entity.Event;
import com.daoyuan.trace.service.IEventFaced;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class EventFacedImpl implements IEventFaced {

    @Autowired
    private IEventService iEventService;

    @Override
    public void save(EventDto eventDto) {
        Event event = new Event();
        BeanUtils.copyProperties(eventDto,event);
        event.setTime(LocalDateTime.now());
        //保存事件
        iEventService.save(event);
    }

}
