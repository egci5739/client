//package com.dyw.client.form;
//
//import com.dyw.client.controller.Egci;
//import com.dyw.client.entity.AccountEntity;
//import com.dyw.client.entity.EquipmentEntity;
//import com.dyw.client.entity.FaceCollectionEntity;
//import com.dyw.client.entity.PassInfoEntity;
//import com.dyw.client.service.DateSelectorButtonService;
//import com.dyw.client.tool.Tool;
//
//import javax.swing.*;
//import javax.swing.event.TableModelEvent;
//import javax.swing.event.TableModelListener;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableModel;
//import javax.swing.table.TableRowSorter;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Timestamp;
//import java.text.DecimalFormat;
//import java.util.List;
//import java.util.Vector;
//
//public class SystemForm {
//    private DefaultTableModel equipmentManagerModel;
//    private List<EquipmentEntity> equipmentEntityList;
//    private DefaultTableModel dataAnalysisModel;
//    private DecimalFormat df = new DecimalFormat("#.00");
//    private RowSorter<TableModel> sorter;
//    private DefaultTableCellRenderer dataAnalysisTableCellRenderer;
//    private DefaultTableModel accountManagementModel;
//    private List<AccountEntity> accountEntityList;
//    private DefaultTableModel faceCollectionModel;
//    private List<FaceCollectionEntity> faceCollectionEntityList;
//
//    private JPanel system;
//    private JTabbedPane tabbedPane1;
//    private JPanel equipmentManagementPanel;
//    private JPanel equipmentManagementToolBarPanel;
//    private JPanel equipmentManagementContentPanel;
//    private JScrollPane equipmentManagementContentScroll;
//    private JTable equipmentManagementContentTable;
//    private JPanel accountManagementPanel;
//    private JPanel dataAnalysisPanel;
//    private JPanel configurationManagementPanel;
//    private JPanel dataAnalysisDateSelectionPanel;
//    private JPanel dataAnalysisContentPanel;
//    private JScrollPane dataAnalysisContentScroll;
//    private JTable dataAnalysisContentTable;
//    private JLabel startTimeSelectionLabel;
//    private DateSelectorButtonService startTimeSelectionButton;
//    private JLabel endTimeSelectionLabel;
//    private DateSelectorButtonService endTimeSelectionButton;
//    private JButton searchButton;
//    private JPanel accountManagementToolBarPanel;
//    private JPanel accountManagementContentPanel;
//    private JScrollPane accountManagementContentScroll;
//    private JTable accountManagementContentTable;
//    private JPanel faceCollectionManagementPanel;
//    private JPanel faceCollectionManagementToolBarPanel;
//    private JPanel faceCollectionManagementContentPanel;
//    private JScrollPane faceCollectionManagementContentScroll;
//    private JTable faceCollectionManagementContentTable;
//    private JButton equipmentManagementAddButton;
//    private JButton equipmentManagementDeleteButton;
//    private JButton faceCollectionManagementAddButton;
//    private JButton faceCollectionManagementDeleteButton;
////    private JButton accountManagementAddButton;
////    private JButton accountManagementDeleteButton;
//
//    /*
//     * 构造函数
//     * */
//    public SystemForm() {
//        /*
//         * 设备管理
//         * */
//        //初始化设备管理表格
//        String[] columnEquipmentInfo = {"设备名称", "设备IP", "切换器IP"};
//        equipmentManagerModel = new DefaultTableModel();
//        equipmentManagerModel.setColumnIdentifiers(columnEquipmentInfo);
//        equipmentManagementContentTable.setModel(equipmentManagerModel);
//        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
//        DefaultTableCellRenderer equipmentInfoTableCellRenderer = new DefaultTableCellRenderer();
//        equipmentInfoTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
//        equipmentManagementContentTable.setDefaultRenderer(Object.class, equipmentInfoTableCellRenderer);
//        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
//            Vector v = new Vector();
//            v.add(0, equipmentEntity.getName());
//            v.add(1, equipmentEntity.getIP());
//            v.add(2, equipmentEntity.getStatusSwitchSocketIP());
//            equipmentManagerModel.addRow(v);
//        }
//        //更改设备信息
//        equipmentManagerModel.addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                int row = e.getFirstRow();
//                int col = e.getColumn();
//                try {
//                    EquipmentEntity equipmentEntity = equipmentEntityList.get(row);
//                    if (col == 0) {
//                        equipmentEntity.setName((String) equipmentManagerModel.getValueAt(row, col));
//                    } else if (col == 1) {
//                        equipmentEntity.setIP((String) equipmentManagerModel.getValueAt(row, col));
//                    } else if (col == 2) {
//                        equipmentEntity.setStatusSwitchSocketIP((String) equipmentManagerModel.getValueAt(row, col));
//                    }
//                    Egci.session.update("mapping.equipmentMapper.updateEquipment", equipmentEntity);
//                    Egci.session.commit();
//                } catch (IndexOutOfBoundsException e1) {
//                    return;
//                }
//            }
//        });
//        //新增设备
//        equipmentManagementAddButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addEquipment();
//            }
//        });
//        //删除设备
//        equipmentManagementDeleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteEquipment();
//            }
//        });
//        //===============================================================================
//        /*
//         * 用户管理
//         * */
//        //初始化用户管理表格
//        String[] columnAccountInfo = {"用户名", "密码", "角色", "权限"};
//        accountManagementModel = new DefaultTableModel();
//        accountManagementModel.setColumnIdentifiers(columnAccountInfo);
//        accountManagementContentTable.setModel(accountManagementModel);
//        DefaultTableCellRenderer accountTableCellRenderer = new DefaultTableCellRenderer();
//        accountTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
//        accountManagementContentTable.setDefaultRenderer(Object.class, accountTableCellRenderer);
//        accountManagementModel.setRowCount(0);
//        accountEntityList = Egci.session.selectList("mapping.accountMapper.getAllAccount");
//        for (AccountEntity accountEntity : accountEntityList) {
//            Vector v = new Vector();
//            v.add(0, accountEntity.getAccountName());
//            v.add(1, accountEntity.getAccountPass());
//            v.add(2, accountEntity.getAccountRole());
//            v.add(3, accountEntity.getAccountPermission());
//            accountManagementModel.addRow(v);
//        }
//        //新增用户
//        //删除用户
//        /*
//         * 采集设备管理
//         * */
//        String[] columnFaceCollectionInfo = {"设备名称", "设备IP", "关联主机IP"};
//        faceCollectionModel = new DefaultTableModel();
//        faceCollectionModel.setColumnIdentifiers(columnFaceCollectionInfo);
//        faceCollectionManagementContentTable.setModel(faceCollectionModel);
//        refreshFaceEquipmentList();
//        //修改采集设备信息
//        faceCollectionModel.addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                int row = e.getFirstRow();
//                int col = e.getColumn();
//                try {
//                    FaceCollectionEntity faceCollectionEntity = faceCollectionEntityList.get(row);
//                    if (col == 0) {
//                        faceCollectionEntity.setFaceCollectionName((String) faceCollectionModel.getValueAt(row, col));
//                    } else if (col == 1) {
//                        faceCollectionEntity.setFaceCollectionIp((String) faceCollectionModel.getValueAt(row, col));
//                    } else if (col == 2) {
//                        faceCollectionEntity.setHostIp((String) faceCollectionModel.getValueAt(row, col));
//                    }
//                    Egci.session.update("mapping.faceCollectionMapper.updateFaceCollection", faceCollectionEntity);
//                    Egci.session.commit();
//                } catch (IndexOutOfBoundsException e1) {
//                    return;
//                }
//            }
//        });
//        //新增采集设备
//        faceCollectionManagementAddButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addFaceEquipment();
//            }
//        });
//        //删除采集设备
//        faceCollectionManagementDeleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteFaceEquipment();
//            }
//        });
//        //===============================================================================
//        /*
//         * 数据分析
//         * */
//        String[] columnDataAnalysisInfo = {"设备名称", "比对总数", "比对通过", "比对失败", "卡号不存在", "成功率", "失败率"};
//        dataAnalysisModel = new DefaultTableModel() {
//            @Override
//            public Class<?> getColumnClass(int column) {
//                Class returnValue;
//                if ((column >= 0) && (column < getColumnCount())) {
//                    returnValue = getValueAt(0, column).getClass();
//                } else {
//                    returnValue = Object.class;
//                }
//                return returnValue;
//            }
//
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        dataAnalysisModel.setColumnIdentifiers(columnDataAnalysisInfo);
//        dataAnalysisContentTable.setModel(dataAnalysisModel);
//        dataAnalysisTableCellRenderer = new DefaultTableCellRenderer();
//        dataAnalysisTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
//        dataAnalysisContentTable.setDefaultRenderer(Object.class, dataAnalysisTableCellRenderer);
//        sorter = new TableRowSorter<TableModel>(dataAnalysisModel);
//        dataAnalysisContentTable.setRowSorter(sorter);
//        //查询数据分析结果
//        searchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                search();
//            }
//        });
//    }
//
//    //====================================================================================
//    /*
//     * 删除设备
//     * */
//    private void deleteEquipment() {
//        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
//            Egci.session.delete("mapping.equipmentMapper.deleteEquipment", equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow()).getId());
//            Egci.session.commit();
//            refreshEquipmentList();
//        }
//    }
//
//    /*
//     * 新增设备
//     * */
//    private void addEquipment() {
//        String[] str = {"一核", "二核", "三核"};
//        int groupId = 0;
//        String result = (String) JOptionPane.showInputDialog(null, "请选择设备属于哪一核", null, 1, null, str, str[0]);
//        if (result != null) {
//            if ("一核".equals(result)) {
//                groupId = 2;
//            } else if ("二核".equals(result)) {
//                groupId = 3;
//            } else {
//                groupId = 4;
//            }
//            EquipmentEntity equipmentEntity = new EquipmentEntity();
//            equipmentEntity.setName("");
//            equipmentEntity.setIP("");
//            equipmentEntity.setStatusSwitchSocketIP("");
//            equipmentEntity.setGroupId(groupId);
//            Vector v = new Vector();
//            equipmentManagerModel.addRow(v);
//            Egci.session.insert("mapping.equipmentMapper.addEquipment", equipmentEntity);
//            Egci.session.commit();
//            refreshEquipmentList();
//        }
//    }
//
//    /*
//     * 重新加载设备列表
//     * */
//    public void refreshEquipmentList() {
//        equipmentManagerModel.setRowCount(0);
//        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
//        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
//            Vector v = new Vector();
//            v.add(0, equipmentEntity.getName());
//            v.add(1, equipmentEntity.getIP());
//            v.add(2, equipmentEntity.getStatusSwitchSocketIP());
//            equipmentManagerModel.addRow(v);
//        }
//    }
//
//    //====================================================================================
//    /*
//     * 查询数据分析结果
//     * */
//    private void search() {
//        dataAnalysisContentTable.setRowSorter(null);
//        dataAnalysisModel.setRowCount(0);
//        for (int i = 0; i < equipmentEntityList.size(); i++) {
//            Vector v = new Vector();
//            EquipmentEntity equipmentEntity = equipmentEntityList.get(i);
//            PassInfoEntity passInfoEntity = new PassInfoEntity();
//            passInfoEntity.setIP(equipmentEntity.getIP());
//            passInfoEntity.setStartDate(new Timestamp(startTimeSelectionButton.getDate().getTime()));
//            passInfoEntity.setEndDate(new Timestamp(endTimeSelectionButton.getDate().getTime()));
//            v.add(0, equipmentEntity.getName());
//            //获取总数
//            passInfoEntity.setEventTypeId(0);
//            int totalNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
//            v.add(1, totalNumber);
//            //获取通过数量
//            passInfoEntity.setEventTypeId(105);
//            int successNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
//            v.add(2, successNumber);
//            //获取失败数量
//            passInfoEntity.setEventTypeId(112);
//            int faultNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
//            v.add(3, faultNumber);
//            //获取卡号不存在数量
//            passInfoEntity.setEventTypeId(9);
//            int noCardNumber = Egci.session.selectOne("mapping.passInfoMapper.getPassNumberCount", passInfoEntity);
//            v.add(4, noCardNumber);
//            //成功率
//            v.add(5, df.format((float) successNumber / (successNumber + faultNumber) * 100) + "%");
//            //失败率
//            v.add(6, df.format((float) faultNumber / (successNumber + faultNumber) * 100) + "%");
//            dataAnalysisModel.addRow(v);
//        }
//        dataAnalysisContentTable.setRowSorter(sorter);
//    }
//
//    //======================================================================================================
//    /*
//     * 新增采集设备
//     * */
//    private void addFaceEquipment() {
//        FaceCollectionEntity faceCollectionEntity = new FaceCollectionEntity();
//        faceCollectionEntity.setFaceCollectionName("");
//        faceCollectionEntity.setFaceCollectionIp("");
//        faceCollectionEntity.setHostIp("");
//        Vector v = new Vector();
//        faceCollectionModel.addRow(v);
//        Egci.session.insert("mapping.faceCollectionMapper.addFaceCollection", faceCollectionEntity);
//        Egci.session.commit();
//        refreshFaceEquipmentList();
//    }
//
//    /*
//     * 刷新采集设备列表
//     * */
//    private void refreshFaceEquipmentList() {
//        faceCollectionModel.setRowCount(0);
//        faceCollectionEntityList = Egci.session.selectList("mapping.faceCollectionMapper.getAllFaceCollection");
//        for (FaceCollectionEntity faceCollectionEntity : faceCollectionEntityList) {
//            Vector v1 = new Vector();
//            v1.add(0, faceCollectionEntity.getFaceCollectionName());
//            v1.add(1, faceCollectionEntity.getFaceCollectionIp());
//            v1.add(2, faceCollectionEntity.getHostIp());
//            faceCollectionModel.addRow(v1);
//        }
//    }
//
//    /*
//     * 删除采集设备
//     * */
//    private void deleteFaceEquipment() {
//        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
//            Egci.session.delete("mapping.faceCollectionMapper.deleteFaceCollection", faceCollectionEntityList.get(faceCollectionManagementContentTable.getSelectedRow()).getId());
//            Egci.session.commit();
//            refreshFaceEquipmentList();
//        }
//    }
//
//    /*
//     * 初始化页面
//     * */
//    public void init() {
//        JFrame frame = new JFrame("SystemForm");
//        frame.setContentPane(this.system);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//        startTimeSelectionButton.setText(Tool.getCurrentDate() + " 00:00:00");
//        endTimeSelectionButton.setText(Tool.getCurrentDate() + " 23:59:59");
//    }
//
//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//        startTimeSelectionButton = new DateSelectorButtonService();
//        endTimeSelectionButton = new DateSelectorButtonService();
//    }
//}
