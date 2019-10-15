package com.daoyuan.trace.client.manager;

import com.alibaba.fastjson.JSON;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import com.daoyuan.trace.dto.TableConfigDto;
import com.daoyuan.trace.dto.TablePropertyConfigDto;
import com.daoyuan.trace.service.ITableConfigFaced;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TableConfigContext {

    @Autowired
    private ITableConfigFaced iTableConfigFaced;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, r -> {
        Thread thread = new Thread(r,"TableConfigScheduler");
        thread.setDaemon(true);
        return thread;
    });

    private Map<String, TableConfigDto> cacheTableConfigs = Maps.newConcurrentMap();

    private Lock lock = new ReentrantLock();

    @PostConstruct
    public void init(){
        log.info("table config init");
        loadConfigAndSetCache();
        refreshConfigAndSetCache();
    }

    private void loadConfigAndSetCache(){
        try{
            lock.lockInterruptibly();
            log.info("TableConfigContext.loadConfigAndSetCache");
            //加载配置,并缓存到jvm内存中
            List<TableConfigDto> tableConfigs = iTableConfigFaced.loadTableConfigs();
            log.info("加载到的tableConfigs:{}", JSON.toJSONString(tableConfigs));
            cacheTableConfigs = tableConfigs.stream().collect(Collectors.toMap(TableConfigDto::getTableName, tableConfig -> tableConfig));
        }catch (Exception e){
            log.info("加载TableConfig配置失败,{}",e);
//            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    private void refreshConfigAndSetCache(){
        executorService.scheduleWithFixedDelay(()->{
            loadConfigAndSetCache();
        },1,5,TimeUnit.MINUTES);
    }

    public TableConfigDto getTableConfigByTableName(String tableName){
        return cacheTableConfigs.get(tableName);
    }

    public TablePropertyConfigDto getTableConfigPrimaryKeyByTableName(String tableName){
        TableConfigDto tableConfig = cacheTableConfigs.get(tableName);
        if (Objects.nonNull(tableConfig)) {
            //查找表中属性配置为primaryKey=true的属性
            Optional<TablePropertyConfigDto> tablePropertyConfigOptional = tableConfig.getProperties().stream().filter(tablePropertyConfig -> Boolean.TRUE.equals(tablePropertyConfig.getPrimaryKey())).findAny();
            if (tablePropertyConfigOptional.isPresent()) {
                return tablePropertyConfigOptional.get();
            }
            return null;
        }
        return null;
    }

    public TablePropertyConfigDto getTablePropertyConfigByTableNameAndProperty(String tableName, String propertyName){
        TableConfigDto tableConfig = cacheTableConfigs.get(tableName);
        if (Objects.nonNull(tableConfig)) {
            List<TablePropertyConfigDto> properties = tableConfig.getProperties();
            Optional<TablePropertyConfigDto> optionalTablePropertyConfig = properties.stream().filter(tablePropertyConfig -> tablePropertyConfig.getProEnName().equals(propertyName)).findAny();

            if (optionalTablePropertyConfig.isPresent()) {
                return optionalTablePropertyConfig.get();
            }
            return null;
        }
        return null;
    }
}
