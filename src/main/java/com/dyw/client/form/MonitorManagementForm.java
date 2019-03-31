package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.*;
import com.dyw.client.functionForm.MonitorFunction;
import com.dyw.client.tool.Tool;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MonitorManagementForm {
    public JPanel getMonitorManagementForm() {
        return monitorManagementForm;
    }

    private JPanel monitorManagementForm;
    private JPanel monitorManagementPanel;
    private JPanel monitorManagementTolBarPanel;
    private JButton monitorManagementAddButton;
    private JButton monitorManagementEditButton;
    private JButton monitorManagementDeleteButton;
    private JPanel monitorManagementContentPanel;
    private JScrollPane monitorManagementContentScroll;
    private JTable monitorManagementContentTable;

    private List<MonitorPointEntity> monitorPointEntityList = new ArrayList<>();//监控点列表
    private DefaultTableModel monitorManagementContentTableModel;
    private List<RelateInfoEntity> relateInfoEntityList = new ArrayList<>();//布控信息表
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表
    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
    private List<RegionEntity> regionEntityList = new ArrayList<>();//区域列表


    public MonitorManagementForm() {
        try {
            //获取根控制中心
            ctrlCenterEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter?source=device", null).getString("ctrlCenter"), CtrlCenterEntity.class).get(0);
            //获取区域列表
            regionEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID(), null).getString("region"), RegionEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
         * 获取抓拍机信息
         * */
        getMonitor();
        //初始化名单库人脸显示列表
        String[] columnMonitorManagementContentInfo = {"布控名称", "布控对象", "布控范围", "布控时段", "布控创建时间"};
        monitorManagementContentTableModel = new DefaultTableModel();
        monitorManagementContentTableModel.setColumnIdentifiers(columnMonitorManagementContentInfo);
        monitorManagementContentTable.setModel(monitorManagementContentTableModel);
        //获取布控列表
        getMonitorList();
        //添加布控
        monitorManagementAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMonitor();
            }
        });
        //删除布控
        monitorManagementDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMonitor();
            }
        });
        //编辑布控
        monitorManagementEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMonitor();
            }
        });
    }

    public void getMonitorList() {
        relateInfoEntityList.clear();
        monitorManagementContentTableModel.setRowCount(0);
        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json";
        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(1, instruction, null);
        try {
            relateInfoEntityList = JSONObject.parseArray(resultData.getString("relateInfo"), RelateInfoEntity.class);
            for (RelateInfoEntity relateInfoEntity : relateInfoEntityList) {
                Vector vector = new Vector();
                vector.add(0, relateInfoEntity.getName());
                vector.add(1, relateInfoEntity.getFDID());
                vector.add(2, relateInfoEntity.getCameraName());
                vector.add(3, relateInfoEntity.getPlanInfo().get(0).getStartTime() + "-" + relateInfoEntity.getPlanInfo().get(0).getEndTime());
                vector.add(4, relateInfoEntity.getCreateTime());
                monitorManagementContentTableModel.addRow(vector);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * 添加布控
     * */
    private void addMonitor() {
        getMonitor();
        MonitorFunction monitorFunction = new MonitorFunction(null, monitorPointEntityList, this);
        monitorFunction.init();
    }

    /*
     * 编辑布控
     * */
    private void editMonitor() {
        if (monitorManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择布控信息", "提示", 0);
            return;
        }
        RelateInfoEntity relateInfoEntity = relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow());
        MonitorFunction monitorFunction = new MonitorFunction(relateInfoEntity, monitorPointEntityList, this);
        monitorFunction.init();
    }

    /*
     * 删除布控
     * */
    private void deleteMonitor() {
        if (monitorManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择布控信息", "提示", 0);
            return;
        }
        if (!Tool.showConfirm("确认删除？", "提示")) {
            return;
        }
        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json&relateID=" + relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow()).getRelateID();
        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
        try {
            if (resultData.getInt("statusCode") == 1) {
                Tool.showMessage("删除成功", "提示", 0);
                getMonitorList();
            } else {
                Tool.showMessage("删除设备失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取全部监控点信息
     * */
    private void getMonitor() {
        monitorPointEntityList.clear();
        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/monitorPoint/search";
        org.json.JSONObject inboundData = new org.json.JSONObject();
        try {
            inboundData.put("searchResultPosition", 0);
            inboundData.put("maxResults", 100);
            inboundData.put("isRegion", "yes");
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
            monitorPointEntityList = JSONObject.parseArray(new org.json.JSONObject(resultData.getString("ctrlCenter")).getString("monitorPoint"), MonitorPointEntity.class);
        } catch (JSONException e) {
            Tool.showMessage("获取监控点失败或没有添加监控点", "提示", 0);
            e.printStackTrace();
        }
    }
}
