package com.dyw.client.entity.protection;

public class MonitorPointEntity {
    private String monitorPointID;//监控点id
    private String monitorPointName;//监控点名称
    private String deviceID;//设备 ID
    private String deviceIP;//设备 IP
    private int devicePort;//设备端口号
    private String deviceUser;//设备用户名
    private String devicePassword;//设备密码
    private String streamURL;//取流URL
    private String isGuard;//是否布防
    private int channel;//设备通道号
    private Boolean isSupportPlayBack;//支持录像回放
    private Boolean isAnsysisVideo;//正在分析视频
    private String source;//资源类型

    public String getMonitorPointID() {
        return monitorPointID;
    }

    public void setMonitorPointID(String monitorPointID) {
        this.monitorPointID = monitorPointID;
    }

    public String getMonitorPointName() {
        return monitorPointName;
    }

    public void setMonitorPointName(String monitorPointName) {
        this.monitorPointName = monitorPointName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public int getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

    public String getDeviceUser() {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser) {
        this.deviceUser = deviceUser;
    }

    public String getDevicePassword() {
        return devicePassword;
    }

    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getIsGuard() {
        return isGuard;
    }

    public void setIsGuard(String isGuard) {
        this.isGuard = isGuard;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Boolean getSupportPlayBack() {
        return isSupportPlayBack;
    }

    public void setSupportPlayBack(Boolean supportPlayBack) {
        isSupportPlayBack = supportPlayBack;
    }

    public Boolean getAnsysisVideo() {
        return isAnsysisVideo;
    }

    public void setAnsysisVideo(Boolean ansysisVideo) {
        isAnsysisVideo = ansysisVideo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
