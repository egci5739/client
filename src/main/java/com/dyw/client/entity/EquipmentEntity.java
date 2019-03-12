package com.dyw.client.entity;

public class EquipmentEntity {
    private int Id;//id
    private int GroupId;//核数
    private String Name;//设备名称
    private String IP;//设备ip
    private String StatusSwitchSocketIP;//切换器ip

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getStatusSwitchSocketIP() {
        return StatusSwitchSocketIP;
    }

    public void setStatusSwitchSocketIP(String statusSwitchSocketIP) {
        StatusSwitchSocketIP = statusSwitchSocketIP;
    }
}
