package com.dyw.client.entity;

import java.sql.Timestamp;

public class PassInfoEntity {
    private int Id;
    private String StaffName;//姓名
    private String CardNumber;//卡号
    private java.sql.Timestamp Date;//通行时间
    private java.sql.Timestamp startDate;//开始时间
    private java.sql.Timestamp endDate;//结束时间
    private int Similarity;//相似度值
    private Boolean IsPass;//是否通过
    private byte[] CapturePhoto;//抓拍图
    private byte[] photo;//底图
    private int EventTypeId;//事件id
    private String EquipmentName;//设备名称
    private String IP;//设备ip

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public int getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(int similarity) {
        Similarity = similarity;
    }

    public Boolean getPass() {
        return IsPass;
    }

    public void setPass(Boolean pass) {
        IsPass = pass;
    }

    public byte[] getCapturePhoto() {
        return CapturePhoto;
    }

    public void setCapturePhoto(byte[] capturePhoto) {
        CapturePhoto = capturePhoto;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getEventTypeId() {
        return EventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        EventTypeId = eventTypeId;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
