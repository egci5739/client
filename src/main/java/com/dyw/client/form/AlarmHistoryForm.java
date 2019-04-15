package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.AlarmHistoryEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.service.AlarmHistoryTableCellRenderer;
import com.dyw.client.service.AlarmTableCellRenderer;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AlarmHistoryForm {
    public JPanel getAlarmHistoryForm() {
        return alarmHistoryForm;
    }

    private JPanel alarmHistoryForm;
    private JPanel alarmHistoryToolBarPanel;
    private JPanel alarmHistoryContentPanel;
    private JPanel alarmHistoryPagePanel;
    private JScrollPane alarmHistoryContentScroll;
    private JTable alarmHistoryContentTable;
    private JButton firstPageButton;
    private JButton previousPageButton;
    private JButton nextPageButton;
    private JComboBox fdLibSelectComboBox;
    private JTextField minSimilarityText;
    private JTextField maxSimilarityText;
    private JTextField maxResultNumberText;
    private DateSelectorButtonService snapStartTimeButton;
    private DateSelectorButtonService snapEndTimeButton;
    private JLabel minSimilarityLabel;
    private JLabel maxSimilarityLabel;
    private JLabel maxResultNumberLabel;
    private JLabel snapStartTimeLabel;
    private JLabel snapEndTimeLabel;
    private JButton searchButton;

    private Logger logger = LoggerFactory.getLogger(AlarmHistoryForm.class);
    private DefaultTableModel alarmHistoryModel;
    private int pageNum = 0;
    private List<AlarmHistoryEntity> alarmHistoryEntityList = new ArrayList<>();
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表
    private Map<Integer, String> conditionFdLibMap = new HashMap<>();

    public AlarmHistoryForm() {
        //初始化人脸库选择框
        getFDLib();
        FDLibEntity fdLibEntity = new FDLibEntity();
        fdLibEntity.setName("--全部人员库--");
        fdLibEntity.setFDID("-1");
        fdLibEntityList.add(0, fdLibEntity);
        int i = 0;
        for (FDLibEntity fdLibEntity1 : fdLibEntityList) {
            fdLibSelectComboBox.addItem(fdLibEntity1.getName());
            conditionFdLibMap.put(i, fdLibEntity1.getFDID());
            i++;
        }
        String[] columnAlarmHistoryInfo = {"抓拍图", "底图", "报警时间", "报警地点", "姓名", "性别", "名单库", "相似度"};
        alarmHistoryModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alarmHistoryModel.setColumnIdentifiers(columnAlarmHistoryInfo);
        alarmHistoryContentTable.setModel(alarmHistoryModel);
        TableCellRenderer alarmHistoryTableCellRenderer = new AlarmHistoryTableCellRenderer();
        alarmHistoryContentTable.setDefaultRenderer(Object.class, alarmHistoryTableCellRenderer);
        /*
         * 首页
         * */
        firstPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = 0;
                search();
            }
        });
        /*
         * 上一页
         * */
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pageNum < 1) {
                    Tool.showMessage("已经是第一页", "提示", 0);
                    return;
                }
                pageNum = pageNum - 30;
                search();
            }
        });
        /*
         * 下一页
         * */
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageNum = pageNum + 30;
                search();
            }
        });
        //查询
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (minSimilarityText.getText().equals("") || maxSimilarityText.getText().equals("") || maxResultNumberText.getText().equals("")) {
                    Tool.showMessage("请完善查询条件", "提示", 0);
                    return;
                }
                pageNum = 0;
                search();
            }
        });
        snapStartTimeButton.setText(Tool.getCurrentDate() + " 00:00:00");
        snapEndTimeButton.setText(Tool.getCurrentDate() + " 23:59:59");
    }

    /*
     * 查询记录
     * */
    private void search() {
        alarmHistoryModel.setRowCount(0);
        String instruction = "/ISAPI/Intelligent/FDLib/FCSearch?format=json";
        JSONObject inboundData = new JSONObject();
        try {
            inboundData.put("searchResultPosition", pageNum);
            inboundData.put("maxResultNumber", Integer.parseInt(maxResultNumberText.getText()));
            inboundData.put("maxResults", 30);
            inboundData.put("FDID", conditionFdLibMap.get(fdLibSelectComboBox.getSelectedIndex()));
            inboundData.put("cameraID", "-1");
            inboundData.put("maxSimilarity", Float.parseFloat(maxSimilarityText.getText()));
            inboundData.put("minSimilarity", Float.parseFloat(minSimilarityText.getText()));
            inboundData.put("sortType", "time");
            inboundData.put("snapStartTime", Tool.changeTimeToISO8601(snapStartTimeButton.getText()));
            inboundData.put("snapEndTime", Tool.changeTimeToISO8601(snapEndTimeButton.getText()));
            JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
            alarmHistoryEntityList = com.alibaba.fastjson.JSONObject.parseArray(resultData.getString("MatchList"), AlarmHistoryEntity.class);
            for (AlarmHistoryEntity alarmHistoryEntity : alarmHistoryEntityList) {
                if (!alarmHistoryEntity.getHuman_data().get(0).getFace_data().get(0).getBl_picurl().equals("")) {
                    Vector vector = new Vector();
                    vector.add(0, Base64.encodeBytes(Tool.getURLStream(alarmHistoryEntity.getFace_picurl())));
                    vector.add(1, Base64.encodeBytes(Tool.getURLStream(alarmHistoryEntity.getHuman_data().get(0).getFace_data().get(0).getBl_picurl())));
                    vector.add(2, alarmHistoryEntity.getAlarm_date());
                    vector.add(3, alarmHistoryEntity.getDevice_name());
                    vector.add(4, alarmHistoryEntity.getHuman_data().get(0).getHuman_name());
                    vector.add(5, alarmHistoryEntity.getGender());
                    vector.add(6, alarmHistoryEntity.getHuman_data().get(0).getName());
                    vector.add(7, alarmHistoryEntity.getHuman_data().get(0).getFace_data().get(0).getFace_similarity());
                    alarmHistoryModel.addRow(vector);
                }
            }
        } catch (JSONException e) {
            logger.error("获取查询记录出错", e);
        }
    }

    /*
     * 获取人脸库列表
     * */
    public void getFDLib() {
        try {
            fdLibEntityList.clear();
            fdLibEntityList = com.alibaba.fastjson.JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
        } catch (JSONException e1) {
            logger.error("获取人脸库出错", e1);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        snapStartTimeButton = new DateSelectorButtonService();
        snapEndTimeButton = new DateSelectorButtonService();
    }
}