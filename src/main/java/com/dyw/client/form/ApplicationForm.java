package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.HttpHostNotificationEntity;
import com.dyw.client.form.guard.AccessRecordForm;
import com.dyw.client.form.guard.AlarmForm;
import com.dyw.client.form.guard.EquipmentTreeForm;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.NetStateService;
import com.dyw.client.timer.MonitorStatusTimer;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.List;

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
         * 12：initHistoryVideoForm：历史通行视频回放
         * 13：initEquipmentTreeForm：设备状态树
         * 14：initAccessRecordForm：实时通行-新
         * 15：initAlarmForm：报警记录
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
                    break;
                case 3:
                    initIntelligentApplicationForm();
                    break;
                case 4:
                    initAccountManagementForm();
                    break;
//                case 5:
//                    initDataAnalysisForm();
//                    break;
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
//                case 11:
//                    initFaultSummationForm();
//                    break;
                case 12:
                    initHistoryVideoForm();
                    break;
                case 13:
                    initEquipmentTreeForm();
                    break;
                case 14:
                    initAccessRecordForm();
                    break;
                case 15:
                    initAlarmForm();
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
        Egci.monitorRealTimeForm = new MonitorRealTimeForm();
        MonitorStatusTimer monitorStatusTimer = new MonitorStatusTimer(Egci.monitorRealTimeForm);
        monitorStatusTimer.open();
        applicationTabbedPane.add("实时监控", Egci.monitorRealTimeForm.getMonitorRealTimePanel());
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

    /*
     * 历史通行视频回放
     * init
     * */
    private void initHistoryVideoForm() {
        if (Egci.faceServerStatus == 0) {
            return;
        }
        HistoryVideoForm historyVideoForm = new HistoryVideoForm();
        applicationTabbedPane.add("通行视频记录", historyVideoForm.getHistoryVideoForm());
    }

    /*
     * 设备状态树
     * init
     * */
    private void initEquipmentTreeForm() {
        Egci.equipmentTreeForm = new EquipmentTreeForm();
//        applicationTabbedPane.add("设备状态树", treeForm.getEquipmentTreeForm());
        Egci.equipmentTreeForm.init();
    }

    /*
     * 实时通行-新
     * initAccessRecordForm
     * */
    private void initAccessRecordForm() {
        Egci.accessRecordForm = new AccessRecordForm();
        Egci.accessRecordForm.init();
    }

    /*
     * 报警记录
     * init
     * */
    private void initAlarmForm() {
        Egci.alarmForm = new AlarmForm();
        Egci.alarmForm.init();
    }

    public void init() {
        frame = new JFrame("ApplicationForm");
        frame.setContentPane(this.applicationForm);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //自定义退出操作
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Tool.showMessage("系统正在关闭中...", "系统正在关闭中..", 1);
                    //第一步：获取全部报警主机信息，判断是否已存在
                    String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":12346/alarm";
                    String instructionGet = "/ISAPI/Event/notification/httpHosts?format=json";
                    List<HttpHostNotificationEntity> httpHostNotificationEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, instructionGet, null).getString("HttpHostNotification"), HttpHostNotificationEntity.class);
                    for (HttpHostNotificationEntity httpHostNotificationEntity : httpHostNotificationEntityList) {
                        if (httpHostNotificationEntity.getUrl().equals(url)) {
                            Tool.deleteHttpHosts(httpHostNotificationEntity.getId());
                        }
                    }
                    System.exit(0);
                } catch (Exception e1) {
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }
        });
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getRootPane().setDefaultButton(monitorEnterButton);
        frame.setVisible(true);
        try {
            monitorText.requestFocus();
        } catch (Exception ignored) {
        }
        try {
            NetStateService netStateService = new NetStateService();
            if (netStateService.ping(Egci.configEntity.getSocketIp())) {
                //创建接收通行信息的socket对象
                MonitorReceiveInfoSocketService monitorReceiveInfoSocketService = new MonitorReceiveInfoSocketService();
                monitorReceiveInfoSocketService.sendInfo(Tool.getAccessPermissionInfo(Egci.accountEntity.getAccountPermission()));
                monitorReceiveInfoSocketService.start();
            } else {
                Egci.monitorWorkStatus = 0;
            }
        } catch (Exception e) {
            Egci.monitorWorkStatus = 0;
        }
    }
}
