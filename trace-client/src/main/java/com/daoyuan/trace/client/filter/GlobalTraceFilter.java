package com.daoyuan.trace.client.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.constants.Tags;
import com.daoyuan.trace.client.trace.AbstractSpan;
import com.daoyuan.trace.client.utils.SpanContextManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER} , order = -9999)//在生产者和消费者都激活
public class GlobalTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        attachment();

        return invoker.invoke(invocation);
    }

    public void attachment(){
        try {
            //从附件中获取
            RpcContext rpcContext = RpcContext.getContext();
            boolean isConsumer = rpcContext.isConsumerSide();
            if (isConsumer) {//消费者
                AbstractSpan abstractSpan = SpanContextManager.get();
                rpcContext.getAttachments().put(Tags.SPAN_ID, JSON.toJSONString(abstractSpan));
            } else{
                AbstractSpan abstractSpan = JSON.parseObject(rpcContext.getAttachment(Tags.SPAN_ID), AbstractSpan.class);
                SpanContextManager.set(abstractSpan);
            }
        }catch (Exception e){
            log.info("dubbo过滤器解析附件异常:{}",e);
        }
    }

}
