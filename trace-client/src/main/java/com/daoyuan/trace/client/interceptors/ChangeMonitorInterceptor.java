package com.daoyuan.trace.client.interceptors;

import com.daoyuan.trace.client.interceptors.listener.ChangeEventListener;
import com.daoyuan.trace.client.interceptors.parse.ParseFactory;
import com.daoyuan.trace.client.interceptors.condition.TraceMybatisCondition;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import com.daoyuan.trace.client.interceptors.parse.DataPaser;
import com.daoyuan.trace.client.trace.AbstractSpan;
import com.daoyuan.trace.client.trace.EventDataBus;
import com.daoyuan.trace.client.utils.SpanContextManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class,Object.class})
})
@Conditional(TraceMybatisCondition.class)
@Component
public class ChangeMonitorInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("ChangeMonitorInterceptor.intercept");
        //获取拦截目标
        Object target = realTarget(invocation.getTarget());

        Object result = null;
        if (target instanceof Executor) {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];

            MybatisInvocation mybatisInvocation = new MybatisInvocation(args, ms, parameter, (Executor) target);

            //在执行sql操作前,原数据的处理
            List<ChangeEntity> changeTable = beforeProceed(mybatisInvocation);
            //mybatis的所有操作都是交给Executor去执行的,Executor.update就是用来指向insert,delete,update方法的
            //SimpleExecutor.update -> BaseExecutor.doUpdate -> StatementHandler.update ->PreparedStatementHandler.update -> PreparedStatement.execute
            // 执行Update方法，除了查询之外的Insert，Delete，Update都是属于Update方法
            result = invocation.proceed();

            //执行完sql操作后,的数据处理
            afterProceed(mybatisInvocation,changeTable);

        }

        return result;
    }

    /**
     * 方法执行之前解析旧数据
     * @param mybatisInvocation
     */
    public List<ChangeEntity> beforeProceed(MybatisInvocation mybatisInvocation){
        log.info("ChangeMonitorInterceptor.beforeProceed");
        try {
            AbstractSpan abstractSpan = SpanContextManager.get();
            if (Objects.nonNull(abstractSpan)) {
                //根据sql命令名称创建相应数据解析器
                DataPaser dataPaser = ParseFactory.getInstance().creator(mybatisInvocation.getMappedStatement().getSqlCommandType().name());
                return dataPaser.parseBefore(mybatisInvocation);
            }
        } catch (Throwable t){
            //捕获异常,不影响目标应用的更新语句
            log.warn("ChangeMonitorInterceptor.intercept before update exception:{}",t);
        }
        return null;
    }

    public void afterProceed(MybatisInvocation mybatisInvocation, List<ChangeEntity> changeTable){

        log.info("ChangeMonitorInterceptor.afterProceed");
        try {
            AbstractSpan abstractSpan = SpanContextManager.get();
            if (Objects.nonNull(abstractSpan)) {
                // 方法执行之后处理数据
                DataPaser dataPaser = ParseFactory.getInstance().creator(mybatisInvocation.getMappedStatement().getSqlCommandType().name());
                changeTable = dataPaser.parseAfter(mybatisInvocation, changeTable);

                if (changeTable != null) {
                    ChangeEventListener changeEventListener = ChangeEventListener.getInstance();
                    changeEventListener.listenModification(mybatisInvocation, changeTable);
                }

                //TODO 这里待优化,每次会重复通知
                EventDataBus eventDataBus = new EventDataBus();
                eventDataBus.notifyFinish();
            }
        } catch (Throwable t){
            //捕获异常,不影响目标应用的更新语句
            log.warn("ChangeMonitorInterceptor.intercept before update exception:{}",t);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private static Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        } else {
            return target;
        }
    }
}
