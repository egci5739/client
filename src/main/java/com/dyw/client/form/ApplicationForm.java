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
        //登陆脸谱服务器
        HttpsClientUtil.httpsClientInit(Egci.configEntity.getFaceServerIp(), Egci.configEntity.getFaceServerPort(), "admin", "hik12345");
        //登录校验代码
        String strUrl = "/ISAPI/Security/userCheck";
        String strOut = "";
        strOut = HttpsClientUtil.httpsGet("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + strUrl);
        //解析返回的xml文件
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new ByteArrayInputStream(strOut.getBytes("UTF-8")));
            Element employees = document.getRootElement();
            for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                if (employee.getName() == "statusValue" && 0 == employee.getText().compareTo("200")) {
                    JOptionPane.showMessageDialog(null, "登陆成功", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            //登陆失败
            JOptionPane.showMessageDialog(null, "登陆失败", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "登陆失败", "Error", JOptionPane.ERROR_MESSAGE);
        }

        AccountManagementForm accountManagementForm = new AccountManagementForm();
        applicationTabbedPane.add("系统用户管理", accountManagementForm.getAccountManagementForm());

        DataAnalysisForm dataAnalysisForm = new DataAnalysisForm();
        applicationTabbedPane.add("数据分析", dataAnalysisForm.getDataAnalysisForm());

        EquipmentManagementForm equipmentManagementForm = new EquipmentManagementForm();
        applicationTabbedPane.add("设备管理", equipmentManagementForm.getEquipmentManagementForm());

        FaceCollectionManagementForm faceCollectionManagementForm = new FaceCollectionManagementForm();
        applicationTabbedPane.add("采集设备管理", faceCollectionManagementForm.getFaceCollectionManagementForm());

        IntelligentApplicationForm intelligentApplicationForm = new IntelligentApplicationForm();
        applicationTabbedPane.add("智能应用", intelligentApplicationForm.getIntelligentApplicationForm());

        MonitorHistoryForm monitorHistoryForm = new MonitorHistoryForm();
        applicationTabbedPane.add("历史记录查询", monitorHistoryForm.getMonitorHistoryForm());

        MonitorManagementForm monitorManagementForm = new MonitorManagementForm();
        applicationTabbedPane.add("布控管理", monitorManagementForm.getMonitorManagementForm());

        MonitorRealTimeForm monitorRealTimeForm = new MonitorRealTimeForm();
        applicationTabbedPane.add("实时监控", monitorRealTimeForm.getMonitorRealTimePanel());

        PersonManagementForm personManagementForm = new PersonManagementForm();
        applicationTabbedPane.add("布控名单管理", personManagementForm.getPersonManagementForm());

        RegisterForm registerForm = new RegisterForm();
        registerForm.init();
        applicationTabbedPane.add("办证端", registerForm.getRegisterForm());

        ResourceManagementForm resourceManagementForm = new ResourceManagementForm();
        applicationTabbedPane.add("布控设备管理", resourceManagementForm.getResourceManagementForm());

    }

    public void init() {
        frame = new JFrame("ApplicationForm");
        frame.setContentPane(this.applicationForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
