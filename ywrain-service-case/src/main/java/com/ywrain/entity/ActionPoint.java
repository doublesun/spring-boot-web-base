package com.ywrain.entity;

import com.ywrain.entity.ext.ActionPointExt;
import java.util.Date;

public class ActionPoint extends ActionPointExt {
    private Long id;

    private Integer clientType;

    private Long cverStart;

    private Long cverEnd;

    private String pageCode;

    private String page;

    private String view;

    private String eventId;

    private String eventRemark;

    private Integer eventType;

    private Integer pointType;

    private Integer del;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Long getCverStart() {
        return cverStart;
    }

    public void setCverStart(Long cverStart) {
        this.cverStart = cverStart;
    }

    public Long getCverEnd() {
        return cverEnd;
    }

    public void setCverEnd(Long cverEnd) {
        this.cverEnd = cverEnd;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode == null ? null : pageCode.trim();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page == null ? null : page.trim();
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view == null ? null : view.trim();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId == null ? null : eventId.trim();
    }

    public String getEventRemark() {
        return eventRemark;
    }

    public void setEventRemark(String eventRemark) {
        this.eventRemark = eventRemark == null ? null : eventRemark.trim();
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}