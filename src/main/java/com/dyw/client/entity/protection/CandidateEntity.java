package com.dyw.client.entity.protection;

import java.util.List;

public class CandidateEntity {
    private int alarmId;//报警 ID
    private String blacklist_id;//报警名单 ID
    private List<Human_dataEntity> human_data;//匹配的人脸信息列表, 同一个人员的多张人脸
    private String human_id;//名单人员 ID
    private Reserve_fieldEntity reserve_field;//保留字节(透传数据)
    private float similarity;//该人员所有匹配人脸图片结果中的最大相似度

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getBlacklist_id() {
        return blacklist_id;
    }

    public void setBlacklist_id(String blacklist_id) {
        this.blacklist_id = blacklist_id;
    }

    public List<Human_dataEntity> getHuman_data() {
        return human_data;
    }

    public void setHuman_data(List<Human_dataEntity> human_data) {
        this.human_data = human_data;
    }

    public String getHuman_id() {
        return human_id;
    }

    public void setHuman_id(String human_id) {
        this.human_id = human_id;
    }

    public Reserve_fieldEntity getReserve_field() {
        return reserve_field;
    }

    public void setReserve_field(Reserve_fieldEntity reserve_field) {
        this.reserve_field = reserve_field;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
}
