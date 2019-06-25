package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.form.EquipmentManagementForm;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DeviceFunction {
    private JPanel deviceFunction;
    private JLabel equipmentTypeLabel;
    private JComboBox equipmentTypeCombo;
    private JLabel equipmentNameLabel;
    private JTextField equipmentNameText;
    private JLabel equipmentIpLabel;
    private JTextField equipmentIpText;
    private JLabel equipmentPermissionLabel;
    private JComboBox equipmentPermissionCombo;
    private JLabel equipmentSwitchIpLabel;
    private JTextField equipmentSwitchIpText;
    private JLabel equipmentHostIpLabel;
    private JTextField equipmentHostIpText;
    private JLabel equipmentChannelLabel;
    private JTextField equipmentChannelText;
    private JButton confirmButton;
    private JButton cancelButton;

    private JFrame frame;
    private EquipmentManagementForm equipmentManagementForm;
    private EquipmentEntity equipmentEntity;
    private Logger logger = LoggerFactory.getLogger(DeviceFunction.class);


    public DeviceFunction(EquipmentManagementForm equipmentManagementForm, EquipmentEntity equipmentEntity) {
        this.equipmentManagementForm = equipmentManagementForm;
        this.equipmentEntity = equipmentEntity;
        /*
         * 初始化设备类型选择框
         * */
        equipmentTypeCombo.addItem("请选择设备类型");
        equipmentTypeCombo.addItem("门禁一体机");
        equipmentTypeCombo.addItem("采集设备");
        equipmentTypeCombo.addItem("布控抓拍机");
        equipmentTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (equipmentTypeCombo.getSelectedIndex()) {
                        case 0:
                            frozen();
                            break;
                        case 1:
                            defrost();
                            showMJZJ();
                            break;
                        case 2:
                            defrost();
                            showCJSB();
                            break;
                        case 3:
                            defrost();
                            showZPJ();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        /*
         * 初始化设备选择框
         * */
        equipmentPermissionCombo.addItem("请选择设备权限");
        equipmentPermissionCombo.addItem("一核");
        equipmentPermissionCombo.addItem("二核");
        equipmentPermissionCombo.addItem("三核");
        /*
         * 确定按钮事件
         * */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm();
            }
        });
        /*
         * 取消按钮事件
         * */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        /*
         * 冻结内容框
         * */
        frozen();
    }

    /*
     * 确认按钮
     * */
    private void confirm() {
        if (equipmentTypeCombo.getSelectedIndex() == 0) {
            Tool.showMessage("请正确选择设备类型", "提示", 0);
            return;
        }
        if (equipmentPermissionCombo.getSelectedIndex() == 0) {
            Tool.showMessage("请正确选择设备权限", "提示", 0);
            return;
        }
        try {
            equipmentEntity.setEquipmentType(equipmentTypeCombo.getSelectedIndex());
            equipmentEntity.setEquipmentName(equipmentNameText.getText());
            equipmentEntity.setEquipmentIp(equipmentIpText.getText());
            equipmentEntity.setEquipmentPermission(equipmentPermissionCombo.getSelectedIndex());
            equipmentEntity.setEquipmentSwitchIp(equipmentSwitchIpText.getText());
            equipmentEntity.setEquipmentHostIp(equipmentHostIpText.getText());
            equipmentEntity.setEquipmentChannel(equipmentChannelText.getText());
            Egci.session.insert("mapping.equipmentMapper.addEquipment", equipmentEntity);
            Egci.session.commit();
            equipmentManagementForm.refreshEquipmentCollection();
            frame.dispose();
        } catch (Exception e) {
            logger.error("新增设备出错", e);
            Tool.showMessage("保存出错，请稍后重试", "提示", 0);
        }
    }

    /*
     * 隐藏内容框
     * */
    private void hide() {
        equipmentSwitchIpLabel.setVisible(false);
        equipmentSwitchIpText.setVisible(false);
        equipmentHostIpLabel.setVisible(false);
        equipmentHostIpText.setVisible(false);
        equipmentChannelLabel.setVisible(false);
        equipmentChannelText.setVisible(false);
    }

    /*
     * 冻结内容框
     * */
    private void frozen() {
        equipmentNameText.setEnabled(false);
        equipmentIpText.setEnabled(false);
        equipmentHostIpText.setEnabled(false);
        equipmentSwitchIpText.setEnabled(false);
        equipmentChannelText.setEnabled(false);
        equipmentPermissionCombo.setEnabled(false);
    }

    /*
     * 解冻内容框
     * */
    private void defrost() {
        equipmentNameText.setEnabled(true);
        equipmentIpText.setEnabled(true);
        equipmentHostIpText.setEnabled(true);
        equipmentSwitchIpText.setEnabled(true);
        equipmentChannelText.setEnabled(true);
        equipmentPermissionCombo.setEnabled(true);
    }

    /*
     * 显示门禁主机内容
     * */
    private void showMJZJ() {
        hide();
        equipmentSwitchIpLabel.setVisible(true);
        equipmentSwitchIpText.setVisible(true);
    }

    /*
     * 显示采集设备内容
     * */
    private void showCJSB() {
        hide();
        equipmentHostIpLabel.setVisible(true);
        equipmentHostIpText.setVisible(true);
    }

    /*
     * 显示布控抓拍机内容
     * */
    private void showZPJ() {
        hide();
        equipmentChannelLabel.setVisible(true);
        equipmentChannelText.setVisible(true);
    }

    public void init() {
        frame = new JFrame("添加设备");
        frame.setContentPane(this.deviceFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
