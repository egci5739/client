package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.MonitorPointEntity;
import com.dyw.client.entity.protection.RelateInfoEntity;
import com.dyw.client.functionForm.MonitorFunction;
import com.dyw.client.tool.Tool;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MonitorManagementForm {
    public JPanel getMonitorManagementForm() {
        return monitorManagementForm;
    }

    private JPanel monitorManagementForm;
    private JPanel monitorManagementPanel;
    private JPanel monitorManagementTolBarPanel;
    private JButton monitorManagementAddButton;
    private JButton monitorManagementEditButton;
    private JButton monitorManagementDeleteButton;
    private JPanel monitorManagementContentPanel;
    private JScrollPane monitorManagementContentScroll;
    private JTable monitorManagementContentTable;

    private List<MonitorPointEntity> monitorPointEntityList = new ArrayList<>();//监控点列表
    private DefaultTableModel monitorManagementContentTableModel;
    private List<RelateInfoEntity> relateInfoEntityList = new ArrayList<>();//布控信息表
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表


    public MonitorManagementForm() {
        //初始化名单库人脸显示列表
        String[] columnMonitorManagementContentInfo = {"布控名称", "布控类型", "布控对象", "布控范围", "布控时段", "布控创建时间"};
        monitorManagementContentTableModel = new DefaultTableModel();
        monitorManagementContentTableModel.setColumnIdentifiers(columnMonitorManagementContentInfo);
        monitorManagementContentTable.setModel(monitorManagementContentTableModel);
        //获取布控列表
        getMonitorList();
        //添加布控
        monitorManagementAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMonitor();
            }
        });
        //删除布控
        monitorManagementDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMonitor();
            }
        });
        //编辑布控
        monitorManagementEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMonitor();
            }
        });
    }

    public void getMonitorList() {
        relateInfoEntityList.clear();
        monitorManagementContentTableModel.setRowCount(0);
        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json";
        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(1, instruction, null);
        try {
            relateInfoEntityList = JSONObject.parseArray(resultData.getString("relateInfo"), RelateInfoEntity.class);
            for (RelateInfoEntity relateInfoEntity : relateInfoEntityList) {
                Vector vector = new Vector();
                vector.add(0, relateInfoEntity.getName());
                vector.add(1, relateInfoEntity.getListType());
                vector.add(2, relateInfoEntity.getFDID());
                vector.add(3, relateInfoEntity.getCameraName());
                vector.add(4, relateInfoEntity.getPlanInfo().get(0).getStartTime() + "-" + relateInfoEntity.getPlanInfo().get(0).getEndTime());
                vector.add(5, relateInfoEntity.getCreateTime());
                monitorManagementContentTableModel.addRow(vector);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * 添加布控
     * */
    private void addMonitor() {
        MonitorFunction monitorFunction = new MonitorFunction(null, fdLibEntityList, monitorPointEntityList, this);
        monitorFunction.init();
    }

    /*
     * 编辑布控
     * */
    private void editMonitor() {
        if (monitorManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择布控信息", "提示", 0);
            return;
        }
        RelateInfoEntity relateInfoEntity = relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow());
        MonitorFunction monitorFunction = new MonitorFunction(relateInfoEntity, fdLibEntityList, monitorPointEntityList, this);
        monitorFunction.init();
    }

    /*
     * 删除布控
     * */
    private void deleteMonitor() {
        if (monitorManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择布控信息", "提示", 0);
            return;
        }
        if (!Tool.showConfirm("确认删除？", "提示")) {
            return;
        }
        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json&relateID=" + relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow()).getRelateID();
        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
        try {
            if (resultData.getInt("statusCode") == 1) {
                Tool.showMessage("删除成功", "提示", 0);
                getMonitorList();
            } else {
                Tool.showMessage("删除设备失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
