package com.dyw.client.entity.protection;

public class FaceMarkEntity {
    private LeftEyeEntity leftEye;//左眼位置
    private RightEyeEntity rightEye;//右眼位置
    private NoseTipEntity noseTip;//鼻尖位置
    private LeftMouthEntity leftMouth;//左嘴角位置
    private RightMouthEntity rightMouth;//右嘴角位置

    public LeftEyeEntity getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(LeftEyeEntity leftEye) {
        this.leftEye = leftEye;
    }

    public RightEyeEntity getRightEye() {
        return rightEye;
    }

    public void setRightEye(RightEyeEntity rightEye) {
        this.rightEye = rightEye;
    }

    public NoseTipEntity getNoseTip() {
        return noseTip;
    }

    public void setNoseTip(NoseTipEntity noseTip) {
        this.noseTip = noseTip;
    }

    public LeftMouthEntity getLeftMouth() {
        return leftMouth;
    }

    public void setLeftMouth(LeftMouthEntity leftMouth) {
        this.leftMouth = leftMouth;
    }

    public RightMouthEntity getRightMouth() {
        return rightMouth;
    }

    public void setRightMouth(RightMouthEntity rightMouth) {
        this.rightMouth = rightMouth;
    }
}
