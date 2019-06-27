package com.dyw.client.entity.protection;

public class TargetsEntity {
    private int id;//序号,
    private String captureTime;//抓拍时间:"2004-05-03T17:30:08Z"
    private String captureSite;//抓拍地点
    private String subpicUrl;//人脸小图URL
    private Float similarity;//相似度

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }

    public String getCaptureSite() {
        return captureSite;
    }

    public void setCaptureSite(String captureSite) {
        this.captureSite = captureSite;
    }

    public String getSubpicUrl() {
        return subpicUrl;
    }

    public void setSubpicUrl(String subpicUrl) {
        this.subpicUrl = subpicUrl;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }
}
