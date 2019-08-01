package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.EventEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.service.ExportExcelService;
import com.dyw.client.service.HistoryPhotoTableCellRenderer;
import com.dyw.client.service.PageSelectionService;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.*;

public class MonitorHistoryForm {
    private Logger logger = LoggerFactory.getLogger(MonitorHistoryForm.class);

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
    private JButton exportPassInfoButton;
    private JLabel pageInfoLabel;//页数信息
    private int totalPage;//总页数
    private int currentPage = 1;//当前页
    private RowSorter<TableModel> sorter;

    private String passCardSelectionDefaultHint = "请输入卡号";
    private String nameSelectionDefaultHint = "请输入姓名";
    private Map<Integer, String> conditionEquipmentMap;
    private Map<Integer, Integer> conditionEventMap;
    private DefaultTableModel resultModel;
    private List<PassRecordEntity> passInfoHistoryList;
    private PageSelectionService pageSelectionService;

    public MonitorHistoryForm() {
        conditionEquipmentMap = new HashMap<>();
        conditionEventMap = new HashMap<>();
        pageSelectionService = new PageSelectionService();
        perPageNumberSpinner.setValue(5);//每页默认显示数量
        //初始化设备选择下拉框
        List<EquipmentEntity> equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipmentWithCondition", Egci.accountEntity.getAccountPermission());
        EquipmentEntity equipmentEntity1 = new EquipmentEntity();
        equipmentEntity1.setEquipmentName("--全部设备--");
        equipmentEntityList.add(0, equipmentEntity1);
        int i = 0;
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            equipmentSelectionCombo.addItem(equipmentEntity.getEquipmentName());
            conditionEquipmentMap.put(i, equipmentEntity.getEquipmentIp());
            i++;
        }
        //初始化事件选择下拉框
        eventSelectionCombo.addItem("--全部事件--");
        conditionEventMap.put(0, 0);
        eventSelectionCombo.addItem("人证比对通过");
        conditionEventMap.put(1, 105);
        eventSelectionCombo.addItem("人证比对失败");
        conditionEventMap.put(2, 112);
        eventSelectionCombo.addItem("无此卡号");
        conditionEventMap.put(3, 9);
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
        String[] columnHistoryInfo = {"时间", "姓名", "卡号", "事件", "说明", "分值", "设备", "底图", "抓拍"};
        resultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultModel.setColumnIdentifiers(columnHistoryInfo);
        resultContentTable.setModel(resultModel);
        sorter = new TableRowSorter<TableModel>(resultModel);
        resultContentTable.setRowSorter(sorter);
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
                showPageInfo(1);
            }
        });
        //点击上一页
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySearchResult(pageSelectionService.previousPage(passInfoHistoryList));
                showPageInfo(3);
            }
        });
        //点击下一页
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySearchResult(pageSelectionService.nextPage(passInfoHistoryList));
                showPageInfo(2);
            }
        });
        //更改每页显示的页数
        perPageNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageSelectionService.setPerPageNumber((Integer) perPageNumberSpinner.getValue());
                displaySearchResult(pageSelectionService.firstPage(passInfoHistoryList));
                showPageInfo(1);
            }
        });
        resultContentTable.addContainerListener(new ContainerAdapter() {
        });
        resultContentTable.addComponentListener(new ComponentAdapter() {
        });

        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");
        //导出通行记录
        exportPassInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPassInfo();
            }
        });
    }

    /*
     * 查询历史记录
     * */
    private void search() {
//        if (equipmentSelectionCombo.getSelectedIndex() == 0 && passCardSelectionText.getText().equals("请输入卡号") && nameSelectionText.getText().equals("请输入姓名")) {
//            Tool.showMessage("请先选择一台设备或输入卡号、姓名后查询", "提示", 0);
//            return;
//        }
        PassRecordEntity condition = new PassRecordEntity();
        condition.setPassRecordEquipmentIp(conditionEquipmentMap.get(equipmentSelectionCombo.getSelectedIndex()));
        condition.setPassRecordEventTypeId(conditionEventMap.get(eventSelectionCombo.getSelectedIndex()).intValue());
        if (!passCardSelectionText.getText().equals(passCardSelectionDefaultHint)) {
            condition.setPassRecordCardNumber(passCardSelectionText.getText());
        }
        if (!nameSelectionText.getText().equals(nameSelectionDefaultHint)) {
            condition.setPassRecordName(nameSelectionText.getText());
        }
        condition.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
        condition.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
        resultModel.setRowCount(0);
        logger.info("开始查询时间：" + new Date());
        passInfoHistoryList = Egci.session.selectList("mapping.passRecordMapper.getHistoryPassInfo", condition);
        logger.info("结束查询时间：" + new Date());
        displaySearchResult(pageSelectionService.firstPage(passInfoHistoryList));
        logger.info("显示结束时间" + new Date());
        showPageInfo(1);
    }

    /*
     * 将查询结果显示在结果框中
     * */
    private void displaySearchResult(List<PassRecordEntity> passInfoEntityList) {
        resultModel.setRowCount(0);
        for (PassRecordEntity passInfoEntity : passInfoEntityList) {
            try {
                Vector v = new Vector();
                v.add(0, passInfoEntity.getPassRecordPassTime());
                v.add(1, passInfoEntity.getPassRecordName());
                v.add(2, passInfoEntity.getPassRecordCardNumber());
                v.add(3, Tool.eventIdToEventName(passInfoEntity.getPassRecordEventTypeId()));
                v.add(4, passInfoEntity.getPassRecordNote());
                v.add(5, passInfoEntity.getPassRecordSimilarity());
                v.add(6, passInfoEntity.getPassRecordEquipmentName());
                v.add(7, Base64.encodeBytes(passInfoEntity.getPassRecordStaffImage()));
                v.add(8, Base64.encodeBytes(passInfoEntity.getPassRecordCaptureImage()));
                resultModel.addRow(v);
            } catch (Exception e) {
                logger.error("显示查询结果出错", e);
            }
        }
        passTotalNumberLabel.setText("共有： " + passInfoHistoryList.size() + " 条记录");
    }

    /*
     * 导出历史通行记录
     * */

    private void exportPassInfo() {
        try {
            if (passInfoHistoryList.size() < 1) {
                Tool.showMessage("没有数据", "提示", 0);
                return;
            }
            ExportExcelService exportExcelService = new ExportExcelService();
            exportExcelService.exportPassInfo(passInfoHistoryList);
        } catch (Exception e) {
            Tool.showMessage("没有数据", "提示", 0);
            logger.error("导出历史通行记录出错", e);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }

    /*
     * 计算并显示页数信息
     * type:1-全新查询,首页；2-下一页；3-上一页
     * */
    private void showPageInfo(int type) {
        totalPage = (int) Math.ceil(Float.parseFloat(String.valueOf(passInfoHistoryList.size())) / Float.parseFloat(String.valueOf(perPageNumberSpinner.getValue())));
        switch (type) {
            case 1:
                currentPage = 1;
                break;
            case 2:
                if (currentPage < totalPage) {
                    currentPage += 1;
                }
                break;
            case 3:
                if (currentPage > 1) {
                    currentPage -= 1;
                }
                break;
            default:
                break;
        }
        if (passInfoHistoryList.size() > 0) {
            pageInfoLabel.setText("第 " + currentPage + " 页 共 " + totalPage + " 页");
        } else {
            pageInfoLabel.setText("");
        }
    }
}
