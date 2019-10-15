package com.daoyuan.trace.client.manager;

import com.daoyuan.trace.dto.TableConfigDto;
import com.daoyuan.trace.dto.TablePropertyConfigDto;
import com.daoyuan.trace.dto.TraceConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TraceManager {
    @Autowired
    private TableConfigContext tableConfigContext;

    @Autowired
    private TraceConfigContext traceConfigContext;

    public TableConfigDto getTableConfig(String tableName){
        return tableConfigContext.getTableConfigByTableName(tableName);
    }

    public TablePropertyConfigDto getTablePrimaryProperty(String tableName){
        return tableConfigContext.getTableConfigPrimaryKeyByTableName(tableName);
    }

    public TablePropertyConfigDto getTablePropertyConfig(String tableName, String propertyName){
        return tableConfigContext.getTablePropertyConfigByTableNameAndProperty(tableName,propertyName);
    }

    public TraceConfigDto getTraceConfig(String appId, String uri, String method){
        return traceConfigContext.getTraceConfigByAppIdAndUriAndMethod(appId,uri,method);
    }

}
