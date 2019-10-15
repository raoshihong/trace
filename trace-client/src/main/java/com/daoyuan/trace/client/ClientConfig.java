package com.daoyuan.trace.client;

import com.daoyuan.trace.client.manager.TraceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ImportResource(locations = {"classpath:dubbo/lib-dubbo-consumer.xml"})
@EnableAspectJAutoProxy
@Slf4j
public class ClientConfig {

    public ClientConfig() {
        log.info("init config");
    }

    @Bean
    public TraceManager traceManager(){
        return new TraceManager();
    }
}
