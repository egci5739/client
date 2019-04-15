package com.dyw.client.service;

import com.alibaba.fastjson.JSON;
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

import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StaffOperationService {
    private Logger logger = LoggerFactory.getLogger(StaffOperationService.class);

    private Map<String, StaffEntity> resultStaffMap;
    private List<Vector> vectorList;

    public Map<String, StaffEntity> getResultStaffMap() {
        return resultStaffMap;
    }

    public List<Vector> getVectorList() {
        return vectorList;
    }

    public StaffOperationService() {
        resultStaffMap = new HashMap<String, StaffEntity>();
        vectorList = new ArrayList<Vector>();
    }

    /*
     * 获取待拍照人员列表
     * */
    public List<StaffEntity> getWaitStaffList() {
        return Egci.session.selectList("mapping.staffMapper.getTemporaryStaff");
    }

    /*
     * 查询人员
     * */
    public void search(String name, String card) {
        resultStaffMap.clear();
        vectorList.clear();
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setName(Tool.getSearchCondition(name));
        staffEntity.setCardNumber(Tool.getSearchCondition(card));
        List<StaffEntity> resultStaffList = null;
        if (!name.equals("") && !card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCardAndName", staffEntity);
        } else if (!name.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithName", staffEntity);
        } else if (!card.equals("")) {
            resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCard", staffEntity);
        }
        int i = 0;
        assert resultStaffList != null;
        for (StaffEntity staffEntity1 : resultStaffList) {
            resultStaffMap.put(i + "", staffEntity1);
            Vector v = new Vector();
            v.add(0, staffEntity1.getCardNumber());
            v.add(1, staffEntity1.getName());
            v.add(2, staffEntity1.getCardId());
            vectorList.add(v);
            i = i + 1;
        }
    }

    /*
     * 保存人员信息
     * */
    public void save(StaffEntity staffEntity, StaffEntity oldStaff) {
        try {
            if (staffEntity.getSex().equals("")) {
                staffEntity.setSex("1");
            }
            if (staffEntity.getBirthday().equals("")) {
                staffEntity.setBirthday("1970-01-01");
            }
            if (oldStaff.getStaffId() > 0) {
                staffEntity.setOldCard(oldStaff.getCardNumber());
                staffEntity.setStaffId(oldStaff.getStaffId());
                //更新
                Egci.session.update("mapping.staffMapper.updateStaff", staffEntity);
                Egci.session.commit();
                //更新脸谱，暂时不用
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
            SendInfoSocketService insertSendInfoSocketService = new SendInfoSocketService();
            insertSendInfoSocketService.sendInfo("1#" + staffEntity.getCardNumber() + "\n");
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
        SendInfoSocketService deleteSendInfoSocketService = new SendInfoSocketService();
        deleteSendInfoSocketService.sendInfo("2#" + staffEntity.getCardNumber());
        deleteSendInfoSocketService.receiveInfoOnce();
        //删除人脸服务器中人脸信息
        if (Egci.faceServerStatus == 1) {
            deleteFaceServerFaceInfo(staffEntity);
        }
        staffEntity.setCardNumber("0");
        Egci.session.delete("mapping.staffMapper.deleteStaff", staffEntity);
        Egci.session.commit();
    }

    /*
     * 新增或更新待拍照人员表
     * */
    public Boolean addWaitStaff(StaffEntity staffEntity) {
        Boolean status = false;
        //判断是否卡号已经存在
        List<StaffEntity> staffEntityStaff = Egci.session.selectList("mapping.staffMapper.getStaffWithCard", staffEntity.getCardNumber());
        List<StaffEntity> staffEntityTemporary = Egci.session.selectList("mapping.staffMapper.getWaitStaffWithCard", staffEntity);
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
                    inboundDataGet.put("name", oldInfo.getName() + "_" + oldInfo.getCardNumber() + "_" + oldInfo.getStaffId());
                    FaceInfoEntity faceInfoEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instructionGet, inboundDataGet).getString("MatchList"), FaceInfoEntity.class).get(0);
                    //更改信息
                    org.json.JSONObject inboundDataSet = new org.json.JSONObject();
                    String instructionSet = "/ISAPI/Intelligent/FDLib/FDSearch?format=json&FDID=" + fdLibEntity.getFDID() + "&FPID=" + faceInfoEntity.getFPID() + "&faceLibType=blackFD";
                    inboundDataSet.put("faceURL", faceInfoEntity.getFaceURL());
                    inboundDataSet.put("faceLibType", "blackFD");
                    inboundDataSet.put("name", newInfo.getName() + "_" + newInfo.getCardNumber() + "_" + newInfo.getStaffId());//名字_卡号_id
                    inboundDataSet.put("gender", Tool.changeGenderToMaleAndFemale(newInfo.getSex()));
                    inboundDataSet.put("bornTime", newInfo.getBirthday());
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
            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, Egci.fdLibIDForStaff, staffEntity.getPhoto(), null);
            //添加到电厂人员库
            org.json.JSONObject inboundDataStaff = new org.json.JSONObject();
            inboundDataStaff.put("faceURL", resultFaceUrlData.getString("URL"));
            inboundDataStaff.put("faceLibType", "blackFD");
            inboundDataStaff.put("FDID", Egci.fdLibIDForStaff);
            inboundDataStaff.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
            if (staffEntity.getSex() == null) {
                staffEntity.setSex("0");//unknown
            }
            inboundDataStaff.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
            if (staffEntity.getBirthday() == null) {
                staffEntity.setBirthday("1900-01-01");
            }
            inboundDataStaff.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getBirthday()));
            Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataStaff);
            //添加到陌生人库
            org.json.JSONObject inboundDataStranger = new org.json.JSONObject();
            inboundDataStranger.put("faceURL", resultFaceUrlData.getString("URL"));
            inboundDataStranger.put("faceLibType", "blackFD");
            inboundDataStranger.put("FDID", Egci.fdLibIDForStranger);
            inboundDataStranger.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
            if (staffEntity.getSex() == null) {
                staffEntity.setSex("0");//unknown
            }
            inboundDataStranger.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
            if (staffEntity.getBirthday() == null || staffEntity.getBirthday().equals("")) {
                staffEntity.setBirthday("1900-01-01");
            }
            inboundDataStranger.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getBirthday()));
            Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataStranger);
        } catch (JSONException e) {
            logger.error("添加人员出错，卡号：" + staffEntity.getCardNumber());
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
            inboundDataGetStaff.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());
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
            inboundDataGetStranger.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());
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
