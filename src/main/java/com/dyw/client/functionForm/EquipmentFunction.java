package com.dyw.client.functionForm;
/*
 * 添加布控设备的类
 * */

import com.dyw.client.entity.protection.CtrlCenterEntity;
import com.dyw.client.entity.protection.DeviceEntity;
import com.dyw.client.entity.protection.MonitorPointEntity;
import com.dyw.client.entity.protection.RegionEntity;
//import com.dyw.client.form.ProtectionForm;
import com.dyw.client.form.ResourceManagementForm;
import com.dyw.client.tool.Tool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentFunction {
    private JFrame frame;
    private JPanel equipmentFunction;
    private JTextField equipmentNameText;
    private JTextField equipmentIpText;
    private JTextField equipmentPortText;
    private JTextField equipmentUserText;
    private JPasswordField equipmentPassText;
    private JButton equipmentConfirmButton;
    private JButton equipmentCancelButton;
    private JLabel equipmenNameLabel;
    private JLabel equipmentIpLabel;
    private JLabel equipmentPortLabel;
    private JLabel equipmentUserLabel;
    private JLabel equipmentPassLabel;
    private JComboBox equipmentAreaCombo;
    private JLabel equipmentAreaLabel;

    public EquipmentFunction(final CtrlCenterEntity ctrlCenterEntity, final List<RegionEntity> regionEntityList, final ResourceManagementForm resourceManagementForm) {
        equipmentAreaCombo.addItem("一核");
        equipmentAreaCombo.addItem("二核");
        equipmentAreaCombo.addItem("三核");
        //确认
        equipmentConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String instructionAddEquipment = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device";
                JSONObject inboundDataIn = new JSONObject();
                JSONObject inboundDataOut = new JSONObject();
                DeviceEntity deviceEntity = getDeviceInfo();
                if (deviceEntity == null) {
                    return;
                }
                //添加设备
                try {
                    inboundDataIn.put("deviceName", deviceEntity.getDeviceName());
                    inboundDataIn.put("deviceIP", deviceEntity.getDeviceIP());
                    inboundDataIn.put("devicePort", deviceEntity.getDevicePort());
                    inboundDataIn.put("deviceUser", deviceEntity.getDeviceUser());
                    inboundDataIn.put("devicePassword", deviceEntity.getDevicePassword());
                    inboundDataIn.put("protocolType", "HIK");
                    inboundDataOut.put("device", inboundDataIn);
                    JSONObject resultDataEquipment = Tool.sendInstructionAndReceiveStatusAndData(3, instructionAddEquipment, inboundDataOut);
                    if (resultDataEquipment.getInt("statusCode") == 1) {
                        //获取监控点id
                        List<MonitorPointEntity> monitorPointEntityList = com.alibaba.fastjson.JSONObject.parseArray(resultDataEquipment.getString("monitorPoint"), MonitorPointEntity.class);
                        //添加监控点
                        String regionId = "";
                        switch ((String) equipmentAreaCombo.getSelectedItem()) {
                            case "一核":
                                regionId = regionEntityList.get(0).getRegionID();
                                break;
                            case "二核":
                                regionId = regionEntityList.get(1).getRegionID();
                                break;
                            case "三核":
                                regionId = regionEntityList.get(2).getRegionID();
                                break;
                            default:
                                break;
                        }
                        String instructionAddMonitor = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/region/" + regionId + "/monitorPoint";
                        String monitorId = monitorPointEntityList.get(0).getMonitorPointID();
                        JSONObject inboundDataAddMonitor = new JSONObject();
                        Map<String, Object> map = new HashMap<>();
                        JSONArray jsonArray = new JSONArray();
                        map.put("monitorPointID", monitorId);
                        jsonArray.put(0, map);
                        inboundDataAddMonitor.put("monitorPoint", jsonArray);
                        JSONObject resultDataMonitor = Tool.sendInstructionAndReceiveStatus(3, instructionAddMonitor, inboundDataAddMonitor);
                        if (resultDataMonitor.getInt("statusCode") == 1) {
                            //对设备布防
                            String instructionGuard = "/ISAPI/SDT/Management/Guard";
                            JSONObject resultGuard = Tool.sendInstructionAndReceiveStatus(3, instructionGuard, inboundDataAddMonitor);
                            if (resultGuard.getInt("statusCode") == 1) {
                                resourceManagementForm.getMonitor();
                                frame.dispose();
                            } else {
                                Tool.showMessage("添加布防失败，错误信息" + resultGuard.getString("errorMsg"), "提示", 0);
                            }
                        } else {
                            Tool.showMessage("添加监控点失败，错误信息" + resultDataMonitor.getString("errorMsg"), "提示", 0);
                        }
                    } else {
                        Tool.showMessage("添加设备失败，错误信息" + resultDataEquipment.getString("errorMsg"), "提示", 0);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //取消
        equipmentCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    /*
     * 获取设备输入信息
     * */
    public DeviceEntity getDeviceInfo() {
        DeviceEntity deviceEntity = new DeviceEntity();
        String equipmentName = equipmentNameText.getText();
        String equipmentIp = equipmentIpText.getText();
        String equipmentPort = equipmentPortText.getText();
        String equipmentUser = equipmentUserText.getText();
        String equipmentPass = equipmentPassText.getText();
        if (equipmentName.equals("") || equipmentIp.equals("") || equipmentPort.equals("") || equipmentUser.equals("") || equipmentPass.equals("")) {
            Tool.showMessage("请完善设备信息", "提示", 0);
            deviceEntity = null;
        } else {
            deviceEntity.setDeviceName(equipmentName);
            deviceEntity.setDeviceIP(equipmentIp);
            deviceEntity.setDevicePort(Integer.parseInt(equipmentPort));
            deviceEntity.setDeviceUser(equipmentUser);
            deviceEntity.setDevicePassword(equipmentPass);
        }
        return deviceEntity;
    }

    public void init() {
        frame = new JFrame("EquipmentFunction");
        frame.setContentPane(this.equipmentFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
