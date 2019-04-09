package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.PassPhotoTableCellRenderer;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;
import java.util.Vector;

public class MonitorRealTimeForm {
    public JPanel getMonitorRealTimePanel() {
        return monitorRealTimePanel;
    }

    private JPanel monitorRealTimePanel;
    private JPanel monitorRealTime;
    private JPanel passSuccessPanel;
    private JPanel passSuccessTitlePanel;
    private JLabel passSuccessTitleLabel;
    private JCheckBox passSuccessRollingCheckBox;
    private JButton paaSuccessClearButton;
    private JPanel passSuccessContentPanel;
    private JScrollPane passSuccessContentScroll;
    private JTable passSuccessContentTable;
    private JPanel passFaultPanel;
    private JPanel passFaultTitlePanel;
    private JLabel passFaultTitleLabel;
    private JCheckBox passFaultRollingCheckBox;
    private JButton paaFaultClearButton;
    private JPanel passFaultContentPanel;
    private JScrollPane passFaultContentScroll;
    private JTable passFaultContentTable;
    private JPanel passAlarmPanel;
    private JPanel passAlarmTitlePanel;
    private JLabel passAlarmTitleLabel;
    private JPanel passAlarmContentPanel;

    private DefaultTableModel passSuccessModel;
    private DefaultTableModel passFaultModel;
    private JScrollBar passSuccessScrollBar;//通行成功滚动条
    private int passSuccessRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private JScrollBar passFaultScrollBar;//通行成功滚动条
    private int passFaultRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private int passSuccessBottomStatus = 0;
    private int passFaultBottomStatus = 0;

    public MonitorRealTimeForm() {
        //创建接收通行信息的socket对象
        MonitorReceiveInfoSocketService monitorReceiveInfoSocketService = new MonitorReceiveInfoSocketService(this);
        monitorReceiveInfoSocketService.sendInfo(Tool.getAccessPermissionInfo(Egci.accountEntity.getAccountPermission()));
        monitorReceiveInfoSocketService.start();
        //初始化通行结果表格
        String[] columnPassInfo = {"人员底图", "抓拍图片", "比对信息"};
        passSuccessModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passSuccessModel.setColumnIdentifiers(columnPassInfo);
        passSuccessContentTable.setModel(passSuccessModel);
        passFaultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passFaultModel.setColumnIdentifiers(columnPassInfo);
        passFaultContentTable.setModel(passFaultModel);
        //表格中显示图片
        TableCellRenderer passTableCellRenderer = new PassPhotoTableCellRenderer();
        passSuccessContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
        passFaultContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
        passSuccessContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passSuccessBottomStatus <= 3) {
                    passSuccessContentScroll.getVerticalScrollBar().setValue(passSuccessContentScroll.getVerticalScrollBar().getModel().getMaximum() - passSuccessContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passSuccessBottomStatus++;
                }
            }
        });
        passSuccessContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passFaultContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passFaultBottomStatus <= 3) {
                    passFaultContentScroll.getVerticalScrollBar().setValue(passFaultContentScroll.getVerticalScrollBar().getModel().getMaximum() - passFaultContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passFaultBottomStatus++;
                }
            }
        });
        passFaultContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passSuccessScrollBar = passSuccessContentScroll.getVerticalScrollBar();
        passFaultScrollBar = passFaultContentScroll.getVerticalScrollBar();
        //通行成功是否滚动
        passSuccessRollingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    passSuccessRollingStatus = 1;
                } else {
                    passSuccessRollingStatus = 0;
                }
            }
        });
        //清空通行成功记录
        paaSuccessClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passSuccessModel.setRowCount(0);
            }
        });
        //通行失败是否滚动
        passFaultRollingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    passFaultRollingStatus = 1;
                } else {
                    passFaultRollingStatus = 0;
                }
            }
        });
        //清空通行失败记录
        paaFaultClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passFaultModel.setRowCount(0);
            }
        });
    }

    /*
     * 新增通行记录
     * */
    public void addPassInfo(PassInfoEntity passInfoEntity) {
        Vector v = new Vector();
        v.add(0, Base64.encodeBytes(passInfoEntity.getPhoto()));
        v.add(1, Base64.encodeBytes(passInfoEntity.getCapturePhoto()));
        if (passInfoEntity.getPass()) {
            v.add(2, Tool.displayPassSuccessResult(passInfoEntity));
            passSuccessModel.addRow(v);
            if (passSuccessRollingStatus == 1) {
                moveScrollBarToBottom(passSuccessScrollBar);
            }
            passSuccessBottomStatus = 0;
        } else {
            v.add(2, Tool.displayPassFaultResult(passInfoEntity));
            passFaultModel.addRow(v);
            if (passFaultRollingStatus == 1) {
                moveScrollBarToBottom(passFaultScrollBar);
            }
            passFaultBottomStatus = 0;
        }
    }

    /*
     * 将滚动条移到底部
     * */
    private void moveScrollBarToBottom(JScrollBar jScrollBar) {
        if (jScrollBar != null) {
            jScrollBar.setValue(jScrollBar.getMaximum());
        }
    }
}