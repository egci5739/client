package com.dyw.client.service;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.protection.HttpHostNotificationEntity;
import com.dyw.client.service.inter.BaseAlarmInterface;
import com.dyw.client.service.inter.BaseMonitorInterface;
import com.dyw.client.service.inter.BaseStatusInterface;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.List;

public abstract class BaseFormService implements BaseAlarmInterface, BaseMonitorInterface, BaseStatusInterface {
    private JFrame baseFrame;

    /*
     * 打开窗口
     * */
    public boolean open() {
//        baseFrame.setLocationRelativeTo(null);//居中显示
        baseFrame.setVisible(true);
        baseFrame.setExtendedState(Frame.NORMAL);
        baseFrame.toFront();
        return true;
    }

    /*
     * 关闭窗口
     * */
    public boolean close() {
        baseFrame.setVisible(false);
        return false;
    }

    /*
     * 初始化
     * */
    public void init(String name, JPanel jPanel) {
        baseFrame = new JFrame(name);
        baseFrame.setContentPane(jPanel);
//        baseFrame.setDefaultCloseOperation();
        baseFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                baseFrame.setVisible(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }
        });

        baseFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                if (baseFrame.getY() < Egci.menuHeight) {
                    baseFrame.setLocation(baseFrame.getX(), Egci.menuHeight);
                }
            }
        });
        baseFrame.pack();
//        baseFrame.setVisible(true);
    }

    @Override
    public void saveOtherEvent(AlarmEntity alarmEntity) {

    }

    @Override
    public void addAlarmInfo(AlarmEntity alarmEntity) {

    }

    @Override
    public void addPassInfo(PassRecordEntity passRecordEntity) {

    }

    @Override
    public void getEquipmentStatus() {

    }

    /*
     * 获取Panel
     * */
    public abstract JPanel getPanel();

    @Override
    public void stressAlarmSearchHistory(AlarmEntity alarmEntity) {
    }

    /*
     * 改变状态
     * */
    public void changeStatus(int status) {
    }
}
