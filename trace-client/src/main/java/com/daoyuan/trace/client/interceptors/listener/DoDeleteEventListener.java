package com.daoyuan.trace.client.interceptors.listener;

import com.daoyuan.trace.client.interceptors.mybatis.ChangeEntity;

import java.util.List;

public interface DoDeleteEventListener {

	void onPostDelete(List<ChangeEntity> changeTable);
}
