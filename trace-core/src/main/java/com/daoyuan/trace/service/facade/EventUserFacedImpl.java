package com.daoyuan.trace.service.facade;

import com.daoyuan.trace.entity.EventUser;
import com.daoyuan.trace.service.IEventUserFaced;
import com.daoyuan.trace.dto.EventUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventUserFacedImpl implements IEventUserFaced {
    @Autowired
    private com.daoyuan.trace.service.IEventUserService iEventUserService;
    @Override
    public void save(EventUserDto eventUserDto) {
        EventUser eventUser = new EventUser();
        BeanUtils.copyProperties(eventUserDto,eventUser);
        iEventUserService.save(eventUser);
    }
}
