package com.daoyuan.trace.service.facade;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daoyuan.trace.entity.EventContent;
import com.daoyuan.trace.service.TracingContextListener;
import com.daoyuan.trace.dto.TraceSegment;
import com.daoyuan.trace.service.IEventContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TracingContextListenerImpl implements TracingContextListener {

    @Autowired
    private IEventContentService iEventContentService;

//    @Autowired
//    private DynamicSqlMapper dynamicSqlMapper;

    @Async
    @Override
    public void notifyFinish(TraceSegment traceSegment) {
        log.info("TracingContextListenerImpl.notifyFinish traceSegment:{}",JSON.toJSONString(traceSegment));
        //TODO 可以根据traceSegment 来处理不同的监听数据
        //根据pageCode处理EventDataContent的数据

        String spanId = traceSegment.getSpanId();
        QueryWrapper<EventContent> contentQueryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<EventContent> lambdaQueryWrapper = contentQueryWrapper.lambda();

        //将这条链路的所有变更数据都取出来
        lambdaQueryWrapper.eq(EventContent::getSpanId, spanId);

//        List<EventContent> eventContents = iEventContentService.list(contentQueryWrapper);

//        eventContents.stream().forEach(eventContent -> {
//            if (Objects.nonNull(eventContent.getTableName())) {
//                PageTableShowColumnConfig pageTableShowColumnConfig = PageTableShowColumnConfigUitls.getPageTableShowColumnConfigByTableName(eventContent.getTableName());
//                if (Objects.nonNull(pageTableShowColumnConfig)) {
//                    //取出变更的实体数据
//                    String body = eventContent.getAfterBody();
//                    if (DBActionTypeEnum.DELETE.getValue().equals(eventContent.getDataActionType())) {
//                        body = eventContent.getBeforeBody();
//                    }
//                    List<ChangeColumn> changeColumns = JSON.parseArray(body, ChangeColumn.class);
//
//                    changeColumns.stream().forEach(changeColumn -> {
//                        if ("id".equals(changeColumn.getName())) {//TODO 有些表设计不规范,可能主键名不为id
//                            String dataId = Objects.toString(changeColumn.getValue(),null);
//                            eventContent.setDataId(Objects.isNull(dataId)?null: Long.valueOf(dataId));
//                        }
//                        if (pageTableShowColumnConfig.getShowProperty().equals(changeColumn.getName())) {
//                            eventContent.setDataName(Objects.toString(changeColumn.getValue(),null));
//                        }
//
//                    });
//
//                    eventContent.setDataTableName(eventContent.getTableName());
//                    iEventContentService.updateById(eventContent);
//                }
//            }
//        });

    }
}
