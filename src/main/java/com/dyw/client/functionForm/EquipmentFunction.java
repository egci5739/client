package com.dyw.client.functionForm;

import com.dyw.client.entity.protection.CtrlCenterEntity;
import com.dyw.client.entity.protection.DeviceEntity;
import com.dyw.client.form.ProtectionForm;
import com.dyw.client.tool.Tool;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EquipmentFunction {
    private JFrame frame;
    private JPanel equipmentFunction;
    private JTextField equipmenNameText;
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

    public EquipmentFunction(final CtrlCenterEntity ctrlCenterEntity, final ProtectionForm protectionForm) {
        //确认
        equipmentConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device";
                JSONObject inboundDataIn = new JSONObject();
                JSONObject inboundDataOut = new JSONObject();
                DeviceEntity deviceEntity = getDeviceInfo();
                if (deviceEntity == null) {
                    return;
                }
                try {
                    inboundDataIn.put("deviceName", deviceEntity.getDeviceName());
                    inboundDataIn.put("deviceIP", deviceEntity.getDeviceIP());
                    inboundDataIn.put("devicePort", deviceEntity.getDevicePort());
                    inboundDataIn.put("deviceUser", deviceEntity.getDeviceUser());
                    inboundDataIn.put("devicePassword", deviceEntity.getDevicePassword());
                    inboundDataIn.put("protocolType", "HIK");
                    inboundDataOut.put("device", inboundDataIn);
                    JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataOut);
                    if (resultData.getInt("statusCode") == 1) {
                        Tool.showMessage("添加成功", "提示", 0);
                        protectionForm.getDevice();
                        frame.dispose();
                    } else {
                        Tool.showMessage("添加失败，错误信息" + resultData.getString("errorMsg"), "提示", 0);
                    }
                    System.out.println(inboundDataOut);
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
        String equipmentName = equipmenNameText.getText();
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
        frame.setVisible(true);
    }
}
