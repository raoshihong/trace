package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.TraceSegment;
import com.daoyuan.trace.exception.TraceException;

public interface TracingContextListener {
    void notifyFinish(TraceSegment traceSegment) throws TraceException;
}
