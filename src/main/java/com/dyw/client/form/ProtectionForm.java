package com.dyw.client.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.controller.WebcamViewerExample;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.FaceInfoEntity;
import com.dyw.client.functionForm.FaceBaseFunction;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ProtectionForm {
    private DefaultTableModel resourceManagementContentTableModel;
    private DefaultTableModel personManagementContentResultTableModel;
    //    private List<FDLibEntity> libEntityList;//人脸库列表
    private List<String> FDLibNames;//人脸库名称列表
    private List<FaceInfoEntity> faceInfoEntityList;//人员列表
    private List<FDLibEntity> fdLibEntityList;//人脸库列表

    private String FDID;

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
    private JPanel personManagementContentToolBarPanel;
    private JPanel personManagementContentResultPanel;
    private JPanel personManagementContentSelectPagePanel;
    private JButton personManagementContentToolBarAddButton;
    private JButton personManagementContentToolBarEditButton;
    private JButton personManagementContentToolBarDeleteButton;
    private JTable personManagementContentResultTable;
    private JScrollPane personManagementContentResultScroll;

    /*
     * 构造函数
     * */
    public ProtectionForm() {
        fdLibEntityList = new ArrayList<>();
        faceInfoEntityList = new ArrayList<>();
        FDLibNames = new ArrayList<>();
//        libEntityList = new ArrayList<>();
        FDID = null;
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
        /*
         * 名单管理
         * */
        //初始化名单库人脸显示列表
        String[] columnPersonManagementContentResultInfo = {"姓名", "性别", "出生日期", "证件号码"};
        personManagementContentResultTableModel = new DefaultTableModel();
        personManagementContentResultTableModel.setColumnIdentifiers(columnPersonManagementContentResultInfo);
        personManagementContentResultTable.setModel(personManagementContentResultTableModel);
        //获取人脸库列表
        getFDLib();
        //添加人脸库
        personManagementBaseAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFDLib();
            }
        });
        //修改人脸库
        personManagementBaseEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFDLib();
            }
        });
        //选择某一个人脸库
        personManagementBaseList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try {
                    personManagementContentResultTableModel.setRowCount(0);
                    if (fdLibEntityList.get(personManagementBaseList.getSelectedIndex()) != null) {
                        FDLibEntity fdLibEntity = fdLibEntityList.get(personManagementBaseList.getSelectedIndex());
                        //获取人脸库中的人脸数据
                        String inboundData = "{\"searchResultPosition\":0,\"maxResults\":100,\"faceLibType\":\"blackFD\",\"FDID\":\"" + fdLibEntity.getFDID() + "\"}";
                        String instruction = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
                        faceInfoEntityList.clear();
                        faceInfoEntityList = JSON.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData, "MatchList"), FaceInfoEntity.class);
                        for (FaceInfoEntity faceInfoEntity : faceInfoEntityList) {
                            Vector v = new Vector();
                            v.add(0, faceInfoEntity.getName());
                            v.add(1, faceInfoEntity.getGender());
                            v.add(2, faceInfoEntity.getBornTime());
                            v.add(3, faceInfoEntity.getCertificateNumber());
                            personManagementContentResultTableModel.addRow(v);
                        }
                        FDID = fdLibEntity.getFDID();
                    }
                } catch (ArrayIndexOutOfBoundsException e1) {
                } catch (NullPointerException e2) {
                }
            }
        });
        //添加人员
        personManagementContentToolBarAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFace();
            }
        });
        //删除人脸库
        personManagementBaseDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFDLib();
            }
        });
    }

    //=====================================================================================
    /*
     *名单管理
     * */
    //获取人脸库列表
    public void getFDLib() {
        fdLibEntityList.clear();
        FDLibNames.clear();
        fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null, "FDLib"), FDLibEntity.class);
        for (FDLibEntity fdLibEntity : fdLibEntityList) {
            FDLibNames.add(fdLibEntity.getName());
        }
        personManagementBaseList.setListData(FDLibNames.toArray());
    }

    //创建人脸库
    public void addFDLib() {
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, null);
        faceBaseFunction.init();
    }

    //修改人脸库
    private void editFDLib() {
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, fdLibEntityList.get(personManagementBaseList.getSelectedIndex()));
        faceBaseFunction.init();
    }

    //添加人脸
    private void addFace() {
        String cardNumber = JOptionPane.showInputDialog(null, "请输入卡号", "添加新用户", 1);
        //查询人员信息
        if (cardNumber != null) {
            StaffEntity staffEntity = Egci.session.selectOne("mapping.staffMapper.getResultStaffWithCard", cardNumber);
            if (staffEntity == null) {
                Tool.showMessage("人员不存在", "提示", 0);
            } else {
                String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
                String inboundData = "{\"faceLibType\":\"blackFD\",\"FDID\":\"" + FDID + "\",\"name\":\"" + staffEntity.getName() + "\",\"gender\":\"" + staffEntity.getSex() + "\",\"bornTime\":\"" + staffEntity.getBirthday() + "\",\"certificateNumber\":\"" + staffEntity.getCardNumber() + "\",\"customInfo\":\"" + staffEntity.getCompany() + "\"}";
                int resultStatus = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
                if (resultStatus == 1) {
                    Tool.showMessage("添加成功", "提示", 0);
                } else {
                    Tool.showMessage("添加失败，错误码：" + resultStatus, "提示", 0);
                }
            }
        }
    }

    //删除人脸库
    private void deleteFDLib() {
        if (!Tool.showConfirm("确认删除", "删除提示")) {
            return;
        }
        String instruction = "/ISAPI/Intelligent/FDLib?format=json&FDID=" + fdLibEntityList.get(personManagementBaseList.getSelectedIndex()).getFDID() + "&faceLibType=blackFD";
        int resultStatus = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
        if (resultStatus == 1) {
            Tool.showMessage("删除成功", "提示", 0);
            getFDLib();
        } else {
            Tool.showMessage("删除失败", "提示", 0);
        }
    }

    public void init() {
        JFrame frame = new WebcamViewerExample();
        frame.setContentPane(this.protection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
