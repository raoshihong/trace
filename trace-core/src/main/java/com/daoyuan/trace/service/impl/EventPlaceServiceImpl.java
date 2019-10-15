package com.daoyuan.trace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.trace.entity.EventPlace;
import com.daoyuan.trace.mapper.EventPlaceMapper;
import com.daoyuan.trace.service.IEventPlaceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件发生地记录表 服务实现类
 * </p>
 *
 * @author 
 */
@Service
public class EventPlaceServiceImpl extends ServiceImpl<EventPlaceMapper, EventPlace> implements IEventPlaceService {

}
