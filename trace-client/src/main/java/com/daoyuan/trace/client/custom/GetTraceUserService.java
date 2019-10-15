package com.daoyuan.trace.client.custom;

import org.springframework.stereotype.Component;

/**
 * 不同系统实现自己的返回用户信息的功能
 */
@Component
public interface GetTraceUserService {

    TraceEventUser getTraceEventUser();

}
