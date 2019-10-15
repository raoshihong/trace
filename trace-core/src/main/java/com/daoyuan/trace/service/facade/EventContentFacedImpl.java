package com.daoyuan.trace.service.facade;

import com.daoyuan.trace.service.IEventContentService;
import com.daoyuan.trace.dto.EventContentDto;
import com.daoyuan.trace.entity.EventContent;
import com.daoyuan.trace.exception.TraceException;
import com.daoyuan.trace.service.IEventContentFaced;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventContentFacedImpl implements IEventContentFaced {

    @Autowired
    private IEventContentService iEventContentService;

    @Override
    public void save(EventContentDto eventContentDto)throws TraceException {
        EventContent eventContent = new EventContent();
        BeanUtils.copyProperties(eventContentDto,eventContent);
        iEventContentService.save(eventContent);
    }

}
