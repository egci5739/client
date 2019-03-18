package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.FaceCollectionEntity;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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

public class SystemForm {
    private DefaultTableModel equipmentManagerModel;
    private List<EquipmentEntity> equipmentEntityList;
    private DefaultTableModel dataAnalysisModel;
    private DecimalFormat df = new DecimalFormat("#.00");
    private RowSorter<TableModel> sorter;
    private DefaultTableCellRenderer dataAnalysisTableCellRenderer;
    private DefaultTableModel accountManagementModel;
    private List<AccountEntity> accountEntityList;
    private DefaultTableModel faceCollectionModel;
    private List<FaceCollectionEntity> faceCollectionEntityList;

    private JPanel system;
    private JTabbedPane tabbedPane1;
    private JPanel equipmentManagementPanel;
    private JPanel equipmentManagementToolBarPanel;
    private JPanel equipmentManagementContentPanel;
    private JScrollPane equipmentManagementContentScroll;
    private JTable equipmentManagementContentTable;
    private JPanel accountManagementPanel;
    private JPanel dataAnalysisPanel;
    private JPanel configurationManagementPanel;
    private JPanel dataAnalysisDateSelectionPanel;
    private JPanel dataAnalysisContentPanel;
    private JScrollPane dataAnalysisContentScroll;
    private JTable dataAnalysisContentTable;
    private JLabel startTimeSelectionLabel;
    private DateSelectorButtonService startTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private DateSelectorButtonService endTimeSelectionButton;
    private JButton searchButton;
    private JPanel accountManagementToolBarPanel;
    private JPanel accountManagementContentPanel;
    private JScrollPane accountManagementContentScroll;
    private JTable accountManagementContentTable;
    private JPanel faceCollectionManagementPanel;
    private JPanel faceCollectionManagementToolBarPanel;
    private JPanel faceCollectionManagementContentPanel;
    private JScrollPane faceCollectionManagementContentScroll;
    private JTable faceCollectionManagementContentTable;

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
        DefaultTableCellRenderer equipmentInfoTableCellRenderer = new DefaultTableCellRenderer();
        equipmentInfoTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        equipmentManagementContentTable.setDefaultRenderer(Object.class, equipmentInfoTableCellRenderer);
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
         * 用户管理
         * */
        //初始化用户管理表格
        String[] columnAccountInfo = {"用户名", "密码", "角色", "权限"};
        accountManagementModel = new DefaultTableModel();
        accountManagementModel.setColumnIdentifiers(columnAccountInfo);
        accountManagementContentTable.setModel(accountManagementModel);
        DefaultTableCellRenderer accountTableCellRenderer = new DefaultTableCellRenderer();
        accountTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        accountManagementContentTable.setDefaultRenderer(Object.class, accountTableCellRenderer);
        accountEntityList = Egci.session.selectList("mapping.accountMapper.getAllAccount");
        for (AccountEntity accountEntity : accountEntityList) {
            Vector v = new Vector();
            v.add(0, accountEntity.getAccountName());
            v.add(1, accountEntity.getAccountPass());
            v.add(2, Tool.accountRoleIdToName(accountEntity.getAccountRole()));
            v.add(3, Tool.accountPermissionIdToName(accountEntity.getAccountPermission()));
            accountManagementModel.addRow(v);
        }
        /*
         * 采集设备管理
         * */
        String[] columnFaceCollectionInfo = {"设备名称", "设备IP", "关联主机IP"};
        faceCollectionModel = new DefaultTableModel();
        faceCollectionModel.setColumnIdentifiers(columnFaceCollectionInfo);
        faceCollectionManagementContentTable.setModel(faceCollectionModel);
        faceCollectionEntityList = Egci.session.selectList("mapping.faceCollectionMapper.getAllFaceCollection");
        for (FaceCollectionEntity faceCollectionEntity : faceCollectionEntityList) {
            Vector v = new Vector();
            v.add(0, faceCollectionEntity.getFaceCollectionName());
            v.add(1, faceCollectionEntity.getFaceCollectionIp());
            v.add(2, faceCollectionEntity.getHostIp());
            faceCollectionModel.addRow(v);
        }
        //修改采集设备信息
        faceCollectionModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                FaceCollectionEntity faceCollectionEntity = faceCollectionEntityList.get(row);
                if (col == 0) {
                    faceCollectionEntity.setFaceCollectionName((String) faceCollectionModel.getValueAt(row, col));
                } else if (col == 1) {
                    faceCollectionEntity.setFaceCollectionIp((String) faceCollectionModel.getValueAt(row, col));
                } else if (col == 2) {
                    faceCollectionEntity.setHostIp((String) faceCollectionModel.getValueAt(row, col));
                }
                Egci.session.update("mapping.faceCollectionMapper.updateFaceCollection", faceCollectionEntity);
                Egci.session.commit();
            }
        });
        /*
         * 数据分析
         * */
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

    /*
     * 初始化页面
     * */
    public void init() {
        JFrame frame = new JFrame("SystemForm");
        frame.setContentPane(this.system);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeSelectionButton = new DateSelectorButtonService();
        endTimeSelectionButton = new DateSelectorButtonService();
    }
}
