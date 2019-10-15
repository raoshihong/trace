package com.daoyuan.trace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.trace.entity.EventContent;
import com.daoyuan.trace.mapper.EventContentMapper;
import com.daoyuan.trace.service.IEventContentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件操作内容记录表 服务实现类
 * </p>
 *
 * @author 
 */
@Service
public class EventContentServiceImpl extends ServiceImpl<EventContentMapper, EventContent> implements IEventContentService {

}
