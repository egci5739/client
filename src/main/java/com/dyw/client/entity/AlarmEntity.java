package com.dyw.client.entity;

import java.sql.Timestamp;

public class AlarmEntity {
    private int alarmId;
    private String alarmName;
    private String alarmDetail;
    private int alarmNoteId;
    private String alarmNote;
    private String operator;
    private Timestamp createTime;
    private int alarmStatus;
    private int alarmPermission;

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmDetail() {
        return alarmDetail;
    }

    public void setAlarmDetail(String alarmDetail) {
        this.alarmDetail = alarmDetail;
    }

    public int getAlarmNoteId() {
        return alarmNoteId;
    }

    public void setAlarmNoteId(int alarmNoteId) {
        this.alarmNoteId = alarmNoteId;
    }

    public String getAlarmNote() {
        return alarmNote;
    }

    public void setAlarmNote(String alarmNote) {
        this.alarmNote = alarmNote;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getAlarmPermission() {
        return alarmPermission;
    }

    public void setAlarmPermission(int alarmPermission) {
        this.alarmPermission = alarmPermission;
    }
}
