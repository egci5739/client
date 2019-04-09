package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.EventEntity;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.service.HistoryPhotoTableCellRenderer;
import com.dyw.client.service.PageSelectionService;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MonitorHistoryForm {
    public JPanel getMonitorHistoryForm() {
        return monitorHistoryForm;
    }

    private JPanel monitorHistoryForm;
    private JPanel monitorHistory;
    private JPanel conditionSelectionPanel;
    private JComboBox equipmentSelectionCombo;
    private JComboBox eventSelectionCombo;
    private JTextField passCardSelectionText;
    private JTextField nameSelectionText;
    private JLabel startTimeSelectionLabel;
    private DateSelectorButtonService startTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private DateSelectorButtonService endTimeSelectionButton;
    private JButton searchButton;
    private JPanel resultContentPanel;
    private JScrollPane resultContentScroll;
    private JTable resultContentTable;
    private JPanel pageSelectionBottomPanel;
    private JPanel pageSelectionPanel;
    private JButton previousPageButton;
    private JButton nextPageButton;
    private JButton firstPageButton;
    private JPanel perPageNumberPanel;
    private JSpinner perPageNumberSpinner;
    private JLabel perPageNumberLabel;
    private JButton perPageNumberButton;
    private JPanel passTotalNumberPanel;
    private JLabel passTotalNumberLabel;

    private String passCardSelectionDefaultHint = "请输入卡号";
    private String nameSelectionDefaultHint = "请输入姓名";
    private Map<Integer, String> conditionEquipmentMap;
    private Map<Integer, Integer> conditionEventMap;
    private DefaultTableModel resultModel;
    private List<PassInfoEntity> passInfoHistoryList;
    private PageSelectionService pageSelectionService;

    public MonitorHistoryForm() {
        conditionEquipmentMap = new HashMap<Integer, String>();
        conditionEventMap = new HashMap<Integer, Integer>();
        pageSelectionService = new PageSelectionService();
        perPageNumberSpinner.setValue(5);//每页默认显示数量
        //初始化设备选择下拉框
        List<EquipmentEntity> equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipmentWithCondition", Tool.getGroupId(Egci.accountEntity.getAccountPermission()));
        EquipmentEntity equipmentEntity1 = new EquipmentEntity();
        equipmentEntity1.setName("--全部设备--");
        equipmentEntityList.add(0, equipmentEntity1);
        int i = 0;
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            equipmentSelectionCombo.addItem(equipmentEntity.getName());
            conditionEquipmentMap.put(i, equipmentEntity.getIP());
            i++;
        }
        //初始化事件选择下拉框
        List<EventEntity> eventEntityList = Egci.session.selectList("mapping.eventMapper.getEventOn");
        EventEntity eventEntity1 = new EventEntity();
        eventEntity1.setEventName("--全部事件--");
        eventEntityList.add(0, eventEntity1);
        int j = 0;
        for (EventEntity eventEntity : eventEntityList) {
            eventSelectionCombo.addItem(eventEntity.getEventName());
            conditionEventMap.put(j, eventEntity.getEventId());
            j++;
        }
        //输入卡号框选中/未选中时
        passCardSelectionText.setText(passCardSelectionDefaultHint);
        passCardSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passCardSelectionText.getText().equals(passCardSelectionDefaultHint)) {
                    passCardSelectionText.setText("");
                }
            }
        });
        passCardSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (passCardSelectionText.getText().equals("")) {
                    passCardSelectionText.setText(passCardSelectionDefaultHint);
                }
            }
        });
        //输入姓名框选中/未选中时
        nameSelectionText.setText(nameSelectionDefaultHint);
        nameSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameSelectionText.getText().equals(nameSelectionDefaultHint)) {
                    nameSelectionText.setText("");
                }
            }
        });
        nameSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (nameSelectionText.getText().equals("")) {
                    nameSelectionText.setText(nameSelectionDefaultHint);
                }
            }
        });
        //初始化历史查询结果表格
        String[] columnHistoryInfo = {"时间", "姓名", "卡号", "事件", "分值", "设备", "底图", "抓拍"};
        resultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultModel.setColumnIdentifiers(columnHistoryInfo);
        resultContentTable.setModel(resultModel);
        //表格中显示图片
        TableCellRenderer historyTableCellRenderer = new HistoryPhotoTableCellRenderer();
        resultContentTable.setDefaultRenderer(Object.class, historyTableCellRenderer);
        //点击查询历史记录
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        //点击首页
        firstPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySearchResult(pageSelectionService.firstPage(passInfoHistoryList));
            }
        });
        //点击上一页
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySearchResult(pageSelectionService.previousPage(passInfoHistoryList));
            }
        });
        //点击下一页
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySearchResult(pageSelectionService.nextPage(passInfoHistoryList));
            }
        });
        //更改每页显示的页数
        perPageNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageSelectionService.setPerPageNumber((Integer) perPageNumberSpinner.getValue());
                displaySearchResult(pageSelectionService.firstPage(passInfoHistoryList));
            }
        });
        resultContentTable.addContainerListener(new ContainerAdapter() {
        });
        resultContentTable.addComponentListener(new ComponentAdapter() {
        });

        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");
    }

    /*
     * 查询历史记录
     * */
    private void search() {
        PassInfoEntity condition = new PassInfoEntity();
        condition.setIP(conditionEquipmentMap.get(equipmentSelectionCombo.getSelectedIndex()));
        condition.setEventTypeId(conditionEventMap.get(eventSelectionCombo.getSelectedIndex()).intValue());
        if (!passCardSelectionText.getText().equals(passCardSelectionDefaultHint)) {
            condition.setCardNumber(passCardSelectionText.getText());
        }
        if (!nameSelectionText.getText().equals(nameSelectionDefaultHint)) {
            condition.setStaffName(nameSelectionText.getText());
        }
        condition.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
        condition.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
        resultModel.setRowCount(0);
        passInfoHistoryList = Egci.session.selectList("mapping.passInfoMapper.getHistoryPassInfo", condition);
        displaySearchResult(pageSelectionService.firstPage(passInfoHistoryList));
    }

    /*
     * 将查询结果显示在结果框中
     * */
    private void displaySearchResult(List<PassInfoEntity> passInfoEntityList) {
        resultModel.setRowCount(0);
        for (PassInfoEntity passInfoEntity : passInfoEntityList) {
            Vector v = new Vector();
            v.add(0, passInfoEntity.getDate());
            v.add(1, passInfoEntity.getStaffName());
            v.add(2, passInfoEntity.getCardNumber());
            v.add(3, Tool.eventIdToEventName(passInfoEntity.getEventTypeId()));
            v.add(4, passInfoEntity.getSimilarity());
            v.add(5, passInfoEntity.getEquipmentName());
            v.add(6, Base64.encodeBytes(passInfoEntity.getPhoto()));
            v.add(7, Base64.encodeBytes(passInfoEntity.getCapturePhoto()));
            resultModel.addRow(v);
        }
        passTotalNumberLabel.setText("共有： " + passInfoHistoryList.size() + " 条记录");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }
}
