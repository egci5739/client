package com.dyw.client.service;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

public class LiveTestService {
    private final FaceEngine faceEngine;
    private int errorCode;
    private final FunctionConfiguration functionConfiguration;
    private final Logger logger = LoggerFactory.getLogger(LiveTestService.class);

    public LiveTestService() {
        //dll文件路径
        logger.info(System.getProperty("user.dir") + "\\lib\\WIN64");
        faceEngine = new FaceEngine(System.getProperty("user.dir") + "\\lib\\WIN64");
        errorCode = 0;
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            logger.info("引擎激活失败");
        }
//        faceEngine.activeOffline(System.getProperty("user.dir") + "\\lib\\8611113X4131KBDR.dat");//公司设备

        logger.info(faceEngine.activeOffline(System.getProperty("user.dir") + "\\lib\\8611113X4131KBDR.dat") + "");//现场设备
//        faceEngine.activeOffline("C\\FaceRecognition\\8611113X412DPPCK.dat");//根据设备填写

        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            logger.info("获取激活文件信息失败，错误码：" + errorCode);
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            logger.info("初始化引擎失败,错误码：" + errorCode);
        }
    }

    /*
     * 检测模块
     * */
    public int judge(byte[] picByte) {
        //人脸检测
        ImageInfo imageInfo = getRGBData(picByte);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        //特征提取
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        //设置活体阈值
        errorCode = faceEngine.setLivenessParam(0.5f, 0.7f);
        //人脸属性检测
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        configuration.setSupportFace3dAngle(true);
        configuration.setSupportGender(true);
        configuration.setSupportLiveness(true);
        errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, configuration);
        //活体检测
        List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
        errorCode = faceEngine.getLiveness(livenessInfoList);
        return livenessInfoList.get(0).getLiveness();
    }

    /*
     * 引擎卸载
     * */
    public void release() {
        errorCode = faceEngine.unInit();
    }
}
