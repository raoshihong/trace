package com.daoyuan.trace.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.trace.entity.EventUser;
import com.daoyuan.trace.mapper.EventUserMapper;
import com.daoyuan.trace.service.IEventUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事件用户体系表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class EventUserServiceImpl extends ServiceImpl<EventUserMapper, EventUser> implements IEventUserService {

}
