package com.dyw.client.entity.protection;

public class face_data_alarmEntity {
    private float face_similarity;//相似度
    private String bl_picurl;//名单人脸图片 URL

    public float getFace_similarity() {
        return face_similarity;
    }

    public void setFace_similarity(float face_similarity) {
        this.face_similarity = face_similarity;
    }

    public String getBl_picurl() {
        return bl_picurl;
    }

    public void setBl_picurl(String bl_picurl) {
        this.bl_picurl = bl_picurl;
    }
}
