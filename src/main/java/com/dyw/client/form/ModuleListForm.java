package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.HttpHostNotificationEntity;
import com.dyw.client.form.guard.AccessRecordForm;
import com.dyw.client.form.guard.AlarmForm;
import com.dyw.client.form.guard.EquipmentTreeForm;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.NetStateService;
import com.dyw.client.timer.MonitorStatusTimer;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleListForm implements ActionListener {
    private JFrame frame;
    private JPanel moduleListForm;
    private JPanel moduleListTitlePanel;
    private JPanel moduleListContentPanel;
    private JButton assistButton;
    private JButton monitorEnterButton;
    private JTextField monitorText;
    private String[] functions;
    private Map<String, BaseFormService> functionMap = new HashMap<>();


    /*
     * 功能与编号对照
     *
     * 0：initRegisterForm：办证
     * 1：initMonitorRealTimeForm：实时通行-带图片
     * 2：initMonitorHistoryForm：历史通行记录
     * 3：initIntelligentApplicationForm：布防监控
     * 4：initAccountManagementForm：系统用户管理
     * 5：initDataAnalysisForm：数据统计
     * 6：initEquipmentManagementForm：设备管理
     * 7：initPersonManagementForm：布控名单管理
     * 8：initResourceManagementForm：布控设备管理
     * 9：initAlarmHistoryForm：布控报警历史
     * 10：initExportStaffForm：导出人员信息
     * 11：initFaultSummationForm：失败次数统计
     * 12：initHistoryVideoForm：历史通行视频
     * 13：initEquipmentTreeForm：设备状态树
     * 14：initAccessRecordForm：实时通行-不带图片
     * 15：initAlarmForm：报警记录
     * */

    public ModuleListForm() {
    }

    /*
     * 创建子功能
     * */
    private void initSubfunction() {
        functions = Egci.accountEntity.getAccountFunction().split(",");
        for (String function : functions) {
            switch (Integer.parseInt(function)) {
                case 0:
                    RegisterForm registerForm = new RegisterForm();
                    registerForm.init();
                    monitorEnterButton = registerForm.getSearchButton();
                    monitorText = registerForm.getChineseNameText();
//                    frame.dispose();
                    break;
                case 1:
                    Egci.monitorRealTimeForm = new MonitorRealTimeForm();
                    MonitorStatusTimer monitorStatusTimer = new MonitorStatusTimer(Egci.monitorRealTimeForm);
                    monitorStatusTimer.open();
//        applicationTabbedPane.add("实时监控", Egci.monitorRealTimeForm.getMonitorRealTimePanel());
                    Egci.monitorRealTimeForm.init("实时通行-带图片", Egci.monitorRealTimeForm.getPanel());
                    functionMap.put("实时通行-带图片", Egci.monitorRealTimeForm);
                    JButton monitorRealTimeButton = new JButton("实时通行-带图片");
                    monitorRealTimeButton.addActionListener(this);
                    moduleListContentPanel.add(monitorRealTimeButton);
                    break;
                case 2:
                    Egci.monitorHistoryForm = new MonitorHistoryForm();
                    Egci.monitorHistoryForm.init("历史通行记录", Egci.monitorHistoryForm.getPanel());
                    functionMap.put("历史通行记录", Egci.monitorHistoryForm);
                    JButton monitorHistoryButton = new JButton("历史通行记录");
                    monitorHistoryButton.addActionListener(this);
                    moduleListContentPanel.add(monitorHistoryButton);
                    break;
                case 3:
                    if (Egci.faceServerStatus == 0) {
                        continue;
                    }
                    BaseFormService intelligentApplicationForm = new IntelligentApplicationForm();
                    intelligentApplicationForm.init("布防监控", intelligentApplicationForm.getPanel());
                    functionMap.put("布防监控", intelligentApplicationForm);
                    JButton intelligentApplicationButton = new JButton("布防监控");
                    intelligentApplicationButton.addActionListener(this);
                    moduleListContentPanel.add(intelligentApplicationButton);
                    break;
                case 4:
                    BaseFormService accountManagementForm = new AccountManagementForm();
                    accountManagementForm.init("系统用户管理", accountManagementForm.getPanel());
                    functionMap.put("系统用户管理", accountManagementForm);
                    JButton accountManagementButton = new JButton("系统用户管理");
                    accountManagementButton.addActionListener(this);
                    moduleListContentPanel.add(accountManagementButton);
                    break;
                case 5:
                    BaseFormService dataAnalysisForm = new DataAnalysisForm();
                    dataAnalysisForm.init("数据统计", dataAnalysisForm.getPanel());
                    functionMap.put("数据统计", dataAnalysisForm);
                    JButton dataAnalysisButton = new JButton("数据统计");
                    dataAnalysisButton.addActionListener(this);
                    moduleListContentPanel.add(dataAnalysisButton);
                    break;
                case 6:
                    BaseFormService equipmentManagementForm = new EquipmentManagementForm();
                    equipmentManagementForm.init("设备管理", equipmentManagementForm.getPanel());
                    functionMap.put("设备管理", equipmentManagementForm);
                    JButton equipmentManagementButton = new JButton("设备管理");
                    equipmentManagementButton.addActionListener(this);
                    moduleListContentPanel.add(equipmentManagementButton);
                    break;
                case 7:
                    if (Egci.faceServerStatus == 0) {
                        continue;
                    }
                    BaseFormService personManagementForm = new PersonManagementForm();
                    personManagementForm.init("布控名单管理", personManagementForm.getPanel());
                    functionMap.put("布控名单管理", personManagementForm);
                    JButton personManagementButton = new JButton("布控名单管理");
                    personManagementButton.addActionListener(this);
                    moduleListContentPanel.add(personManagementButton);
                    break;
                case 8:
                    if (Egci.faceServerStatus == 0) {
                        continue;
                    }
                    BaseFormService resourceManagementForm = new ResourceManagementForm();
                    resourceManagementForm.init("布控设备管理", resourceManagementForm.getPanel());
                    functionMap.put("布控设备管理", resourceManagementForm);
                    JButton resourceManagementButton = new JButton("布控设备管理");
                    resourceManagementButton.addActionListener(this);
                    moduleListContentPanel.add(resourceManagementButton);
                    break;
                case 9:
                    if (Egci.faceServerStatus == 0) {
                        continue;
                    }
                    BaseFormService alarmHistoryForm = new AlarmHistoryForm();
                    alarmHistoryForm.init("布控报警历史", alarmHistoryForm.getPanel());
                    functionMap.put("布控报警历史", alarmHistoryForm);
                    JButton alarmHistoryButton = new JButton("布控报警历史");
                    alarmHistoryButton.addActionListener(this);
                    moduleListContentPanel.add(alarmHistoryButton);
                    break;
                case 10:
                    BaseFormService exportStaffForm = new ExportStaffForm();
                    exportStaffForm.init("导出人员信息", exportStaffForm.getPanel());
//                    createButtonGroup("导出人员信息");
                    functionMap.put("导出人员信息", exportStaffForm);
                    JButton exportStaffButton = new JButton("导出人员信息");
                    exportStaffButton.addActionListener(this);
                    moduleListContentPanel.add(exportStaffButton);
                    break;
//                case 11:
//                    initFaultSummationForm();
//                moduleListContentPanel.add();
//                    break;
                case 12:
                    if (Egci.faceServerStatus == 0) {
                        continue;
                    }
                    BaseFormService historyVideoForm = new HistoryVideoForm();
                    historyVideoForm.init("历史通行视频", historyVideoForm.getPanel());
                    functionMap.put("历史通行视频", historyVideoForm);
                    JButton historyVideoButton = new JButton("历史通行视频");
                    historyVideoButton.addActionListener(this);
                    moduleListContentPanel.add(historyVideoButton);
                    break;
                case 13:
                    Egci.equipmentTreeForm = new EquipmentTreeForm();
//        applicationTabbedPane.add("设备状态树", treeForm.getEquipmentTreeForm());
                    Egci.equipmentTreeForm.init("设备状态树", Egci.equipmentTreeForm.getPanel());
                    functionMap.put("设备状态树", Egci.equipmentTreeForm);
                    JButton equipmentTreeButton = new JButton("设备状态树");
                    equipmentTreeButton.addActionListener(this);
                    moduleListContentPanel.add(equipmentTreeButton);
                    break;
                case 14:
                    Egci.accessRecordForm = new AccessRecordForm();
                    Egci.accessRecordForm.init("实时通行-不带图片", Egci.accessRecordForm.getPanel());
                    functionMap.put("实时通行-不带图片", Egci.accessRecordForm);
                    JButton accessRecordButton = new JButton("实时通行-不带图片");
                    accessRecordButton.addActionListener(this);
                    moduleListContentPanel.add(accessRecordButton);
                    break;
                case 15:
                    Egci.alarmForm = new AlarmForm();
                    Egci.alarmForm.init("报警记录", Egci.alarmForm.getPanel());
                    functionMap.put("报警记录", Egci.alarmForm);
                    JButton alarmButton = new JButton("报警记录");
                    alarmButton.addActionListener(this);
                    moduleListContentPanel.add(alarmButton);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(e.getActionCommand());
            functionMap.get(e.getActionCommand()).open();
        } catch (Exception ignored) {
        }
    }

    public void init() {
        frame = new JFrame("门禁人脸识别系统");
        frame.setContentPane(this.moduleListForm);
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
        try {
            monitorText.requestFocus();
        } catch (Exception ignored) {
        }
        frame.setAlwaysOnTop(!frame.isAlwaysOnTop());
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getRootPane().setDefaultButton(monitorEnterButton);

        //创建子功能
        initSubfunction();
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);//禁止改变大小

        try {
            NetStateService netStateService = new NetStateService();
            if (netStateService.ping(Egci.configEntity.getSocketIp())) {
                //创建接收通行信息的socket对象
                if (functions.length != 1 || !Arrays.asList(functions).contains("0")) {
                    MonitorReceiveInfoSocketService monitorReceiveInfoSocketService = new MonitorReceiveInfoSocketService();
                    monitorReceiveInfoSocketService.sendInfo(Tool.getAccessPermissionInfo(Egci.accountEntity.getAccountPermission()));
                    monitorReceiveInfoSocketService.start();
                } else {
                    frame.dispose();
                }
            } else {
                Egci.monitorWorkStatus = 0;
            }
        } catch (Exception e) {
            Egci.monitorWorkStatus = 0;
        }
        assistButton.setVisible(false);//隐藏用来辅助的按钮
        Egci.menuHeight = frame.getBounds().height;
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                frame.setLocation(frame.getX(), 0);
            }
        });
    }
}
