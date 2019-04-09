package com.dyw.client.form;

import com.dyw.client.entity.protection.AlarmHistoryEntity;
import com.dyw.client.service.AlarmHistoryTableCellRenderer;
import com.dyw.client.service.AlarmTableCellRenderer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

    private Logger logger = LoggerFactory.getLogger(AlarmHistoryForm.class);
    private DefaultTableModel alarmHistoryModel;
    private int pageNum = 0;
    private List<AlarmHistoryEntity> alarmHistoryEntityList = new ArrayList<>();

    public AlarmHistoryForm() {
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
         * 查询
         * */
        search();
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
            inboundData.put("maxResultNumber", 30);
            inboundData.put("maxResults", 30);
            inboundData.put("FDID", "-1");
            inboundData.put("cameraID", "-1");
            inboundData.put("maxSimilarity", 1.00);
            inboundData.put("minSimilarity", 0.00);
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
}