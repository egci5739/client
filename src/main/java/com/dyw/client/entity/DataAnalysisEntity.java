package com.dyw.client.entity;

public class DataAnalysisEntity {
    private String equipmentName;
    private int totalNumber;
    private int successNumber;
    private int faultNumber;
    private int noCardNumber;
    private String successRate;
    private String faultRate;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(int successNumber) {
        this.successNumber = successNumber;
    }

    public int getFaultNumber() {
        return faultNumber;
    }

    public void setFaultNumber(int faultNumber) {
        this.faultNumber = faultNumber;
    }

    public int getNoCardNumber() {
        return noCardNumber;
    }

    public void setNoCardNumber(int noCardNumber) {
        this.noCardNumber = noCardNumber;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getFaultRate() {
        return faultRate;
    }

    public void setFaultRate(String faultRate) {
        this.faultRate = faultRate;
    }
}
