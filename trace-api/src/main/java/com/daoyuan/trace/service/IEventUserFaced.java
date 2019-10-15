package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.EventUserDto;
import com.daoyuan.trace.exception.TraceException;

public interface IEventUserFaced {
    void save(EventUserDto eventUserDto) throws TraceException;
}
