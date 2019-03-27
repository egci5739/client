package com.dyw.client.entity.protection;

import java.util.List;

public class FacesEntity {
    private int faceId;//人脸 ID, 一张图片中的人脸 ID 不能重复
    private FaceRectEntity faceRect;//人脸小图矩形框
    private FaceMarkEntity faceMark;//人脸特征属性
    private RecommendFaceRectEntity recommendFaceRect;//人脸区域推荐位置
    private FacePoseEntity facePose;//脸型姿势
    private AgeEntity age;//年龄
    private GenderEntity gender;//性别
    private GlassEntity glass;//是否戴眼镜
    private SmileEntity smile;//是否微笑
    private List<IdentifyEntity> identify;//人员属性信息

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public FaceRectEntity getFaceRect() {
        return faceRect;
    }

    public void setFaceRect(FaceRectEntity faceRect) {
        this.faceRect = faceRect;
    }

    public FaceMarkEntity getFaceMark() {
        return faceMark;
    }

    public void setFaceMark(FaceMarkEntity faceMark) {
        this.faceMark = faceMark;
    }

    public RecommendFaceRectEntity getRecommendFaceRect() {
        return recommendFaceRect;
    }

    public void setRecommendFaceRect(RecommendFaceRectEntity recommendFaceRect) {
        this.recommendFaceRect = recommendFaceRect;
    }

    public FacePoseEntity getFacePose() {
        return facePose;
    }

    public void setFacePose(FacePoseEntity facePose) {
        this.facePose = facePose;
    }

    public AgeEntity getAge() {
        return age;
    }

    public void setAge(AgeEntity age) {
        this.age = age;
    }

    public GenderEntity getGender() {
        return gender;
    }

    public void setGender(GenderEntity gender) {
        this.gender = gender;
    }

    public GlassEntity getGlass() {
        return glass;
    }

    public void setGlass(GlassEntity glass) {
        this.glass = glass;
    }

    public SmileEntity getSmile() {
        return smile;
    }

    public void setSmile(SmileEntity smile) {
        this.smile = smile;
    }

    public List<IdentifyEntity> getIdentify() {
        return identify;
    }

    public void setIdentify(List<IdentifyEntity> identify) {
        this.identify = identify;
    }
}
