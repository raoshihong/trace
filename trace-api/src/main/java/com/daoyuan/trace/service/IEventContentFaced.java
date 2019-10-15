package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.EventContentDto;
import com.daoyuan.trace.exception.TraceException;

public interface IEventContentFaced {
    void save(EventContentDto eventContentDto) throws TraceException;
}
