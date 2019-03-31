package com.dyw.client.entity.protection;

import java.util.List;

public class human_data_alarmEntity {
    private String FDID;//人脸库 ID
    private String name;//人脸库名称
    private String human_name;//名单姓名
    private String human_id;//名单人员 ID
    private List<face_data_alarmEntity> face_data;//人脸图片数据

    public String getFDID() {
        return FDID;
    }

    public void setFDID(String FDID) {
        this.FDID = FDID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHuman_name() {
        return human_name;
    }

    public void setHuman_name(String human_name) {
        this.human_name = human_name;
    }

    public String getHuman_id() {
        return human_id;
    }

    public void setHuman_id(String human_id) {
        this.human_id = human_id;
    }

    public List<face_data_alarmEntity> getFace_data() {
        return face_data;
    }

    public void setFace_data(List<face_data_alarmEntity> face_data) {
        this.face_data = face_data;
    }
}
