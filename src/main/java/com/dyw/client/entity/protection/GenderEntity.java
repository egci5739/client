package com.dyw.client.entity.protection;

public class GenderEntity {
    private float confidence;//置信度
    private String value;//性别

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
