package com.dyw.client.entity;

public class FaceInfoEntity {
    private double noseX;//鼻子位置
    private double noseY;//鼻子位置
    private double faceWidth;//人脸宽度
    private double faceHeight;//人脸高度
    private double imageWidth;//照片宽度
    private double imageHeight;//照片高度

    public double getNoseX() {
        return noseX;
    }

    public void setNoseX(double noseX) {
        this.noseX = noseX;
    }

    public double getNoseY() {
        return noseY;
    }

    public void setNoseY(double noseY) {
        this.noseY = noseY;
    }

    public double getFaceWidth() {
        return faceWidth;
    }

    public void setFaceWidth(double faceWidth) {
        this.faceWidth = faceWidth;
    }

    public double getFaceHeight() {
        return faceHeight;
    }

    public void setFaceHeight(double faceHeight) {
        this.faceHeight = faceHeight;
    }

    public double getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public double getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public String toString() {
        return "FaceInfoEntity{" +
                "noseX=" + noseX +
                ", noseY=" + noseY +
                ", faceWidth=" + faceWidth +
                ", faceHeight=" + faceHeight +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                '}';
    }
}
