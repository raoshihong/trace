package com.daoyuan.trace.client.interceptors.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

public class TraceMybatisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return ClassUtils.isPresent("org.apache.ibatis.session.SqlSessionFactory", this.getClass().getClassLoader());
    }
}
