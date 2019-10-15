package com.daoyuan.trace.web;

import com.daoyuan.trace.entity.Event;
import com.daoyuan.trace.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EventController {

    @Autowired
    private IEventService eventService;

    @GetMapping("/test")
    public void test(){
        Event event = new Event();
        event.setName("sdf");
        event.setSpanId("ffff");
        event.setTime(LocalDateTime.now());
        eventService.save(event);
    }

}
