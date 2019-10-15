package com.daoyuan.trace.service.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daoyuan.trace.entity.TraceConfig;
import com.daoyuan.trace.service.ITraceConfigFaced;
import com.daoyuan.trace.service.ITraceConfigService;
import com.daoyuan.trace.dto.TraceConfigDto;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TraceConfigFacedImpl implements ITraceConfigFaced {
    @Autowired
    private ITraceConfigService iTraceConfigService;

    @Override
    public List<TraceConfigDto> loadTraceConfigs() {
        log.info("TableConfigFacedImpl.loadTraceConfigs");
        QueryWrapper<TraceConfig> queryWrapper = new QueryWrapper<>();
        List<TraceConfig> traceConfigs = iTraceConfigService.list(queryWrapper);
        List<TraceConfigDto> traceConfigDtos = Lists.newArrayList();
        traceConfigs.stream().forEach(traceConfig -> {
            TraceConfigDto traceConfigDto = new TraceConfigDto();
            BeanUtils.copyProperties(traceConfig,traceConfigDto);
            traceConfigDtos.add(traceConfigDto);
        });
        return traceConfigDtos;
    }
}
