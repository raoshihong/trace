package com.daoyuan.trace.service.facade;

import com.daoyuan.trace.entity.EventPlace;
import com.daoyuan.trace.service.IEventPlaceFaced;
import com.daoyuan.trace.dto.EventPlaceDto;
import com.daoyuan.trace.service.IEventPlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventPlaceFacedImpl implements IEventPlaceFaced {
    @Autowired
    private IEventPlaceService iEventPlaceService;
    @Override
    public void save(EventPlaceDto eventPlaceDto) {
        EventPlace eventPlace = new EventPlace();
        BeanUtils.copyProperties(eventPlaceDto,eventPlace);
        iEventPlaceService.save(eventPlace);
    }
}
