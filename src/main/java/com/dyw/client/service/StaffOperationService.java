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
    public void save(StaffEntity staffEntity, String oldCard) {
        List<StaffEntity> resultStaffList = Egci.session.selectList("mapping.staffMapper.getResultStaffWithCard", oldCard);
        if (resultStaffList.size() > 0) {
            //更新
            Egci.session.update("mapping.staffMapper.updateStaff", staffEntity);
            Egci.session.commit();
        } else {
            //新增
            Egci.session.insert("mapping.staffMapper.insertStaff", staffEntity);
            Egci.session.commit();
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
