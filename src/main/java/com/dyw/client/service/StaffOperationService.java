package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.tool.Tool;
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
        try {
            waitStaffMap.clear();
            cardNumbers.clear();
            String sql = "select * from TemporaryStaff";
            ResultSet rs = null;
            rs = Egci.statement.executeQuery(sql);
            while (rs.next()) {
                //如果对象中有数据，就会循环打印出来
                StaffEntity staffEntity = new StaffEntity();
                staffEntity.setName(rs.getString("Name"));
                staffEntity.setNameEn(rs.getString("NameEn"));
                staffEntity.setCardId(rs.getString("CardId"));
                staffEntity.setCardNumber(rs.getString("CardNumber"));
                staffEntity.setBirthday(rs.getString("Birthday"));
                staffEntity.setSex(rs.getString("Sex"));
                staffEntity.setCompany(rs.getString("Company"));
                waitStaffMap.put(rs.getString("CardNumber"), staffEntity);
                cardNumbers.add(rs.getString("CardNumber"));
            }
        } catch (Exception e) {
            logger.error("获取待拍照人员列表失败", e);
        }
    }

    /*
     * 查询人员
     * */
    public void search(String name, String card) {
        resultStaffMap.clear();
        vectorList.clear();
        String sql = null;
        if (!name.equals("") && !card.equals("")) {
            sql = "select * from Staff WHERE Name LIKE " + Tool.getSearchCondition(name) + " OR CardNumber LIKE " + Tool.getSearchCondition(card);
        } else if (!name.equals("")) {
            sql = "select * from Staff WHERE Name LIKE " + Tool.getSearchCondition(name);
        } else if (!card.equals("")) {
            sql = "select * from Staff WHERE CardNumber LIKE " + Tool.getSearchCondition(card);
        }
        try {
            ResultSet rs = null;
            rs = Egci.statement.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                //如果对象中有数据，就会循环打印出来
                StaffEntity staffEntity = new StaffEntity();
                staffEntity.setStaffId(rs.getInt("StaffId"));
                staffEntity.setName(rs.getString("Name"));
                staffEntity.setNameEn(rs.getString("NameEn"));
                staffEntity.setCardId(rs.getString("CardId"));
                staffEntity.setCardNumber(rs.getString("CardNumber"));
                staffEntity.setBirthday(rs.getString("Birthday"));
                staffEntity.setSex(rs.getString("Sex"));
                staffEntity.setCompany(rs.getString("Company"));
                staffEntity.setPhoto(rs.getBytes("Photo"));
                resultStaffMap.put(i + "", staffEntity);
                Vector v = new Vector();
                v.add(0, rs.getString("CardNumber"));
                v.add(1, rs.getString("Name"));
                v.add(2, rs.getString("CardId"));
                vectorList.add(v);
                i = i + 1;
            }
        } catch (Exception e) {
            logger.error("获取人员列表失败", e);
        }
    }

    /*
     * 保存人员信息
     * */
    public void save(StaffEntity staffEntity, String oldCard) {
        String sql = "select * from Staff where CardNumber = " + "'" + staffEntity.getCardNumber() + "'";
        try {
            ResultSet rs = Egci.statement.executeQuery(sql);
            if (rs.next()) {
                //更新
                Egci.session.update("mapping.staffMapper.updateStaff", staffEntity);
                Egci.session.commit();
            } else {
                //新增
                Egci.session.insert("mapping.staffMapper.insertStaff", staffEntity);
                Egci.session.commit();
            }
        } catch (SQLException e) {
            logger.error("查询失败", e);
        }
        try {
            if (!oldCard.equals("0")) {
                SendInfoSocketService deleteSendInfoSocketService = new SendInfoSocketService();
                deleteSendInfoSocketService.sendInfo("2#" + oldCard + "\n");
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
}
