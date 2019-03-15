package com.dyw.client.entity;

public class StaffEntity {
    private String OldCard;
    private int StaffId;
    private String Name;
    private String NameEn;
    private String CardId;
    private String CardNumber;
    private String Birthday;
    private String Sex;
    private String Company;
    private byte[] Photo;
    private String organization;
    private String validityPeriod;

    public String getOldCard() {
        return OldCard;
    }

    public void setOldCard(String oldCard) {
        OldCard = oldCard;
    }

    public int getStaffId() {
        return StaffId;
    }

    public void setStaffId(int staffId) {
        StaffId = staffId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }
}
