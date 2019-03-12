package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.service.DateSelectorButtonService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class SystemForm {
    private DefaultTableModel equipmentManagerModel;
    private List<EquipmentEntity> equipmentEntityList;
    private DefaultTableModel dataAnalysisModel;
    private DecimalFormat df = new DecimalFormat("#.00");

    private JPanel system;
    private JTabbedPane tabbedPane1;
    private JPanel equipmentManagementPanel;
    private JPanel equipmentManagementToolBarPanel;
    private JPanel equipmentManagementContentPanel;
    private JScrollPane equipmentManagementContentScroll;
    private JTable equipmentManagementContentTable;
    private JPanel accountManagementPanel;
    private JPanel dataAnalysisPanel;
    private JPanel ConfigurationManagementPanel;
    private JPanel dataAnalysisDateSelectionPanel;
    private JPanel dataAnalysisContentPanel;
    private JScrollPane dataAnalysisContentScroll;
    private JTable dataAnalysisContentTable;
    private JLabel startTimeSelectionLabel;
    private DateSelectorButtonService startTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private DateSelectorButtonService endTimeSelectionButton;
    private JButton searchButton;

    /*
     * 构造函数
     * */
    public SystemForm() {
        /*
         * 设备管理
         * */
        //初始化设备管理表格
        String[] columnEquipmentInfo = {"设备名称", "设备IP", "切换器IP"};
        equipmentManagerModel = new DefaultTableModel();
        equipmentManagerModel.setColumnIdentifiers(columnEquipmentInfo);
        equipmentManagementContentTable.setModel(equipmentManagerModel);
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            Vector v = new Vector();
            v.add(0, equipmentEntity.getName());
            v.add(1, equipmentEntity.getIP());
            v.add(2, equipmentEntity.getStatusSwitchSocketIP());
//            v.add(3, equipmentEntity.getGroupId());
//            v.add(4, null);
            equipmentManagerModel.addRow(v);
        }
//        更改设备信息
        equipmentManagerModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                EquipmentEntity equipmentEntity = equipmentEntityList.get(row);
                if (col == 0) {
                    equipmentEntity.setName((String) equipmentManagerModel.getValueAt(row, col));
                } else if (col == 1) {
                    equipmentEntity.setIP((String) equipmentManagerModel.getValueAt(row, col));
                } else if (col == 2) {
                    equipmentEntity.setStatusSwitchSocketIP((String) equipmentManagerModel.getValueAt(row, col));
                }
                Egci.session.update("mapping.equipmentMapper.updateEquipment", equipmentEntity);
                Egci.session.commit();
            }
        });
        /*
         * 数据分析
         * */
        String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "成功率", "失败率"};
        dataAnalysisModel = new DefaultTableModel();
        dataAnalysisModel.setColumnIdentifiers(columnDataAnalysisInfo);
        dataAnalysisContentTable.setModel(dataAnalysisModel);
        //查询数据分析结果
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
    }

    /*
     * 查询数据分析结果
     * */
    private void search() {
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
//            获取通过数量
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
            dataAnalysisModel.addRow(v);
            //成功率
            v.add(5, df.format((float) successNumber / (successNumber + faultNumber) * 100) + "%");
            //失败率
            v.add(6, df.format((float) faultNumber / (successNumber + faultNumber) * 100) + "%");
        }
    }

    /*
     * 初始化页面
     * */
    public void init() {
        JFrame frame = new JFrame("SystemForm");
        frame.setContentPane(this.system);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }
}
