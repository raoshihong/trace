package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.EventDto;
import com.daoyuan.trace.exception.TraceException;

public interface IEventFaced {
    void save(EventDto eventDto) throws TraceException;
}
