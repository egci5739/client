package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.protection.AlarmHistoryEntity;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

public class AlarmHistoryFunction {
    private Logger logger = LoggerFactory.getLogger(AlarmHistoryFunction.class);
    private JFrame frame;
    private JPanel alarmHistoryFunction;
    private JPanel alarmHistoryTitlePanel;
    private JPanel alarmHistoryContentPanel;
    private JLabel startTimeSelectionLabel;
    private DateSelectorButtonService startTimeSelectionButton;
    private DateSelectorButtonService endTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private JButton searchButton;
    private JTable alarmHistoryContentTable;
    private JScrollPane alarmHistoryContentScroll;
    private RowSorter<TableModel> sorter;


    private DefaultTableModel resultModel;
    private List<AlarmEntity> alarmEntityList;

    public AlarmHistoryFunction() {
        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");

        //初始化历史查询结果表格
        String[] columnHistoryInfo = {"类型", "设备", "时间", "处理状态", "备注", "处理人"};
        resultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultModel.setColumnIdentifiers(columnHistoryInfo);
        alarmHistoryContentTable.setModel(resultModel);
        sorter = new TableRowSorter<TableModel>(resultModel);
        alarmHistoryContentTable.setRowSorter(sorter);
        /*
         * 查询
         * */
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
    }

    /*
     * 查询
     * */
    private void search() {
        AlarmEntity condition = new AlarmEntity();
        condition.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
        condition.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
        resultModel.setRowCount(0);
        alarmEntityList = Egci.session.selectList("mapping.alarmMapper.getAlarmHistory", condition);
        displaySearchResult(alarmEntityList);
    }

    /*
     * 将查询结果显示在结果框中
     * */
    private void displaySearchResult(List<AlarmEntity> alarmEntityList) {
        resultModel.setRowCount(0);
        for (AlarmEntity alarmEntity : alarmEntityList) {
            try {
                Vector v = new Vector();
                v.add(0, alarmEntity.getAlarmName());
                v.add(1, alarmEntity.getAlarmEquipmentName());
                v.add(2, alarmEntity.getCreateTime().toString().substring(0, 19));
                v.add(3, changeStatusToInfo(alarmEntity.getAlarmStatus()));
                v.add(4, alarmEntity.getAlarmNote());
                v.add(5, alarmEntity.getOperator());
                resultModel.addRow(v);
            } catch (Exception e) {
                logger.error("显示查询结果出错", e);
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }

    public void init() {
        frame = new JFrame("报警历史查询");
        frame.setContentPane(this.alarmHistoryFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*
     * 是否已处理
     * */
    private String changeStatusToInfo(int status) {
        if (status == 1) {
            return "已处理";
        } else {
            return "";
        }
    }
}
