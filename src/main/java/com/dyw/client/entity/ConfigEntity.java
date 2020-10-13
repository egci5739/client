package com.dyw.client.entity;

public class ConfigEntity {
    //一体机通用配置
    private short devicePort;//设备端口
    private String deviceName;//设备账号
    private String devicePass;//设备密码
    //办证端端口
    private short socketRegisterPort;
    //监控端端口
    private short socketMonitorPort;
    //NTP服务器ip
    private String ntpServerIp;
    //服务器IP
    private String socketIp;
    //脸谱服务器ip和端口
    private String faceServerIp;
    private short faceServerPort;
    //nvr服务器IP和端口
    private String nvrServerIp;
    private short nvrServerPort;
    //监控页面显示的最大条数
    private int displayMonitorRowCount;
    //布控页面显示的最大条数
    private int displayProtectionRowCount;
    //socket连接布控端口
    private short socketProtectionPort;
    //独有的
    private String faceCollectionIp;//采集设备IP
    private int synchronizationHour;//几点同步
    private int synchronizationMinute;//几分同步
    private int synchronizationSecond;//几秒同步

    public short getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(short devicePort) {
        this.devicePort = devicePort;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevicePass() {
        return devicePass;
    }

    public void setDevicePass(String devicePass) {
        this.devicePass = devicePass;
    }

    public short getSocketRegisterPort() {
        return socketRegisterPort;
    }

    public void setSocketRegisterPort(short socketRegisterPort) {
        this.socketRegisterPort = socketRegisterPort;
    }

    public short getSocketMonitorPort() {
        return socketMonitorPort;
    }

    public void setSocketMonitorPort(short socketMonitorPort) {
        this.socketMonitorPort = socketMonitorPort;
    }

    public String getNtpServerIp() {
        return ntpServerIp;
    }

    public void setNtpServerIp(String ntpServerIp) {
        this.ntpServerIp = ntpServerIp;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public String getFaceServerIp() {
        return faceServerIp;
    }

    public void setFaceServerIp(String faceServerIp) {
        this.faceServerIp = faceServerIp;
    }

    public short getFaceServerPort() {
        return faceServerPort;
    }

    public void setFaceServerPort(short faceServerPort) {
        this.faceServerPort = faceServerPort;
    }

    public String getNvrServerIp() {
        return nvrServerIp;
    }

    public void setNvrServerIp(String nvrServerIp) {
        this.nvrServerIp = nvrServerIp;
    }

    public short getNvrServerPort() {
        return nvrServerPort;
    }

    public void setNvrServerPort(short nvrServerPort) {
        this.nvrServerPort = nvrServerPort;
    }

    public String getFaceCollectionIp() {
        return faceCollectionIp;
    }

    public void setFaceCollectionIp(String faceCollectionIp) {
        this.faceCollectionIp = faceCollectionIp;
    }

    public int getDisplayMonitorRowCount() {
        return displayMonitorRowCount;
    }

    public void setDisplayMonitorRowCount(int displayMonitorRowCount) {
        this.displayMonitorRowCount = displayMonitorRowCount;
    }

    public int getDisplayProtectionRowCount() {
        return displayProtectionRowCount;
    }

    public void setDisplayProtectionRowCount(int displayProtectionRowCount) {
        this.displayProtectionRowCount = displayProtectionRowCount;
    }

    public short getSocketProtectionPort() {
        return socketProtectionPort;
    }

    public void setSocketProtectionPort(short socketProtectionPort) {
        this.socketProtectionPort = socketProtectionPort;
    }

    public int getSynchronizationHour() {
        return synchronizationHour;
    }

    public void setSynchronizationHour(int synchronizationHour) {
        this.synchronizationHour = synchronizationHour;
    }

    public int getSynchronizationMinute() {
        return synchronizationMinute;
    }

    public void setSynchronizationMinute(int synchronizationMinute) {
        this.synchronizationMinute = synchronizationMinute;
    }

    public int getSynchronizationSecond() {
        return synchronizationSecond;
    }

    public void setSynchronizationSecond(int synchronizationSecond) {
        this.synchronizationSecond = synchronizationSecond;
    }
}
