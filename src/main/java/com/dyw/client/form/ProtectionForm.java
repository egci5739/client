package com.dyw.client.form;

import com.dyw.client.controller.WebcamViewerExample;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

            }
        });
    }

    public void init() {
        JFrame frame = new WebcamViewerExample();
        frame.setContentPane(this.protection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
