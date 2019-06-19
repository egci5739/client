package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.CtrlCenterEntity;
import com.dyw.client.entity.protection.MonitorPointEntity;
import com.dyw.client.entity.protection.RegionEntity;
import com.dyw.client.functionForm.EquipmentFunction;
import com.dyw.client.tool.Tool;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ResourceManagementForm {
    public JPanel getResourceManagementForm() {
        return resourceManagementForm;
    }

    private JPanel resourceManagementForm;
    private JPanel resourceManagementPanel;
    private JPanel resourceManagementTitlePanel;
    private JButton addEquipmentButton;
    private JButton deleteEquipmentButton;
    private JPanel resourceManagementContentPanel;
    private JScrollPane resourceManagementContentScroll;
    private JTable resourceManagementContentTable;

    private Logger logger = LoggerFactory.getLogger(ResourceManagementForm.class);
    private DefaultTableModel deviceManagementContentTableModel;
    private List<MonitorPointEntity> monitorPointEntityList = new ArrayList<>();//监控点列表
    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
    private List<RegionEntity> regionEntityList = new ArrayList<>();//区域列表


    public ResourceManagementForm() {
        try {
            //获取根控制中心
            ctrlCenterEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter?source=device", null).getString("ctrlCenter"), CtrlCenterEntity.class).get(0);
            //获取区域列表
            regionEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID(), null).getString("region"), RegionEntity.class);
        } catch (JSONException e) {
            logger.error("获取根控制中心和区域列表出错", e);
        }
        //初始化全部设备表格
        deviceManagementContentTableModel = new DefaultTableModel();
        //添加设备
        addEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        //删除设备或监控点
        deleteEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEquipment();
            }
        });
        //加载监控点信息
        getMonitor();
    }

    /*
     * 添加设备
     * */
    private void addEquipment() {
        EquipmentFunction equipmentFunction = new EquipmentFunction(ctrlCenterEntity, regionEntityList, this);
        equipmentFunction.init();
    }

    /*
     * 删除设备或监控点
     * */
    private void deleteEquipment() {
        if (resourceManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择设备", "提示", 0);
            return;
        }
        if (!Tool.showConfirm("确认删除", "提示")) {
            return;
        }
        try {
            //第一步：先进行撤防操作
            MonitorPointEntity monitorPointEntity = monitorPointEntityList.get(resourceManagementContentTable.getSelectedRow());
            String instructionGuard = "/ISAPI/SDT/Management/Unguard";
            org.json.JSONObject inboundDataGuard = new org.json.JSONObject();
            Map<String, Object> mapGuard = new HashMap<>();
            JSONArray jsonArrayGuard = new JSONArray();
            mapGuard.put("monitorPointID", monitorPointEntity.getMonitorPointID());
            jsonArrayGuard.put(0, mapGuard);
            inboundDataGuard.put("monitorPoint", jsonArrayGuard);
            org.json.JSONObject resultDataGuard = Tool.sendInstructionAndReceiveStatus(3, instructionGuard, inboundDataGuard);
            if (resultDataGuard.getInt("statusCode") == 1) {
                //第二步：删除设备
                String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device/delete";
                org.json.JSONObject inboundDataEquipment = new org.json.JSONObject();
                HashMap<String, Object> mapEquipment = new HashMap<String, Object>();
                mapEquipment.put("DeviceID", monitorPointEntity.getDeviceID());
                JSONArray jsonArrayEquipment = new JSONArray();
                jsonArrayEquipment.put(0, mapEquipment);
                inboundDataEquipment.put("Device", jsonArrayEquipment);
                org.json.JSONObject resultDataEquipment = Tool.sendInstructionAndReceiveStatus(2, instruction, inboundDataEquipment);
                if (resultDataEquipment.getInt("statusCode") == 1) {
//                    Tool.showMessage("删除成功", "提示", 0);
                    getMonitor();
                } else {
                    Tool.showMessage("删除设备失败，错误码：" + resultDataEquipment.getInt("statusCode"), "提示", 0);
                }
            } else {
                Tool.showMessage("撤防失败,错误码：" + resultDataGuard.getInt("statusCode"), "提示", 0);
            }
        } catch (JSONException e) {
            logger.error("删除设备或监控点出错", e);
        }
    }

    //获取全部监控点信息
    public void getMonitor() {
        monitorPointEntityList.clear();
        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/monitorPoint/search";
        org.json.JSONObject inboundData = new org.json.JSONObject();
        try {
            String[] columnResourceMonitorInfo = {"设备名称", "区域", "IP地址", "端口", "布防状态"};
            deviceManagementContentTableModel.setColumnIdentifiers(columnResourceMonitorInfo);
            resourceManagementContentTable.setModel(deviceManagementContentTableModel);
            deviceManagementContentTableModel.setRowCount(0);
            inboundData.put("searchResultPosition", 0);
            inboundData.put("maxResults", 100);
            inboundData.put("isRegion", "yes");
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
            monitorPointEntityList = JSONObject.parseArray(new org.json.JSONObject(resultData.getString("ctrlCenter")).getString("monitorPoint"), MonitorPointEntity.class);
            for (MonitorPointEntity monitorPointEntity : monitorPointEntityList) {
                Vector vector = new Vector();
                vector.add(0, monitorPointEntity.getMonitorPointName());
                vector.add(1, monitorPointEntity.getRegionName());
                vector.add(2, monitorPointEntity.getDeviceIP());
                vector.add(3, monitorPointEntity.getDevicePort());
                vector.add(4, monitorPointEntity.getIsGuard());
                deviceManagementContentTableModel.addRow(vector);
            }
            getSnapDeviceIpsList();
        } catch (JSONException e) {
//            Tool.showMessage("获取监控点失败或没有添加监控点", "提示", 0);
//            e.printStackTrace();
            logger.error("获取监控点出错", e);
        }
    }

    /*
     * 获取各核抓拍机ip集合
     * */
    private void getSnapDeviceIpsList() {
        Egci.snapDeviceIps.clear();
        Egci.snapDeviceIpsOne.clear();
        Egci.snapDeviceIpsTwo.clear();
        Egci.snapDeviceIpsThree.clear();
        for (MonitorPointEntity monitorPointEntity : monitorPointEntityList) {
            switch (monitorPointEntity.getRegionName()) {
                case "一核":
                    Egci.snapDeviceIpsOne.add(monitorPointEntity.getDeviceIP());
                    break;
                case "二核":
                    Egci.snapDeviceIpsTwo.add(monitorPointEntity.getDeviceIP());
                    break;
                case "三核":
                    Egci.snapDeviceIpsThree.add(monitorPointEntity.getDeviceIP());
                default:
                    break;
            }
        }
        switch (Egci.accountEntity.getAccountPermission()) {
            case 0:
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsOne);
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsTwo);
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsThree);
                break;
            case 1:
                Egci.snapDeviceIps = Egci.snapDeviceIpsOne;
                break;
            case 2:
                Egci.snapDeviceIps = Egci.snapDeviceIpsTwo;
                break;
            case 3:
                Egci.snapDeviceIps = Egci.snapDeviceIpsThree;
                break;
            default:
                break;
        }
    }
}