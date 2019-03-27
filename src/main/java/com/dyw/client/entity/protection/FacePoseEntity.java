package com.dyw.client.entity.protection;

public class FacePoseEntity {
    private float pitch;//平面外上下俯仰角, 人脸朝上为正
    private float roll;//平面内旋转角, 人脸顺时针旋转为正
    private float yaw;//平面外左右偏转角, 人脸朝左为正

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
