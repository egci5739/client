package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.DataAnalysisEntity;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.ChartService;
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
    private JButton openChartButton;

    @Override
    public JPanel getPanel() {
        return dataAnalysisForm;
    }

    private final DefaultTableModel dataAnalysisModel;
    private final DefaultTableCellRenderer dataAnalysisTableCellRenderer;
    private final RowSorter<TableModel> sorter;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final List<EquipmentEntity> equipmentEntityList;
    private final List<DataAnalysisEntity> dataAnalysisEntityList = new ArrayList<>();

    public DataAnalysisForm() {
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipmentWithCondition", 0);
        String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "活体检测失败", "比对成功率(%)", "比对失败率(%)", "活体检测失败率(%)"};
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
//        dataAnalysisContentTable.setAutoCreateRowSorter(true);//attention
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
        //打开柱状图
        openChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChartService chartService = new ChartService(dataAnalysisEntityList, "视图", "事件类型", "数量", 1);
                JFrame frame = new JFrame("柱状图");
                frame.setLocationRelativeTo(null);
                // Setting the width and height of frame
                frame.setSize(700, 400);
                frame.add(chartService.getChartPanel());
                frame.setVisible(true);
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
        int allNotLiveNumber = 0;
        dataAnalysisEntityList.clear();
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            Vector v = new Vector();
            DataAnalysisEntity dataAnalysisEntity = new DataAnalysisEntity();
            PassRecordEntity passInfoEntity = new PassRecordEntity();
            passInfoEntity.setPassRecordEquipmentIp(equipmentEntity.getEquipmentIp());
            passInfoEntity.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
            passInfoEntity.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
//            String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "活体检测失败", "比对成功率(%)", "比对失败率(%)", "活体检测失败率(%)"};
            //获取总数
            v.add(0, equipmentEntity.getEquipmentName());
            passInfoEntity.setPassRecordPassResult(10);
            int totalNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(1, totalNumber);
            //获取通过数量
            passInfoEntity.setPassRecordPassResult(1);
            int successNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(2, successNumber);
            //获取失败数量
            passInfoEntity.setPassRecordPassResult(2);
            int faultNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(3, faultNumber);
            //获取卡号不存在数量
            passInfoEntity.setPassRecordPassResult(0);
            int noCardNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(4, noCardNumber);
            //获取活体检测失败数量
            passInfoEntity.setPassRecordPassResult(3);
            int notLiveNumber = Egci.session.selectOne("mapping.passRecordMapper.getPassNumberCount", passInfoEntity);
            v.add(5, notLiveNumber);
            dataAnalysisEntity.setEquipmentName(equipmentEntity.getEquipmentName());
            dataAnalysisEntity.setTotalNumber(totalNumber);
            dataAnalysisEntity.setSuccessNumber(successNumber);
            dataAnalysisEntity.setFaultNumber(faultNumber);
            dataAnalysisEntity.setNoCardNumber(noCardNumber);
            dataAnalysisEntity.setNotLiveNumber(notLiveNumber);
            dataAnalysisEntity.setSuccessRate(df.format((float) successNumber / (successNumber + faultNumber + notLiveNumber) * 100));
            v.add(6, getRate(totalNumber, successNumber, faultNumber, notLiveNumber, 1));
            dataAnalysisEntity.setFaultRate(df.format((float) faultNumber / (successNumber + faultNumber + notLiveNumber) * 100));
            v.add(7, getRate(totalNumber, successNumber, faultNumber, notLiveNumber, 2));
            dataAnalysisEntity.setNoCardRate(df.format((float) noCardNumber / (successNumber + faultNumber + notLiveNumber) * 100));
            dataAnalysisEntity.setNotLiveRate(df.format((float) notLiveNumber / (successNumber + faultNumber + notLiveNumber) * 100));
            v.add(8, getRate(totalNumber, successNumber, faultNumber, notLiveNumber, 3));
            dataAnalysisEntityList.add(dataAnalysisEntity);
            allSuccessNumber += successNumber;
            allFaultNumber += faultNumber;
            allTotalNumber += totalNumber;
            allNoCardNumber += noCardNumber;
            allNotLiveNumber += notLiveNumber;
            dataAnalysisModel.addRow(v);
        }
        Vector v = new Vector();
        //计算全部设备的数据
        DataAnalysisEntity dataAnalysisEntity = new DataAnalysisEntity();
        dataAnalysisEntity.setEquipmentName("全部设备");
        v.add(0, "全部设备");
        dataAnalysisEntity.setTotalNumber(allTotalNumber);
        v.add(1, allTotalNumber);
        dataAnalysisEntity.setSuccessNumber(allSuccessNumber);
        v.add(2, allSuccessNumber);
        dataAnalysisEntity.setFaultNumber(allFaultNumber);
        v.add(3, allFaultNumber);
        dataAnalysisEntity.setNoCardNumber(allNoCardNumber);
        v.add(4, allNoCardNumber);
        dataAnalysisEntity.setNotLiveNumber(allNotLiveNumber);
        v.add(5, allNotLiveNumber);
        dataAnalysisEntity.setSuccessRate(df.format((float) allSuccessNumber / (allSuccessNumber + allFaultNumber + allNotLiveNumber) * 100));
        v.add(6, getRate(allTotalNumber, allSuccessNumber, allFaultNumber, allNotLiveNumber, 1));
        dataAnalysisEntity.setFaultRate(df.format((float) allFaultNumber / (allSuccessNumber + allFaultNumber + allNotLiveNumber) * 100));
        v.add(7, getRate(allTotalNumber, allSuccessNumber, allFaultNumber, allNotLiveNumber, 2));
        dataAnalysisEntity.setNoCardRate(df.format((float) allNoCardNumber / (allSuccessNumber + allFaultNumber + allNotLiveNumber) * 100));
        dataAnalysisEntity.setFaultRate(df.format((float) allNotLiveNumber / (allSuccessNumber + allFaultNumber + allNotLiveNumber) * 100));
        v.add(8, getRate(allTotalNumber, allSuccessNumber, allFaultNumber, allNotLiveNumber, 3));
        dataAnalysisEntityList.add(dataAnalysisEntity);
        dataAnalysisContentTable.setRowSorter(sorter);
        dataAnalysisModel.addRow(v);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }

    private float getRate(int totalNumber, int successNumber, int faultNumber, int notLiveNumber, int type) {
        if (totalNumber == 0) {
            return 0;
        } else {
            try {
                if (type == 1) {//成功率
                    return Float.parseFloat(df.format((float) successNumber / (successNumber + faultNumber + notLiveNumber) * 100));
                } else if (type == 2) {//失败率
                    return Float.parseFloat(df.format((float) faultNumber / (successNumber + faultNumber + notLiveNumber) * 100));
                } else {//活体失败率
                    return Float.parseFloat(df.format((float) notLiveNumber / (successNumber + faultNumber + notLiveNumber) * 100));
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public void init() {
        frame = new JFrame("数据统计");
        frame.setContentPane(this.dataAnalysisForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
