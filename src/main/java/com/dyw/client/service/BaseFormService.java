package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.protection.AlarmResultEntity;
import com.dyw.client.service.inter.BaseAlarmInterface;
import com.dyw.client.service.inter.BaseMonitorInterface;
import com.dyw.client.service.inter.BaseStatusInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        //监听窗口变化
        baseFrame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getOldState() != e.getNewState()) {
                    switch (e.getNewState()) {
                        case Frame.MAXIMIZED_BOTH:
                            baseFrame.setBounds((int) baseFrame.getBounds().getX(), (int) baseFrame.getBounds().getY(), (int) baseFrame.getBounds().getWidth(), (int) (baseFrame.getBounds().getHeight() - Egci.menuHeight));
                            // 最大化
                            break;
                        case Frame.ICONIFIED:
                            // 最小化
                            break;
                        case Frame.NORMAL:
                            baseFrame.setBounds((int) baseFrame.getBounds().getX(), (int) baseFrame.getBounds().getY(), (int) baseFrame.getBounds().getWidth(), (int) (baseFrame.getBounds().getHeight() - Egci.menuHeight));
                            // 恢复
                            break;
                        default:
                            break;
                    }
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

    /*
     * 显示布控报警信息
     * */
    public void showAlarmInfo(AlarmResultEntity alarmResultEntity) {
    }
}
