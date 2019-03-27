package com.dyw.client.entity.protection;

import java.util.List;

public class AlarmResultEntity {
    private String requestURL;//请求 URL
    private int statusCode;//状态码
    private String statusString;//状态描述
    private String subStatusCode;//子状态码
    private int errorCode;
    private String errorMsg;//
    private String image;//提交任务时的图片 URL
    private String traceUuid;//目标 ID, 一个人员对应一个目标 ID, 相同的人员多个报警结果对应相同的目标 ID
    private int traceIdx;//图片索引, 同一个人员匹配的多张图片中相似度最高的图片索引
    private TargetAttrsEntity targetAttrs;//目标属性
    private List<FacesEntity> faces;//人脸信息包括检测结果、属性、建模和匹配名单, 一张图片中的可能多个人脸结果

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getSubStatusCode() {
        return subStatusCode;
    }

    public void setSubStatusCode(String subStatusCode) {
        this.subStatusCode = subStatusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTraceUuid() {
        return traceUuid;
    }

    public void setTraceUuid(String traceUuid) {
        this.traceUuid = traceUuid;
    }

    public int getTraceIdx() {
        return traceIdx;
    }

    public void setTraceIdx(int traceIdx) {
        this.traceIdx = traceIdx;
    }

    public TargetAttrsEntity getTargetAttrs() {
        return targetAttrs;
    }

    public void setTargetAttrs(TargetAttrsEntity targetAttrs) {
        this.targetAttrs = targetAttrs;
    }

    public List<FacesEntity> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesEntity> faces) {
        this.faces = faces;
    }
}
