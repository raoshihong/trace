package com.daoyuan.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e) {
            log.error("Start FAIL.", e);
        }
    }
}
