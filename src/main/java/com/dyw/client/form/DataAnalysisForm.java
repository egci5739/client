package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.DataAnalysisEntity;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.service.ExportExcelService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DataAnalysisForm extends BaseFormService {
    public JPanel getDataAnalysisForm() {
        return dataAnalysisForm;
    }

    private JFrame frame;
    private JPanel dataAnalysisForm;
    private JPanel dataAnalysisPanel;
    private JPanel dataAnalysisDateSelectionPanel;
    private JLabel startTimeSelectionLabel;
    private DateSelectorButtonService startTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private DateSelectorButtonService endTimeSelectionButton;
    private JButton searchButton;
    private JPanel dataAnalysisContentPanel;
    private JScrollPane dataAnalysisContentScroll;
    private JTable dataAnalysisContentTable;
    private JButton exportButton;

    @Override
    public JPanel getPanel() {
        return dataAnalysisForm;
    }

    private DefaultTableModel dataAnalysisModel;
    private DefaultTableCellRenderer dataAnalysisTableCellRenderer;
    private RowSorter<TableModel> sorter;
    private DecimalFormat df = new DecimalFormat("0.00");
    private List<EquipmentEntity> equipmentEntityList;
    private List<DataAnalysisEntity> dataAnalysisEntityList = new ArrayList<>();

    public DataAnalysisForm() {
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipmentWithCondition", 0);
        String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "成功率(%)", "失败率(%)"};
        dataAnalysisModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataAnalysisModel.setColumnIdentifiers(columnDataAnalysisInfo);
        dataAnalysisContentTable.setModel(dataAnalysisModel);
        dataAnalysisTableCellRenderer = new DefaultTableCellRenderer();
        dataAnalysisTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataAnalysisContentTable.setDefaultRenderer(Object.class, dataAnalysisTableCellRenderer);
        sorter = new TableRowSorter<TableModel>(dataAnalysisModel);
        dataAnalysisContentTable.setRowSorter(sorter);
        //查询数据分析结果
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");
        //导出excel表格
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });
    }

    /*
     * 导出excel表格
     * */
    private void export() {
        if (dataAnalysisEntityList.size() < 1) {
            Tool.showMessage("请先查询数据", "提示", 1);
            return;
        }
        ExportExcelService exportExcelService = new ExportExcelService();
        exportExcelService.exportDataInfo(dataAnalysisEntityList);
    }

    /*
     * 查询数据分析结果
     * */
    private void search() {
        dataAnalysisContentTable.setRowSorter(null);
        dataAnalysisModel.setRowCount(0);
        int allSuccessNumber = 0;
        int allFaultNumber = 0;
        int allTotalNumber = 0;
        int allNoCardNumber = 0;
        dataAnalysisEntityList.clear();
        for (int i = 0; i < equipmentEntityList.size(); i++) {
            DataAnalysisEntity dataAnalysisEntity = new DataAnalysisEntity();
            Vector v = new Vector();
            EquipmentEntity equipmentEntity = equipmentEntityList.get(i);
            PassRecordEntity passInfoEntity = new PassRecordEntity();
            passInfoEntity.setPassRecordEquipmentIp(equipmentEntity.getEquipmentIp());
            passInfoEntity.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
            passInfoEntity.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
            v.add(0, equipmentEntity.getEquipmentName());
            //获取总数
            passInfoEntity.setPassRecordEventTypeId(0);
            int totalNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(1, totalNumber);
            //获取通过数量
            passInfoEntity.setPassRecordEventTypeId(105);
            int successNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(2, successNumber);
            //获取失败数量
            passInfoEntity.setPassRecordEventTypeId(112);
            int faultNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(3, faultNumber);
            //获取卡号不存在数量
            passInfoEntity.setPassRecordEventTypeId(9);
            int noCardNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(4, noCardNumber);
            //成功率
            v.add(5, df.format((float) successNumber / (successNumber + faultNumber) * 100));
            //失败率
            v.add(6, df.format((float) faultNumber / (successNumber + faultNumber) * 100));
            dataAnalysisModel.addRow(v);

            dataAnalysisEntity.setEquipmentName(equipmentEntity.getEquipmentName());
            dataAnalysisEntity.setTotalNumber(totalNumber);
            dataAnalysisEntity.setSuccessNumber(successNumber);
            dataAnalysisEntity.setFaultNumber(faultNumber);
            dataAnalysisEntity.setNoCardNumber(noCardNumber);
            dataAnalysisEntity.setSuccessRate(df.format((float) successNumber / (successNumber + faultNumber) * 100));
            dataAnalysisEntity.setFaultRate(df.format((float) faultNumber / (successNumber + faultNumber) * 100));
            dataAnalysisEntityList.add(dataAnalysisEntity);

            allSuccessNumber += successNumber;
            allFaultNumber += faultNumber;
            allTotalNumber += totalNumber;
            allNoCardNumber += noCardNumber;
        }
        //计算全部设备的数据
        Vector v = new Vector();
        DataAnalysisEntity dataAnalysisEntity = new DataAnalysisEntity();
        v.add(0, "全部设备");
        v.add(1, allTotalNumber);
        v.add(2, allSuccessNumber);
        v.add(3, allFaultNumber);
        v.add(4, allNoCardNumber);
        v.add(5, df.format((float) allSuccessNumber / (allSuccessNumber + allFaultNumber) * 100));
        v.add(6, df.format((float) allFaultNumber / (allSuccessNumber + allFaultNumber) * 100));
        dataAnalysisEntity.setEquipmentName("全部设备");
        dataAnalysisEntity.setTotalNumber(allTotalNumber);
        dataAnalysisEntity.setSuccessNumber(allSuccessNumber);
        dataAnalysisEntity.setFaultNumber(allFaultNumber);
        dataAnalysisEntity.setNoCardNumber(allNoCardNumber);
        dataAnalysisEntity.setSuccessRate(df.format((float) allSuccessNumber / (allSuccessNumber + allFaultNumber) * 100));
        dataAnalysisEntity.setFaultRate(df.format((float) allFaultNumber / (allSuccessNumber + allFaultNumber) * 100));
        dataAnalysisEntityList.add(dataAnalysisEntity);
        dataAnalysisModel.addRow(v);
        dataAnalysisContentTable.setRowSorter(sorter);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }

    public void init() {
        frame = new JFrame("数据统计");
        frame.setContentPane(this.dataAnalysisForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
