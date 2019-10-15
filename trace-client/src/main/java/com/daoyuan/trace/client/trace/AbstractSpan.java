package com.daoyuan.trace.client.trace;


import com.daoyuan.trace.dto.TraceSegment;

public class AbstractSpan {
    /**
     * 链路id
     */
    private String spanId;

    private TraceSegment traceSegment;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public TraceSegment getTraceSegment() {
        return traceSegment;
    }

    public void setTraceSegment(TraceSegment traceSegment) {
        this.traceSegment = traceSegment;
    }
}
