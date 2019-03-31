package com.dyw.client.entity.protection;

import java.util.List;

public class AlarmHistoryEntity {
    private String bkg_picurl;//抓拍背景大图 URL
    private String face_picurl;//抓拍人脸子图 URL
    private String device_name;//监控点名称
    private String alarm_date;//报警日期
    private String ageGroup;//年龄段
    private String gender;//性别
    private String glass;//是否带眼镜
    private float alarm_max_similarity;//相似度最大阈值
    private List<human_data_alarmEntity> human_data;//名单信息

    public String getBkg_picurl() {
        return bkg_picurl;
    }

    public void setBkg_picurl(String bkg_picurl) {
        this.bkg_picurl = bkg_picurl;
    }

    public String getFace_picurl() {
        return face_picurl;
    }

    public void setFace_picurl(String face_picurl) {
        this.face_picurl = face_picurl;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public float getAlarm_max_similarity() {
        return alarm_max_similarity;
    }

    public void setAlarm_max_similarity(float alarm_max_similarity) {
        this.alarm_max_similarity = alarm_max_similarity;
    }

    public List<human_data_alarmEntity> getHuman_data() {
        return human_data;
    }

    public void setHuman_data(List<human_data_alarmEntity> human_data) {
        this.human_data = human_data;
    }
}
