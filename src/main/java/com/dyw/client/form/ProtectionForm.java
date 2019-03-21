package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.WebcamViewerExample;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProtectionForm {
    private DefaultTableModel resourceManagementContentTableModel;

    private JPanel protection;
    private JTabbedPane tabbedPane1;
    private JPanel intelligentApplicationPanel;
    private JPanel personManagementPanel;
    private JPanel monitorManagementPanel;
    private JPanel resourceManagementPanel;
    private JPanel resourceManagementTitlePanel;
    private JPanel resourceManagementContentPanel;
    private JScrollPane resourceManagementContentScroll;
    private JTable resourceManagementContentTable;
    private JButton allEquipmentButton;
    private JButton oneMonitoryPointButton;
    private JButton twoMonitoryPointButton;
    private JButton threeMonitoryPointButton;
    private JButton addEquipmentButton;
    private JButton addMonitoryPointButton;
    private JButton deleteButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel personManagementContentPanel;
    private JPanel personManagementBasePanel;
    private JPanel personManagementBaseToolBarPanel;
    private JPanel personManagementBaseListPanel;
    private JPanel personManagementBaseConditionPanel;
    private JPanel personManagementBaseConditionSearchButtonPanel;
    private JButton personManagementBaseAddButton;
    private JButton personManagementBaseEditButton;
    private JButton personManagementBaseDeleteButton;
    private JScrollPane personManagementBaseListScroll;
    private JList personManagementBaseList;


    /*
     * 构造函数
     * */
    public ProtectionForm() {
        /*
         * 资源管理
         * */
        //初始化全部设备表格
        String[] columnResourceManagementInfo = {"设备名称", "类型", "IP地址", "端口"};
        resourceManagementContentTableModel = new DefaultTableModel();
        resourceManagementContentTableModel.setColumnIdentifiers(columnResourceManagementInfo);
        resourceManagementContentTable.setModel(resourceManagementContentTableModel);
        //获取全部设备
        allEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tool.sendInstructionAndReceiveInfo("POST", textField1.getText(), textField2.getText());
            }
        });
        /*
         * 名单管理
         * */
        //获取名单库列表
        List<FDLibEntity> libEntityList = JSONObject.parseArray(Tool.JSONStringToJSONArray(Tool.sendInstructionAndReceiveInfo("GET", "/ISAPI/Intelligent/FDLib?format=json", null), "FDLib").toJSONString(), FDLibEntity.class);
        List<String> FDLibNames = new ArrayList<>();
        for (FDLibEntity fdLibEntity : libEntityList) {
            FDLibNames.add(fdLibEntity.getName());
        }
        personManagementBaseList.setListData(FDLibNames.toArray());
    }


    public void init() {
        JFrame frame = new WebcamViewerExample();
        frame.setContentPane(this.protection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
