package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.MonitorPointEntity;
import com.dyw.client.entity.protection.RelateInfoEntity;
import com.dyw.client.form.MonitorManagementForm;
//import com.dyw.client.form.ProtectionForm;
import com.dyw.client.tool.MultiComboBox;
import com.dyw.client.tool.NameCode;
import com.dyw.client.tool.Tool;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorFunction {

    private JPanel monitorFunction;
    private JTextField monitorNameText;
    private JButton monitorConfirmButton;
    private JButton monitorCancelButton;
    private JLabel monitorNameLabel;
    private JLabel monitorObjectLabel;
    private JLabel monitorRangeLabel;
    private JLabel monitorThresholdLabel;
    private JLabel monitorReasonLabel;
    private MultiComboBox monitorObjectCombo;
    private MultiComboBox monitorRangeCombo;
    private JTextField monitorThresholdText;
    private JTextField monitorReasonText;
    private JLabel monitorTypeLabel;
    private JComboBox monitorTypeComboBox;
    private List<String> objectIds;
    private List<String> rangeIds;
    private JFrame frame;

    private List<NameCode> fdLib;
    private List<NameCode> fdLibsl;
    private List<NameCode> monitorPoint;
    private List<NameCode> monitorPointsl;
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表

    public MonitorFunction(
            final RelateInfoEntity relateInfoEntity,
            final List<MonitorPointEntity> monitorPointEntityList,
            final MonitorManagementForm monitorManagementForm) {
        /*
         * 布控类型
         * 白名单==黑名单   陌生人==白名单
         * */
        monitorTypeComboBox.addItem("");
        /*
         * 获取人脸库
         * */
        getFDLib();
        objectIds = new ArrayList<>();
        rangeIds = new ArrayList<>();
        fdLib = new ArrayList<>();
        fdLibsl = new ArrayList<>();
        for (FDLibEntity fdLibEntity : fdLibEntityList) {
            NameCode nameCode = new NameCode(fdLibEntity.getFDID(), fdLibEntity.getName());
            fdLib.add(nameCode);
        }
        monitorObjectCombo.initParameter(fdLib, fdLibsl);
        monitorPoint = new ArrayList<>();
        monitorPointsl = new ArrayList<>();

        for (MonitorPointEntity monitorPointEntity : monitorPointEntityList) {
            NameCode nameCode = new NameCode(monitorPointEntity.getMonitorPointID(), monitorPointEntity.getMonitorPointName());
            monitorPoint.add(nameCode);
        }
        monitorRangeCombo.initParameter(monitorPoint, monitorPointsl);

        monitorObjectCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                objectIds.clear();
                List<NameCode> selectedValues = monitorObjectCombo.getSelectedValues();
                for (NameCode a : selectedValues) {
                    System.out.println(a.toStringAll());
                    objectIds.add(a.getCode());
                }
            }
        });
        monitorRangeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rangeIds.clear();
                List<NameCode> selectedValues = monitorRangeCombo.getSelectedValues();
                for (NameCode a : selectedValues) {
                    System.out.println(a.toStringAll());
                    rangeIds.add(a.getCode());
                }
            }
        });
        //确定
        monitorConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (relateInfoEntity == null) {//新增
                    System.out.println("执行新增");
                    String instructionAdd = "/ISAPI/Intelligent/FDLib/executeControl?format=json";
                    JSONObject inboundDataOutAdd = new JSONObject();
                    JSONArray jsonArrayAdd = new JSONArray();
                    Map<String, Object> mapAdd = new HashMap<>();
                    try {
                        mapAdd.put("startTime", "00:00:00");
                        mapAdd.put("endTime", "23:59:59");
                        mapAdd.put("threshold", Integer.parseInt(monitorThresholdText.getText()));
                        jsonArrayAdd.put(0, mapAdd);
                        inboundDataOutAdd.put("name", monitorNameText.getText());
                        inboundDataOutAdd.put("FDID", StringUtils.join(objectIds, ","));
                        inboundDataOutAdd.put("cameraID", StringUtils.join(rangeIds, ","));
                        inboundDataOutAdd.put("reason", monitorReasonText.getText());
                        inboundDataOutAdd.put("planInfo", jsonArrayAdd);
                        JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instructionAdd, inboundDataOutAdd);
                        if (resultData.getInt("statusCode") == 1) {
//                            Tool.showMessage("添加成功", "提示", 0);
                            monitorManagementForm.getMonitorList();
                            frame.dispose();
                        } else {
                            Tool.showMessage("添加失败，错误码：" + resultData.getString("errorMsg"), "提示", 0);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {//修改
                    System.out.println("执行修改");
                    String instructionEdit = "/ISAPI/Intelligent/FDLib/executeControl?format=json&relateID=" + relateInfoEntity.getRelateID();
                    JSONObject inboundDataOutEdit = new JSONObject();
                    JSONArray jsonArrayEdit = new JSONArray();
                    Map<String, Object> mapEdit = new HashMap<>();
                    try {
                        mapEdit.put("startTime", "00:00:00");
                        mapEdit.put("endTime", "23:59:59");
                        mapEdit.put("threshold", Integer.parseInt(monitorThresholdText.getText()));
                        jsonArrayEdit.put(0, mapEdit);
                        inboundDataOutEdit.put("name", monitorNameText.getText());
                        inboundDataOutEdit.put("FDID", StringUtils.join(objectIds, ","));
                        System.out.println("FDID:" + StringUtils.join(objectIds, ","));
                        inboundDataOutEdit.put("cameraID", StringUtils.join(rangeIds, ","));
                        System.out.println("cameraID:" + StringUtils.join(rangeIds, ","));
                        inboundDataOutEdit.put("reason", monitorReasonText.getText());
                        inboundDataOutEdit.put("planInfo", jsonArrayEdit);
                        JSONObject resultData = Tool.sendInstructionAndReceiveStatus(2, instructionEdit, inboundDataOutEdit);
                        if (resultData.getInt("statusCode") == 1) {
//                            Tool.showMessage("添加成功", "提示", 0);
                            monitorManagementForm.getMonitorList();
                            frame.dispose();
                        } else {
                            Tool.showMessage("添加失败，错误码：" + resultData.getString("errorMsg"), "提示", 0);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        //取消
        monitorCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        //判断是否是编辑，如果是就加载旧信息
        if (relateInfoEntity != null) {
            monitorNameText.setText(relateInfoEntity.getName());
            monitorThresholdText.setText(relateInfoEntity.getPlanInfo().get(0).getThreshold() + "");
            monitorReasonText.setText(relateInfoEntity.getReason());
            String[] FDID = relateInfoEntity.getFDID().split(",");
            List<String> result = new ArrayList<>();
            for (String string : FDID) {
                result.add(Egci.fdLibMaps.get(string));
            }
            monitorObjectCombo.setText(StringUtils.join(result, ","));
            monitorRangeCombo.setText(relateInfoEntity.getCameraName());
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        monitorObjectCombo = new MultiComboBox();
        monitorRangeCombo = new MultiComboBox();
    }

    /*
     * 获取人脸库列表
     * */
    public void getFDLib() {
        try {
            fdLibEntityList.clear();
            fdLibEntityList = com.alibaba.fastjson.JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public void init() {
        frame = new JFrame("MonitorFunction");
        frame.setContentPane(this.monitorFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
