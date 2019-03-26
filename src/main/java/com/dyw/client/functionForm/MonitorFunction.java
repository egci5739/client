package com.dyw.client.functionForm;

import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.MonitorPointEntity;
import com.dyw.client.form.ProtectionForm;
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
    private JLabel monitorTypeLabel;
    private JLabel monitorObjectLabel;
    private JLabel monitorRangeLabel;
    private JLabel monitorThresholdLabel;
    private JLabel monitorReasonLabel;
    private JComboBox monitorTypeCombo;
    private MultiComboBox monitorObjectCombo;
    private MultiComboBox monitorRangeCombo;
    private JTextField monitorThresholdText;
    private JTextField monitorReasonText;
    private List<String> objectIds;
    private List<String> rangeIds;
    private JFrame frame;

    private List<NameCode> fdLib;
    private List<NameCode> fdLibsl;
    private List<NameCode> monitorPoint;
    private List<NameCode> monitorPointsl;

    public MonitorFunction(final List<FDLibEntity> fdLibEntityList, final List<MonitorPointEntity> monitorPointEntityList, final ProtectionForm protectionForm) {

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
            NameCode nameCode = new NameCode(monitorPointEntity.getMonitorPointID(), monitorPointEntity.getRegionName());
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
                String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json";
                JSONObject inboundDataOut = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                Map<String, Object> map = new HashMap<>();
                try {
                    map.put("startTime", "00:00:00");
                    map.put("endTime", "23:59:59");
                    map.put("threshold", Integer.parseInt(monitorThresholdText.getText()));
                    jsonArray.put(0, map);
                    inboundDataOut.put("name", monitorNameText.getText());
                    inboundDataOut.put("FDID", StringUtils.join(objectIds, ","));
                    inboundDataOut.put("cameraID", StringUtils.join(rangeIds, ","));
                    inboundDataOut.put("reason", monitorReasonText.getText());
                    inboundDataOut.put("planInfo", jsonArray);
                    JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataOut);
                    if (resultData.getInt("statusCode") == 1) {
                        Tool.showMessage("添加成功", "提示", 0);
                        protectionForm.getMonitorList();
                        frame.dispose();
                    } else {
                        Tool.showMessage("添加失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                System.out.println(monitorObjectCombo.getSelectedValues());
                System.out.println(monitorRangeCombo.getSelectedValues());
            }
        });
        //取消
        monitorCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        monitorObjectCombo = new MultiComboBox();
        monitorRangeCombo = new MultiComboBox();
    }

    public void init() {
        frame = new JFrame("MonitorFunction");
        frame.setContentPane(this.monitorFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
