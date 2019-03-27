package com.dyw.client.entity.protection;

public class Human_dataEntity {
    private String bkg_picurl;//背景大图 URL
    private String face_id;//人脸 ID
    private String face_picurl;//人脸子图 URL
    private Face_rectEntity ace_rect;//人脸子图区域, 相对于背景大图
    private float similarity;//该人脸图片的相似度

    public String getBkg_picurl() {
        return bkg_picurl;
    }

    public void setBkg_picurl(String bkg_picurl) {
        this.bkg_picurl = bkg_picurl;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getFace_picurl() {
        return face_picurl;
    }

    public void setFace_picurl(String face_picurl) {
        this.face_picurl = face_picurl;
    }

    public Face_rectEntity getAce_rect() {
        return ace_rect;
    }

    public void setAce_rect(Face_rectEntity ace_rect) {
        this.ace_rect = ace_rect;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }
}
