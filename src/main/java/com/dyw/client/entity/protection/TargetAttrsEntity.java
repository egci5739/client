package com.dyw.client.entity.protection;

public class TargetAttrsEntity {
    private String deviceId;//设备 ID
    private int deviceChannel;//设备通道号
    private String deviceName;//设备名称
    private String faceTime;//抓拍时间
    private RectEntity rect;//人脸大图矩形框
    private String bkgUrl;//背景大图 URL

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceChannel() {
        return deviceChannel;
    }

    public void setDeviceChannel(int deviceChannel) {
        this.deviceChannel = deviceChannel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFaceTime() {
        return faceTime;
    }

    public void setFaceTime(String faceTime) {
        this.faceTime = faceTime;
    }

    public RectEntity getRect() {
        return rect;
    }

    public void setRect(RectEntity rect) {
        this.rect = rect;
    }

    public String getBkgUrl() {
        return bkgUrl;
    }

    public void setBkgUrl(String bkgUrl) {
        this.bkgUrl = bkgUrl;
    }
}
