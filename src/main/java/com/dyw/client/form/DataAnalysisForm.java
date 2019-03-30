package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.service.DateSelectorButtonService;
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
import java.util.List;
import java.util.Vector;

public class DataAnalysisForm {
    public JPanel getDataAnalysisForm() {
        return dataAnalysisForm;
    }

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

    private DefaultTableModel dataAnalysisModel;
    private DefaultTableCellRenderer dataAnalysisTableCellRenderer;
    private RowSorter<TableModel> sorter;
    private DecimalFormat df = new DecimalFormat("#.00");
    private List<EquipmentEntity> equipmentEntityList;

    public DataAnalysisForm() {
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
        String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "成功率", "失败率"};
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
    }

    /*
     * 查询数据分析结果
     * */
    private void search() {
        dataAnalysisContentTable.setRowSorter(null);
        dataAnalysisModel.setRowCount(0);
        for (int i = 0; i < equipmentEntityList.size(); i++) {
            Vector v = new Vector();
            EquipmentEntity equipmentEntity = equipmentEntityList.get(i);
            PassInfoEntity passInfoEntity = new PassInfoEntity();
            passInfoEntity.setIP(equipmentEntity.getIP());
            passInfoEntity.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
            passInfoEntity.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
            v.add(0, equipmentEntity.getName());
            //获取总数
            passInfoEntity.setEventTypeId(0);
            int totalNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
            v.add(1, totalNumber);
            //获取通过数量
            passInfoEntity.setEventTypeId(105);
            int successNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
            v.add(2, successNumber);
            //获取失败数量
            passInfoEntity.setEventTypeId(112);
            int faultNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
            v.add(3, faultNumber);
            //获取卡号不存在数量
            passInfoEntity.setEventTypeId(9);
            int noCardNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
            v.add(4, noCardNumber);
            //成功率
            v.add(5, df.format((float) successNumber / (successNumber + faultNumber) * 100) + "%");
            //失败率
            v.add(6, df.format((float) faultNumber / (successNumber + faultNumber) * 100) + "%");
            dataAnalysisModel.addRow(v);
        }
        dataAnalysisContentTable.setRowSorter(sorter);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }
}
