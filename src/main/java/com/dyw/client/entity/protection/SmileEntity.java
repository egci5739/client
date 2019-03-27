package com.dyw.client.entity.protection;

public class SmileEntity {
    private float confidence;//置信度
    private String value;//是否戴眼镜: no-不戴眼镜, yes-戴眼镜

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
