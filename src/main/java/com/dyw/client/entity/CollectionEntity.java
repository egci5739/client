package com.dyw.client.entity;

public class CollectionEntity {
    private int Id;
    private byte[] StaffPhoto;
    private byte[] IdentificationPhoto;
    private String Similation;
    private String Name;
    private String CardId;
    private String nation;
    private String Sex;
    private String Birthday;
    private String ExpirationDate;
    private String organization;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public byte[] getStaffPhoto() {
        return StaffPhoto;
    }

    public void setStaffPhoto(byte[] staffPhoto) {
        StaffPhoto = staffPhoto;
    }

    public byte[] getIdentificationPhoto() {
        return IdentificationPhoto;
    }

    public void setIdentificationPhoto(byte[] identificationPhoto) {
        IdentificationPhoto = identificationPhoto;
    }

    public String getSimilation() {
        return Similation;
    }

    public void setSimilation(String similation) {
        Similation = similation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        ExpirationDate = expirationDate;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
