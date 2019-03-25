package com.dyw.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.FaceInfoEntity;
import com.dyw.client.tool.Tool;
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

    private Map<String, StaffEntity> waitStaffMap;
    private List<String> cardNumbers;
    private Map<String, StaffEntity> resultStaffMap;
    private List<Vector> vectorList;


    public Map<String, StaffEntity> getWaitStaffMap() {
        return waitStaffMap;
    }

    public List<String> getCardNumbers() {
        return cardNumbers;
    }

    public Map<String, StaffEntity> getResultStaffMap() {
        return resultStaffMap;
    }

    public List<Vector> getVectorList() {
        return vectorList;
    }

    public StaffOperationService() {
        waitStaffMap = new HashMap<String, StaffEntity>();
        cardNumbers = new ArrayList<String>();
        resultStaffMap = new HashMap<String, StaffEntity>();
        vectorList = new ArrayList<Vector>();
    }

    /*
     * 获取待拍照人员列表
     * */
    public void getWaitStaffList() {
        waitStaffMap.clear();
        cardNumbers.clear();
        List<StaffEntity> waitStaffList = Egci.session.selectList("mapping.staffMapper.getTemporaryStaff");
        for (StaffEntity staffEntity : waitStaffList) {
            waitStaffMap.put(staffEntity.getCardNumber(), staffEntity);
            cardNumbers.add(staffEntity.getCardNumber());
        }
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
        List<StaffEntity> resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCard", oldStaff.getCardNumber());
        if (resultStaffList.size() > 0) {
            staffEntity.setOldCard(oldStaff.getCardNumber());
            staffEntity.setStaffId(oldStaff.getStaffId());
            //更新
            Egci.session.update("mapping.staffMapper.updateStaff", staffEntity);
            Egci.session.commit();
            updateFaceServerFaceInfo(oldStaff, staffEntity);
        } else {
            //新增
            Egci.session.insert("mapping.staffMapper.insertStaff", staffEntity);
            Egci.session.commit();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (resultStaffList.size() > 0) {
                SendInfoSocketService deleteSendInfoSocketService = new SendInfoSocketService();
                deleteSendInfoSocketService.sendInfo("2#" + oldStaff.getCardNumber() + "\n");
                deleteSendInfoSocketService.receiveInfoOnce();
                Thread.sleep(1000);
                SendInfoSocketService insertSendInfoSocketService = new SendInfoSocketService();
                insertSendInfoSocketService.sendInfo("1#" + staffEntity.getCardNumber() + "\n");
                insertSendInfoSocketService.receiveInfoOnce();
            } else {
                SendInfoSocketService insertSendInfoSocketService = new SendInfoSocketService();
                insertSendInfoSocketService.sendInfo("1#" + staffEntity.getCardNumber() + "\n");
                insertSendInfoSocketService.receiveInfoOnce();
            }
        } catch (InterruptedException e) {
            logger.error("延迟出错", e);
        }
    }

    /*
     * 删除人员
     * */
    public void delete(StaffEntity staffEntity) {
        Egci.session.delete("mapping.staffMapper.deleteStaff", staffEntity);
        Egci.session.commit();
        SendInfoSocketService deleteSendInfoSocketService = new SendInfoSocketService();
        deleteSendInfoSocketService.sendInfo("2#" + staffEntity.getCardNumber());
        deleteSendInfoSocketService.receiveInfoOnce();
    }

    /*
     * 新增或更新待拍照人员表
     * */
    public Boolean addWaitStaff(StaffEntity staffEntity) {
        Boolean status = false;
        //判断是否卡号已经存在
        StaffEntity staffEntityStaff = Egci.session.selectOne("mapping.staffMapper.getResultStaffWithCard", staffEntity);
        StaffEntity staffEntityTemporary = Egci.session.selectOne("mapping.staffMapper.getWaitStaffWithCard", staffEntity);
        if (staffEntityStaff == null && staffEntityTemporary == null) {
            Egci.session.insert("mapping.staffMapper.insertWaitStaff", staffEntity);
            Egci.session.commit();
            status = true;
        }
        return status;
    }

    /*
     * 更新脸谱服务器中的人员信息
     * */
    public void updateFaceServerFaceInfo(StaffEntity oldInfo, StaffEntity newInfo) {
        try {
            //获取人脸库列表
//            List<FDLibEntity> fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            List<FDLibEntity> fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                String instructionGet = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
                org.json.JSONObject inboundDataGet = new org.json.JSONObject();
                inboundDataGet.put("searchResultPosition", 0);
                inboundDataGet.put("maxResults", 100);
                inboundDataGet.put("faceLibType", "blackFD");
                inboundDataGet.put("FDID", fdLibEntity.getFDID());
                inboundDataGet.put("name", oldInfo.getName() + "_" + oldInfo.getCardNumber() + "_" + oldInfo.getStaffId());
                System.out.println("看看看看：" + oldInfo.getName() + "_" + oldInfo.getCardNumber() + "_" + oldInfo.getStaffId());
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
                    Tool.showMessage("添加成功", "提示", 0);
                } else {
                    Tool.showMessage("添加失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
