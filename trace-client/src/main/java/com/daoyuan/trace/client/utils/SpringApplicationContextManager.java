package com.daoyuan.trace.client.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringApplicationContextManager implements ApplicationContextAware {

    private static ApplicationContext ctxt;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctxt = applicationContext;
    }

    public static Object getBean(String beanName){
        return ctxt.getBean(beanName);
    }

    public static  <T> T getBean(Class<T> clazz){
        return ctxt.getBean(clazz);
    }

    public static <T> T getBean(String beanName,Class<T> clazz){
        return ctxt.getBean(beanName,clazz);
    }
}
