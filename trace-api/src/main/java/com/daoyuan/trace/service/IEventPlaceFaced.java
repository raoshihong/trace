package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.EventPlaceDto;
import com.daoyuan.trace.exception.TraceException;

public interface IEventPlaceFaced {
    void save(EventPlaceDto eventPlaceDto) throws TraceException;
}
