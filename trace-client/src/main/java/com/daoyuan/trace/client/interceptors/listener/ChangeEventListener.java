package com.daoyuan.trace.client.interceptors.listener;

import com.daoyuan.trace.client.interceptors.enums.DBActionTypeEnum;
import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;
import com.daoyuan.trace.client.interceptors.mybatis.MybatisInvocation;
import com.daoyuan.trace.client.utils.SpringApplicationContextManager;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ChangeEventListener {

	public ChangeEventListener() {
	}

	private static ChangeEventListener factory = null;

	public static ChangeEventListener getInstance() {
		if (factory == null) {
			factory = new ChangeEventListener();
		}
		return factory;
	}

	public void listenModification(MybatisInvocation mybatisInvocation, List<ChangeEntity> changeDatas) {
		DoChangeEventListener doChangeEventListener = SpringApplicationContextManager.getBean("doChangeEventListener",DoChangeEventListener.class);
		String commandName = mybatisInvocation.getMappedStatement().getSqlCommandType().name();
		if (StringUtils.equals(DBActionTypeEnum.UPDATE.getValue(), commandName)) {
			doChangeEventListener.onPostUpdate(changeDatas);
		} else if (StringUtils.equals(DBActionTypeEnum.INSERT.getValue(), commandName)) {
			doChangeEventListener.onPostInsert(changeDatas);
		} else if (StringUtils.equals(DBActionTypeEnum.DELETE.getValue(), commandName)) {
			doChangeEventListener.onPostDelete(changeDatas);
		}
	}

}
