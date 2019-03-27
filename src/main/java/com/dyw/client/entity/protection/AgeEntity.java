package com.dyw.client.entity.protection;

public class AgeEntity {
    private int range;//年龄偏差
    private int value;//年龄
    private String ageGroup;//年龄段: unknown-未知, child-少年, young-青年,middle-中年, old-老年

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }
}
