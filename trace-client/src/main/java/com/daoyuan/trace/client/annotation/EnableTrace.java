package com.daoyuan.trace.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据{appId,method,uri} 坐标确定唯一
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableTrace {
    //应用的appId
    String appId();
    //自定义method
    String method();
    //自定义uri
    String uri();
}
