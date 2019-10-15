package com.daoyuan.trace.client.manager;

import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import com.daoyuan.trace.dto.TraceConfigDto;
import com.daoyuan.trace.service.ITraceConfigFaced;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TraceConfigContext {

    @Autowired
    private ITraceConfigFaced iTraceConfigFaced;

    private List<TraceConfigDto> traceConfigs = Lists.newArrayList();
    private Lock lock = new ReentrantLock();

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1,r -> {
        Thread t = new Thread(r,"TraceConfigScheduler");
        t.setDaemon(true);
        return t;
    });

    @PostConstruct
    public void init(){
        log.info("trace config init");
        loadConfigAndSetCache();
        refreshConfigAndSetCache();
    }

    private void loadConfigAndSetCache(){
        try {
            lock.lockInterruptibly();
            log.info("TraceConfigContext.loadConfigAndSetCache");
            traceConfigs = iTraceConfigFaced.loadTraceConfigs();
            log.info("加载到的traceConfigs:{}", JSON.toJSONString(traceConfigs));
        }catch (Exception e){
            log.info("加载TraceConfig配置失败,{}",e);
//            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    private void refreshConfigAndSetCache(){
        executorService.scheduleWithFixedDelay(()->{
            loadConfigAndSetCache();
        },1,5, TimeUnit.MINUTES);
    }

    public TraceConfigDto getTraceConfigByAppIdAndUriAndMethod(String appId, String uri, String method){
        if (!CollectionUtils.isEmpty(traceConfigs)) {
            Optional<TraceConfigDto> traceConfigOptional = traceConfigs.stream().filter(traceConfig -> traceConfig.getAppId().equals(appId) && traceConfig.getMethod().equals(method) && uri.contains(traceConfig.getUrl())).findAny();
            if (traceConfigOptional.isPresent()) {
                return traceConfigOptional.get();
            }
        }

        return null;
    }

}
