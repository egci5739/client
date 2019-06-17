package com.dyw.client.form;

import com.dyw.client.controller.Egci;

import javax.swing.*;

public class ApplicationForm {
    private JFrame frame;
    private JPanel applicationForm;
    private JTabbedPane applicationTabbedPane;
    private JButton monitorEnterButton;
    private JTextField monitorText;

    /*
     * 构造函数
     * init
     * */
    public ApplicationForm() {
        /*
         * 功能与编号对照
         *
         * 0：initRegisterForm：办证
         * 1：initMonitorRealTimeForm：实时通行记录
         * 2：initMonitorHistoryForm：历史通行记录
         * 3：initIntelligentApplicationForm：智能应用
         * 4：initAccountManagementForm：用户管理
         * 5：initDataAnalysisForm：数据分析
         * 6：initEquipmentManagementForm：设备管理
         * 7：initPersonManagementForm：布控名单管理
         * 8：initResourceManagementForm：布控设备管理
         * 9：initAlarmHistoryForm：布控报警历史
         * 10：initExportStaffForm：导出人员信息
         * 11：initFaultSummationForm：失败次数统计
         * */
        String[] functions = Egci.accountEntity.getAccountFunction().split(",");
        for (String function : functions) {
            switch (Integer.parseInt(function)) {
                case 0:
                    initRegisterForm();
                    break;
                case 1:
                    initMonitorRealTimeForm();
                    break;
                case 2:
                    initMonitorHistoryForm();
                case 3:
                    initIntelligentApplicationForm();
                    break;
                case 4:
                    initAccountManagementForm();
                    break;
                case 5:
                    initDataAnalysisForm();
                    break;
                case 6:
                    initEquipmentManagementForm();
                    break;
                case 7:
                    initPersonManagementForm();
                    break;
                case 8:
                    initResourceManagementForm();
                    break;
                case 9:
                    initAlarmHistoryForm();
                    break;
                case 10:
                    initExportStaffForm();
                    break;
                case 11:
                    initFaultSummationForm();
                    break;
                default:
                    break;
            }
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
     * 智能应用
     * init
     * */
    private void initIntelligentApplicationForm() {
        if (Egci.faceServerStatus == 0) {
            return;
        }
        IntelligentApplicationForm intelligentApplicationForm = new IntelligentApplicationForm();
        applicationTabbedPane.add("智能应用", intelligentApplicationForm.getIntelligentApplicationForm());
    }

    /*
     * 通行历史记录查询
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
        if (Egci.faceServerStatus == 0) {
            return;
        }
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
        if (Egci.faceServerStatus == 0) {
            return;
        }
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
        monitorEnterButton = registerForm.getSearchButton();
        monitorText = registerForm.getChineseNameText();
    }

    /*
     * 布控设备管理
     * init
     * */
    private void initResourceManagementForm() {
        if (Egci.faceServerStatus == 0) {
            return;
        }
        ResourceManagementForm resourceManagementForm = new ResourceManagementForm();
        applicationTabbedPane.add("布控设备管理", resourceManagementForm.getResourceManagementForm());
    }

    /*
     * 布控报警历史查询
     * init
     * */
    private void initAlarmHistoryForm() {
        if (Egci.faceServerStatus == 0) {
            return;
        }
        AlarmHistoryForm alarmHistoryForm = new AlarmHistoryForm();
        applicationTabbedPane.add("报警历史", alarmHistoryForm.getAlarmHistoryForm());
    }

    /*
     * 导出人员信息到excel
     * init
     * */
    private void initExportStaffForm() {
        ExportStaffForm exportStaffForm = new ExportStaffForm();
        applicationTabbedPane.add("导出人员信息", exportStaffForm.getExportStaffForm());
    }

    /*
     * 失败次数统计
     * init
     * */
    private void initFaultSummationForm() {
        FaultSummationForm faultSummationForm = new FaultSummationForm();
        applicationTabbedPane.add("失败次数统计", faultSummationForm.getFaultSummation());
    }

    public void init() {
        frame = new JFrame("ApplicationForm");
        frame.setContentPane(this.applicationForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getRootPane().setDefaultButton(monitorEnterButton);
        frame.setVisible(true);
        try {
            monitorText.requestFocus();
        } catch (Exception ignored) {
        }
    }
}
