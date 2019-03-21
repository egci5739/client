package com.dyw.client.entity.protection;

public class FDLibEntity {
    private String FDID;//人脸库 ID
    private String faceLibType;//人脸库类型: blackFD-名单库, staticFD-静态库
    private String name;//人脸库的名称
    private String customInfo;//自定义信息

    public String getFDID() {
        return FDID;
    }

    public void setFDID(String FDID) {
        this.FDID = FDID;
    }

    public String getFaceLibType() {
        return faceLibType;
    }

    public void setFaceLibType(String faceLibType) {
        this.faceLibType = faceLibType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customInfo = customInfo;
    }
}
