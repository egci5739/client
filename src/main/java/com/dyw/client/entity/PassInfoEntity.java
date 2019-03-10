package com.dyw.client.entity;

public class PassInfoEntity {
    private String passName;
    private String passCard;
    private String passTime;
    private String passPoint;
    private String isPass;
    private byte[] passPhoto;
    private byte[] photo;

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getPassCard() {
        return passCard;
    }

    public void setPassCard(String passCard) {
        this.passCard = passCard;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(String passPoint) {
        this.passPoint = passPoint;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public byte[] getPassPhoto() {
        return passPhoto;
    }

    public void setPassPhoto(byte[] passPhoto) {
        this.passPhoto = passPhoto;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
