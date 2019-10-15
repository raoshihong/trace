package com.daoyuan.trace.client.trace;

import java.io.Serializable;

public class TraceData implements Serializable {

    /**
     * 事件名
     */
    private String name;
    /**
     * 发生地
     */
    private String pageCode;
    /**
     * 发生地名称
     */
    private String pageName;
    /**
     * 系统(根据系统来获取对应的用户id)
     */
    private String platformCode;

    private String platformName;
    /**
     * 事件类型
     */
    private String type;

    private String appId;
    private String url;
    private String method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
