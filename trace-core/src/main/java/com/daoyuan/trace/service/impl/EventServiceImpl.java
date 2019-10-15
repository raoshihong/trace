package com.daoyuan.trace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.trace.entity.Event;
import com.daoyuan.trace.mapper.EventMapper;
import com.daoyuan.trace.service.IEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件记录总表 服务实现类
 * </p>
 *
 * @author 
 * @since 2019-09-05
 */

@Slf4j
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {


}
