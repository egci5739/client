package com.dyw.client.form;

import ISAPI.HttpsClientUtil;
import com.dyw.client.controller.Egci;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class ApplicationForm {
    private JFrame frame;
    private JPanel applicationForm;
    private JTabbedPane applicationTabbedPane;

    public ApplicationForm() {
        switch (Egci.accountEntity.getAccountRole()) {
            case 1:
                initRegisterForm();
                break;
            case 2:
                initMonitorRealTimeForm();
                initMonitorHistoryForm();
                break;
            case 3:
                initIntelligentApplicationForm();
                break;
            case 0:
                initAccountManagementForm();
                initDataAnalysisForm();
                initEquipmentManagementForm();
                initFaceCollectionManagementForm();
                initIntelligentApplicationForm();
                initMonitorHistoryForm();
                initMonitorManagementForm();
                initMonitorRealTimeForm();
                initPersonManagementForm();
                initResourceManagementForm();
                initAlarmHistoryForm();
        }
    }

    /*
     * 系统用户管理
     * init
     * */
    private void initAccountManagementForm() {
        AccountManagementForm accountManagementForm = new AccountManagementForm();
        applicationTabbedPane.add("系统用户管理", accountManagementForm.getAccountManagementForm());
    }

    /*
     * 数据分析
     * init
     * */
    private void initDataAnalysisForm() {
        DataAnalysisForm dataAnalysisForm = new DataAnalysisForm();
        applicationTabbedPane.add("数据分析", dataAnalysisForm.getDataAnalysisForm());
    }

    /*
     * 一体机管理
     * init
     * */
    private void initEquipmentManagementForm() {
        EquipmentManagementForm equipmentManagementForm = new EquipmentManagementForm();
        applicationTabbedPane.add("设备管理", equipmentManagementForm.getEquipmentManagementForm());
    }

    /*
     * 采集设备管理
     * init
     * */
    private void initFaceCollectionManagementForm() {
        FaceCollectionManagementForm faceCollectionManagementForm = new FaceCollectionManagementForm();
        applicationTabbedPane.add("采集设备管理", faceCollectionManagementForm.getFaceCollectionManagementForm());
    }

    /*
     * 智能应用
     * init
     * */
    private void initIntelligentApplicationForm() {
        IntelligentApplicationForm intelligentApplicationForm = new IntelligentApplicationForm();
        applicationTabbedPane.add("智能应用", intelligentApplicationForm.getIntelligentApplicationForm());
    }

    /*
     * 历史记录查询
     * init
     * */
    private void initMonitorHistoryForm() {
        MonitorHistoryForm monitorHistoryForm = new MonitorHistoryForm();
        applicationTabbedPane.add("历史记录查询", monitorHistoryForm.getMonitorHistoryForm());
    }

    /*
     * 布控管理
     * init
     * */
    private void initMonitorManagementForm() {
        MonitorManagementForm monitorManagementForm = new MonitorManagementForm();
        applicationTabbedPane.add("布控管理", monitorManagementForm.getMonitorManagementForm());
    }

    /*
     * 实时监控
     * init
     * */
    private void initMonitorRealTimeForm() {
        MonitorRealTimeForm monitorRealTimeForm = new MonitorRealTimeForm();
        applicationTabbedPane.add("实时监控", monitorRealTimeForm.getMonitorRealTimePanel());
    }

    /*
     * 布控名单管理
     * init
     * */
    private void initPersonManagementForm() {
        PersonManagementForm personManagementForm = new PersonManagementForm();
        applicationTabbedPane.add("布控名单管理", personManagementForm.getPersonManagementForm());
    }

    /*
     * 办证端
     * init
     * */
    private void initRegisterForm() {
        RegisterForm registerForm = new RegisterForm();
        registerForm.init();
        applicationTabbedPane.add("办证端", registerForm.getRegisterForm());
    }

    /*
     * 布控设备管理
     * init
     * */
    private void initResourceManagementForm() {
        ResourceManagementForm resourceManagementForm = new ResourceManagementForm();
        applicationTabbedPane.add("布控设备管理", resourceManagementForm.getResourceManagementForm());
    }

    /*
     * 布控报警历史查询
     * init
     * */
    private void initAlarmHistoryForm() {
        AlarmHistoryForm alarmHistoryForm = new AlarmHistoryForm();
        applicationTabbedPane.add("报警历史", alarmHistoryForm.getAlarmHistoryForm());
    }

    public void init() {
        frame = new JFrame("ApplicationForm");
        frame.setContentPane(this.applicationForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
