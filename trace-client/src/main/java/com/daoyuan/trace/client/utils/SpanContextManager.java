package com.daoyuan.trace.client.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.daoyuan.trace.client.trace.AbstractSpan;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class SpanContextManager {
    private static final TransmittableThreadLocal<AbstractSpan> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(AbstractSpan value){
        THREAD_LOCAL.set(value);
    }

    public static AbstractSpan get(){
        return THREAD_LOCAL.get();
    }

    public static AbstractSpan createSpan(){
        AbstractSpan abstractSpan = new AbstractSpan();
        abstractSpan.setSpanId(UUID.randomUUID().toString());
        SpanContextManager.set(abstractSpan);
        log.info("span start spanId:{}",abstractSpan.getSpanId());
        return abstractSpan;
    }

    public static void stopSpan(){
        THREAD_LOCAL.remove();
    }

}
