package com.daoyuan.trace.client.interceptors.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.daoyuan.trace.client.interceptors.enums.DBActionTypeEnum;
import com.daoyuan.trace.client.manager.TraceManager;
import com.daoyuan.trace.client.trace.AbstractSpan;
import com.daoyuan.trace.client.trace.EventDataBus;
import com.daoyuan.trace.client.utils.SpanContextManager;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeColumn;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import com.daoyuan.trace.dto.EventContentDto;
import com.daoyuan.trace.dto.TableConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service("doChangeEventListener")
public class DoChangeEventListener implements DoInsertEventListener, DoDeleteEventListener, DoUpdateEventListener {

	@Autowired
	private TraceManager traceManager;

	@Override
	public void onPostDelete( List<ChangeEntity> changeTable) {
		for (ChangeEntity changeEntity : changeTable) {
			storeChangeEntity(changeEntity, DBActionTypeEnum.DELETE);
		}
	}

	@Override
	public void onPostInsert(List<ChangeEntity> changeTable) {
		for (ChangeEntity changeEntity : changeTable) {
			storeChangeEntity(changeEntity,DBActionTypeEnum.INSERT);
		}
	}

	@Override
	public void onPostUpdate(List<ChangeEntity> changeTable) {
		for (ChangeEntity changeEntity : changeTable) {
			storeChangeEntity(changeEntity,DBActionTypeEnum.UPDATE);
		}
	}

	private void storeChangeEntity(ChangeEntity changeEntity, DBActionTypeEnum actionTypeEnum){
		log.info("DoChangeEventListener.storeChangeEntity changeEntity:{}",JSON.toJSONString(changeEntity));

		boolean change = false;
		if (DBActionTypeEnum.INSERT.getValue().equals(actionTypeEnum.getValue())) {
			change = true;
		}else if(DBActionTypeEnum.DELETE.getValue().equals(actionTypeEnum.getValue())){
			change = true;
		}else{
			for(ChangeColumn beforeColumn : changeEntity.getBeforeColumnList()){
				Optional<ChangeColumn> afterComunOptional = changeEntity.getAfterColumnList().stream().filter(changeColumn -> changeColumn.getName().equals(beforeColumn.getName())).findAny();
				if (afterComunOptional.isPresent()) {
					ChangeColumn afterComun = afterComunOptional.get();
					String olValue = Objects.toString(beforeColumn.getValue(),"");
					String neValue = Objects.toString(afterComun.getValue(),"");
					if (!"updated_at".equals(beforeColumn.getName()) && !Objects.equals(olValue,neValue)) {
						change = true;
					}
				}else{
					change = true;
				}
			}
		}

		//数据有变更,则记录
		if(!change){
			return;
		}
		try {
			TableConfigDto tableConfig = traceManager.getTableConfig(changeEntity.getTableName());
			Long entityId = changeEntity.getEntityId();
			String tableName = changeEntity.getTableName();
			if (Objects.nonNull(tableConfig)) {

				AbstractSpan abstractSpan = SpanContextManager.get();
				if (Objects.nonNull(abstractSpan)) {
					EventContentDto eventContentDto = new EventContentDto();
					eventContentDto.setSpanId(abstractSpan.getSpanId());
					eventContentDto.setDataActionType(actionTypeEnum.getValue());
					eventContentDto.setTableNameDesc(tableConfig.getTableNameDesc());
					eventContentDto.setTableName(tableName);
					eventContentDto.setRecordId(entityId);
					eventContentDto.setBeforeBody(JSON.toJSONString(changeEntity.getBeforeColumnList(), SerializerFeature.WriteMapNullValue));
					eventContentDto.setAfterBody(JSON.toJSONString(changeEntity.getAfterColumnList(), SerializerFeature.WriteMapNullValue));

					EventDataBus eventData = new EventDataBus();
					eventData.buildEventContent(eventContentDto);
				}
			}
		} catch (Exception e) {
			log.info("保存前后修改数据失败:{}",e);
		}
	}

}
