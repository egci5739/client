package com.dyw.client.service;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.FaceInfoEntity;
import com.dyw.client.tool.Tool;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StaffOperationService {
    private Logger logger = LoggerFactory.getLogger(StaffOperationService.class);

    /*
     * 获取待拍照人员列表
     * */
    public List<StaffEntity> getWaitStaffList() {
        try {
            List<StaffEntity> list = Egci.session.selectList("mapping.staffMapper.getTemporaryStaff");
            Egci.session.commit();
            return list;
        } catch (Exception e) {
            logger.error("获取待拍照人员列表出错", e);
            return null;
        }
    }

    /*
     * 查询人员
     * */
    public List<StaffEntity> search(String name, String card) {
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setStaffName(Tool.getSearchCondition(name));
        staffEntity.setStaffCardNumber(Tool.getSearchCondition(card));
        List<StaffEntity> resultStaffList = null;
        if (!name.equals("") && !card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCardAndName", staffEntity);
        } else if (!name.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithName", staffEntity);
        } else if (!card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCard", staffEntity);
        }
        return resultStaffList;
    }

    /*
     * 查询待拍照人员
     * */
    public List<StaffEntity> searchWaitStaff(String name, String card) {
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setStaffName(Tool.getSearchCondition(name));
        staffEntity.setStaffCardNumber(Tool.getSearchCondition(card));
        List<StaffEntity> resultStaffList = null;
        if (!name.equals("") && !card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getWaitStaffWithCardAndName", staffEntity);
        } else if (!name.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getWaitStaffWithName", staffEntity);
        } else if (!card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getWaitStaffWithCard", staffEntity);
        }
        return resultStaffList;
    }

    /*
     * 保存人员信息
     * */
    public void save(StaffEntity staffEntity, StaffEntity oldStaff) {
        try {
            if (staffEntity.getStaffBirthday().equals("") || staffEntity.getStaffCardNumber().length() < 10) {
                staffEntity.setStaffBirthday("1970-01-01");
            }
            if (oldStaff.getStaffId() > 0) {
                staffEntity.setOldCard(oldStaff.getStaffCardNumber());
                staffEntity.setStaffId(oldStaff.getStaffId());
                //更新
                Egci.session.update("mapping.staffMapper.updateStaff", staffEntity);
                Egci.session.commit();
                //更新脸谱
                if (Egci.faceServerStatus == 1) {
                    updateFaceServerFaceInfo(oldStaff, staffEntity);//更新脸谱服务器中的人员信息
                }
            } else {
                //新增
                Egci.session.insert("mapping.staffMapper.insertStaff", staffEntity);
                Egci.session.commit();
                //添加脸谱
                if (Egci.faceServerStatus == 1) {
                    addFaceServerFaceInfo(staffEntity);
                }
            }
            Thread.sleep(1000);
            SendInfoSocketService insertSendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketRegisterPort());
            insertSendInfoSocketService.sendInfo("1#" + staffEntity.getStaffCardNumber() + "\n");
            insertSendInfoSocketService.receiveInfoOnce();
        } catch (Exception e) {
            logger.error("保存人员信息出错", e);
        }
    }

    /*
     * 删除人员
     * */
    public void delete(StaffEntity staffEntity) {
        //特殊情况，需先删一体机里面的
        SendInfoSocketService deleteSendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketRegisterPort());
        deleteSendInfoSocketService.sendInfo("2#" + staffEntity.getStaffCardNumber());
        deleteSendInfoSocketService.receiveInfoOnce();
        //删除人脸服务器中人脸信息
        if (Egci.faceServerStatus == 1) {
            deleteFaceServerFaceInfo(staffEntity);
        }
        staffEntity.setStaffCardNumber("0");
        Egci.session.delete("mapping.staffMapper.deleteStaff", staffEntity);
        Egci.session.commit();
    }

    /*
     * 新增或更新待拍照人员表
     * */
    public Boolean addWaitStaff(StaffEntity staffEntity) {
        Boolean status = false;
        //判断是否卡号已经存在
        List<StaffEntity> staffEntityStaff = Egci.session.selectList("mapping.staffMapper.getStaffWithCard", staffEntity.getStaffCardNumber());
        List<StaffEntity> staffEntityTemporary = Egci.session.selectList("mapping.staffMapper.getWaitStaffWithCardAccurate", staffEntity);
        if (staffEntityStaff.size() > 0 || staffEntityTemporary.size() > 0) {
            status = false;
        } else {
            Egci.session.insert("mapping.staffMapper.insertWaitStaff", staffEntity);
            Egci.session.commit();
            status = true;
        }
        return status;
    }

    /*
     * 更新脸谱服务器中的人员信息
     * */
    private void updateFaceServerFaceInfo(StaffEntity oldInfo, StaffEntity newInfo) {
        try {
            //获取人脸库列表
            List<FDLibEntity> fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                try {
                    String instructionGet = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
                    org.json.JSONObject inboundDataGet = new org.json.JSONObject();
                    inboundDataGet.put("searchResultPosition", 0);
                    inboundDataGet.put("maxResults", 100);
                    inboundDataGet.put("faceLibType", "blackFD");
                    inboundDataGet.put("FDID", fdLibEntity.getFDID());
                    inboundDataGet.put("name", oldInfo.getStaffName() + "_" + oldInfo.getStaffCardNumber() + "_" + oldInfo.getStaffId());
                    FaceInfoEntity faceInfoEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instructionGet, inboundDataGet).getString("MatchList"), FaceInfoEntity.class).get(0);
                    //更改信息
                    org.json.JSONObject inboundDataSet = new org.json.JSONObject();
                    String instructionSet = "/ISAPI/Intelligent/FDLib/FDSearch?format=json&FDID=" + fdLibEntity.getFDID() + "&FPID=" + faceInfoEntity.getFPID() + "&faceLibType=blackFD";
                    inboundDataSet.put("faceURL", faceInfoEntity.getFaceURL());
                    inboundDataSet.put("faceLibType", "blackFD");
                    inboundDataSet.put("name", newInfo.getStaffName() + "_" + newInfo.getStaffCardNumber() + "_" + newInfo.getStaffId());//名字_卡号_id
                    inboundDataSet.put("gender", Tool.changeGenderToMaleAndFemale(String.valueOf(newInfo.getStaffGender())));
                    inboundDataSet.put("bornTime", newInfo.getStaffBirthday());
                    org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(2, instructionSet, inboundDataSet);
                    if (resultData.getInt("statusCode") == 1) {
                        // Tool.showMessage("添加成功", "提示", 0);
                    } else {
                        logger.info("更新脸谱服务器中的人员信息出错，错误码：" + resultData.getString("errorMsg"));
                    }
                } catch (Exception e) {
                    logger.error("更新脸谱服务器中的人员信息出错,不提示错误信息");
                }
            }
        } catch (JSONException e) {
            logger.error("获取人脸库列表出错", e);
        }
    }

    /*
     * 添加脸谱服务器中的人员信息
     * */
    private void addFaceServerFaceInfo(StaffEntity staffEntity) {
        String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
        try {
            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, Egci.fdLibIDForStaff, staffEntity.getStaffImage(), null);
            //添加到电厂人员库
            org.json.JSONObject inboundDataStaff = new org.json.JSONObject();
            inboundDataStaff.put("faceURL", resultFaceUrlData.getString("URL"));
            inboundDataStaff.put("faceLibType", "blackFD");
            inboundDataStaff.put("FDID", Egci.fdLibIDForStaff);
            inboundDataStaff.put("name", staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id

            inboundDataStaff.put("gender", Tool.changeGenderToMaleAndFemale(String.valueOf(staffEntity.getStaffGender())));
            if (staffEntity.getStaffBirthday() == null) {
                staffEntity.setStaffBirthday("1900-01-01");
            }
            inboundDataStaff.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getStaffBirthday()));
            Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataStaff);
            //添加到陌生人库
            org.json.JSONObject inboundDataStranger = new org.json.JSONObject();
            inboundDataStranger.put("faceURL", resultFaceUrlData.getString("URL"));
            inboundDataStranger.put("faceLibType", "blackFD");
            inboundDataStranger.put("FDID", Egci.fdLibIDForStranger);
            inboundDataStranger.put("name", staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
            inboundDataStranger.put("gender", Tool.changeGenderToMaleAndFemale(String.valueOf(staffEntity.getStaffGender())));
            if (staffEntity.getStaffBirthday() == null || staffEntity.getStaffBirthday().equals("")) {
                staffEntity.setStaffBirthday("1900-01-01");
            }
            inboundDataStranger.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getStaffBirthday()));
            Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataStranger);
        } catch (JSONException e) {
            logger.error("添加人员出错，卡号：" + staffEntity.getStaffCardNumber());
        }
    }

    /*
     * 删除人脸服务器中的人员信息
     * */
    private void deleteFaceServerFaceInfo(StaffEntity staffEntity) {
        try {
            String instructionGet = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
            //删除电厂人员库中的人脸信息
            org.json.JSONObject inboundDataGetStaff = new org.json.JSONObject();
            inboundDataGetStaff.put("searchResultPosition", 0);
            inboundDataGetStaff.put("maxResults", 100);
            inboundDataGetStaff.put("faceLibType", "blackFD");
            inboundDataGetStaff.put("FDID", Egci.fdLibIDForStaff);
            inboundDataGetStaff.put("name", staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber() + "_" + staffEntity.getStaffId());
            FaceInfoEntity faceInfoEntityStaff = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instructionGet, inboundDataGetStaff).getString("MatchList"), FaceInfoEntity.class).get(0);
            org.json.JSONObject deleteInboundDataStaff = new org.json.JSONObject();
            HashMap<String, Object> mapStaff = new HashMap<String, Object>();
            JSONArray jsonArrayStaff = new JSONArray();
            mapStaff.put("value", faceInfoEntityStaff.getFPID());
            jsonArrayStaff.put(0, mapStaff);
            deleteInboundDataStaff.put("FPID", jsonArrayStaff);
            Tool.faceInfoOperation(2, Egci.fdLibIDForStaff, null, deleteInboundDataStaff);
            //删除电厂陌生人员库人脸信息
            org.json.JSONObject inboundDataGetStranger = new org.json.JSONObject();
            inboundDataGetStranger.put("searchResultPosition", 0);
            inboundDataGetStranger.put("maxResults", 100);
            inboundDataGetStranger.put("faceLibType", "blackFD");
            inboundDataGetStranger.put("FDID", Egci.fdLibIDForStranger);
            inboundDataGetStranger.put("name", staffEntity.getStaffName() + "_" + staffEntity.getStaffCardNumber() + "_" + staffEntity.getStaffId());
            FaceInfoEntity faceInfoEntityStranger = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instructionGet, inboundDataGetStranger).getString("MatchList"), FaceInfoEntity.class).get(0);
            org.json.JSONObject deleteInboundDataStranger = new org.json.JSONObject();
            HashMap<String, Object> mapStranger = new HashMap<String, Object>();
            JSONArray jsonArrayStranger = new JSONArray();
            mapStranger.put("value", faceInfoEntityStranger.getFPID());
            jsonArrayStranger.put(0, mapStranger);
            deleteInboundDataStranger.put("FPID", jsonArrayStranger);
            Tool.faceInfoOperation(2, Egci.fdLibIDForStranger, null, deleteInboundDataStranger);
        } catch (Exception e) {
            logger.error("删除人脸服务器中的人员出错", e);
        }
    }
}
