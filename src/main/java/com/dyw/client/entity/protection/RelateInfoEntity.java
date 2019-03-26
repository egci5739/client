package com.dyw.client.entity.protection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RelateInfoEntity {
    private String relateID;//布控 ID
    private String name;//布控名称
    private String FDID;//人脸库 ID
    private String cameraID;//监控点 ID
    private String cameraName;//监控点名称
    private String reason;//布控原因
    private List<PlanInfoEntity> planInfo;//布控计划信息
    private String createTime;//创建时间
    private String userName;//布控人
    private String listType;//布控类型

    public String getRelateID() {
        return relateID;
    }

    public void setRelateID(String relateID) {
        this.relateID = relateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFDID() {
        return FDID;
    }

    public void setFDID(String FDID) {
        this.FDID = FDID;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<PlanInfoEntity> getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(List<PlanInfoEntity> planInfo) {
        this.planInfo = planInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
