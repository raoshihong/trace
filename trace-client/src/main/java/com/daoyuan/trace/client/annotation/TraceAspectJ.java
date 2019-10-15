package com.daoyuan.trace.client.annotation;

import com.daoyuan.trace.client.manager.TraceManager;
import com.daoyuan.trace.dto.TraceConfigDto;
import com.daoyuan.trace.dto.TraceSegment;
import com.google.common.base.Splitter;
import com.daoyuan.trace.client.trace.AbstractSpan;
import com.daoyuan.trace.client.trace.EventDataBus;
import com.daoyuan.trace.client.trace.TraceData;
import com.daoyuan.trace.client.utils.SpanContextManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Slf4j
@Aspect
public class TraceAspectJ {

    private static final String REGXSTR = "@(com.daoyuan.trace.client.annotation.EnableTrace || org.springframework.web.bind.annotation.GetMapping || org.springframework.web.bind.annotation.PostMapping || org.springframework.web.bind.annotation.PutMapping || org.springframework.web.bind.annotation.DeleteMapping || org.springframework.web.bind.annotation.PatchMapping || org.springframework.web.bind.annotation.RequestMapping)";

    @Autowired
    private TraceManager traceManager;

    //拦截所有@PostMapping,@GetMapping,@RequestMapping,带有自定义EnableTrace的
    @Around("execution("+REGXSTR+" * *(..)))")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable{

        Object[] args = pjp.getArgs();
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();

        proceedBefore(method);

        //执行目标方法
        return  pjp.proceed(args);
    }

    private String getAppId(String contextPath){
        return Splitter.on("/").omitEmptyStrings().splitToList(contextPath).get(0);
    }

    private boolean hasMapping(Method method){
        if (method.isAnnotationPresent(GetMapping.class)
                || method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(PutMapping.class)
                || method.isAnnotationPresent(DeleteMapping.class)
                || method.isAnnotationPresent(PatchMapping.class)
                || method.isAnnotationPresent(RequestMapping.class)) {
            return true;
        }
        return false;
    }

    private void proceedBefore(Method method){
        if (hasMapping(method)) {

            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = servletRequestAttributes.getRequest();
            String appId = getAppId(request.getContextPath());
            String uri = request.getRequestURI();
            String requestMethod = request.getMethod().toUpperCase();
            //根据appId,uri,requestMethod获取traceConfig
            startTrace(appId,uri,requestMethod);
        } else if (method.isAnnotationPresent(EnableTrace.class)) {//普通方法上
            EnableTrace enableTrace = method.getAnnotation(EnableTrace.class);
            String appId = enableTrace.appId();
            String uri = enableTrace.uri();
            String requestMethod = enableTrace.method();
            startTrace(appId,uri,requestMethod);
        }
    }

    private void startTrace(String appId,String uri,String requestMethod){
        log.info("TraceAspectJ.startTrace appId:{},uri:{},requestMethod:{}",appId,uri,requestMethod);
        try {
            TraceConfigDto traceConfig = traceManager.getTraceConfig(appId,uri,requestMethod);
            //配置了这个地址的就进行收集
            if (Objects.nonNull(traceConfig)) {
                AbstractSpan abstractSpan = SpanContextManager.createSpan();

                // 事件开始
                TraceSegment traceSegment = new TraceSegment();
                traceSegment.setSpanId(abstractSpan.getSpanId());
                traceSegment.setPageCode(traceConfig.getPageCode());
                abstractSpan.setTraceSegment(traceSegment);

                TraceData traceData = new TraceData();
                BeanUtils.copyProperties(traceConfig,traceData);
                traceData.setUrl(uri);

                EventDataBus eventDataBus = new EventDataBus();
                eventDataBus.build(traceData);
            }
        }catch (Exception e){
            log.info("链路开启异常:{}",e);
        }
    }

}
