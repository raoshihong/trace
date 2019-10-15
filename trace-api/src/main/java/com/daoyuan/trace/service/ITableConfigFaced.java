package com.daoyuan.trace.service;

import com.daoyuan.trace.dto.TableConfigDto;
import com.daoyuan.trace.exception.TraceException;

import java.util.List;

public interface ITableConfigFaced {
    List<TableConfigDto> loadTableConfigs() throws TraceException;
}
