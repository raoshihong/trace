package com.daoyuan.trace.service;


import com.daoyuan.trace.dto.TraceConfigDto;
import com.daoyuan.trace.exception.TraceException;

import java.util.List;

public interface ITraceConfigFaced {
    List<TraceConfigDto> loadTraceConfigs() throws TraceException;
}
