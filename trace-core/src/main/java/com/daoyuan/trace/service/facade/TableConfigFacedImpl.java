package com.daoyuan.trace.service.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daoyuan.trace.entity.TablePropertyConfig;
import com.daoyuan.trace.service.ITableConfigFaced;
import com.daoyuan.trace.service.ITableConfigService;
import com.daoyuan.trace.service.ITablePropertyConfigService;
import com.daoyuan.trace.dto.TableConfigDto;
import com.daoyuan.trace.dto.TablePropertyConfigDto;
import com.daoyuan.trace.entity.TableConfig;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TableConfigFacedImpl implements ITableConfigFaced {

    @Autowired
    private ITableConfigService iTableConfigService;

    @Autowired
    private ITablePropertyConfigService iTablePropertyConfigService;

    @Override
    public List<TableConfigDto> loadTableConfigs() {
        log.info("TableConfigFacedImpl.loadTableConfigs");

        List<TableConfigDto> tableConfigDtos = Lists.newArrayList();
        QueryWrapper<TableConfig> tableConfigQueryWrapper = new QueryWrapper<>();

        List<TableConfig> tableConfigs = iTableConfigService.list(tableConfigQueryWrapper);

        tableConfigs.stream().forEach(tableConfig -> {
            Long id = tableConfig.getId();
            QueryWrapper<TablePropertyConfig> tablePropertyConfigQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<TablePropertyConfig> tablePropertyConfigLambdaQueryWrapper = tablePropertyConfigQueryWrapper.lambda();
            tablePropertyConfigLambdaQueryWrapper.eq(TablePropertyConfig::getTableId,id);
            List<TablePropertyConfig> tablePropertyConfigs = iTablePropertyConfigService.list(tablePropertyConfigQueryWrapper);

            TableConfigDto tableConfigDto = new TableConfigDto();
            BeanUtils.copyProperties(tableConfig,tableConfigDto);
            List<TablePropertyConfigDto> properties = Lists.newArrayList();
            tableConfigDto.setProperties(properties);
            tablePropertyConfigs.stream().forEach(tablePropertyConfig -> {
                TablePropertyConfigDto tablePropertyConfigDto = new TablePropertyConfigDto();
                BeanUtils.copyProperties(tablePropertyConfig,tablePropertyConfigDto);
                properties.add(tablePropertyConfigDto);
            });
            tableConfigDtos.add(tableConfigDto);
        });

        return tableConfigDtos;
    }
}
