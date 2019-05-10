package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.service.SendInfoSocketService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class EquipmentManagementForm {
    public JPanel getEquipmentManagementForm() {
        return equipmentManagementForm;
    }

    private JPanel equipmentManagementForm;
    private JPanel equipmentManagementPanel;
    private JPanel equipmentManagementToolBarPanel;
    private JButton equipmentManagementAddButton;
    private JButton equipmentManagementDeleteButton;
    private JPanel equipmentManagementContentPanel;
    private JScrollPane equipmentManagementContentScroll;
    private JTable equipmentManagementContentTable;
    private JButton importStaffButton;
    private JButton closeFaceButton;
    private JButton cardAndFaceModeButton;
    private JButton faceModeButton;

    private DefaultTableModel equipmentManagerModel;
    private List<EquipmentEntity> equipmentEntityList;

    public EquipmentManagementForm() {
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
            equipmentManagerModel.addRow(v);
        }
        //更改设备信息
        equipmentManagerModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                try {
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
                } catch (IndexOutOfBoundsException e1) {
                    return;
                }
            }
        });
        //新增设备
        equipmentManagementAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        //删除设备
        equipmentManagementDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEquipment();
            }
        });
        //导入人员到一体机
        importStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importStaff();
            }
        });
        //关闭人脸通行
        closeFaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFace();
            }
        });
        //切换到卡+人脸模式
        cardAndFaceModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runFace();
                setCardAndFaceMode();
            }
        });
        //切换到刷脸模式
        faceModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runFace();
                setFaceMode();
            }
        });
    }

    /*
     * 关闭人脸通行
     * */
    private void closeFace() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        if (Tool.showConfirm("确定关闭该设备的人脸识别通行？", "提示")) {
            EquipmentEntity equipmentEntity = equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow());
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getServerIp(), Egci.configEntity.getServerMonitorPort());
            sendInfoSocketService.sendInfo("5#" + equipmentEntity.getStatusSwitchSocketIP() + "#0");
            sendInfoSocketService.receiveInfoOnce();
        }
    }

    /*
     * 启用人脸识别
     * */
    private void runFace() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        EquipmentEntity equipmentEntity = equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow());
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getServerIp(), Egci.configEntity.getServerMonitorPort());
        sendInfoSocketService.sendInfo("5#" + equipmentEntity.getStatusSwitchSocketIP() + "#1");
        sendInfoSocketService.receiveInfoOnce();
    }

    /*
     * 设置卡+人脸模式
     * */
    private void setCardAndFaceMode() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        EquipmentEntity equipmentEntity = equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow());
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getServerIp(), Egci.configEntity.getServerMonitorPort());
        sendInfoSocketService.sendInfo("4#" + equipmentEntity.getIP() + "#0");
        sendInfoSocketService.receiveInfoOnce();
    }

    /*
     * 设置人脸模式
     * */
    private void setFaceMode() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        EquipmentEntity equipmentEntity = equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow());
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getServerIp(), Egci.configEntity.getServerMonitorPort());
        sendInfoSocketService.sendInfo("4#" + equipmentEntity.getIP() + "#1");
        sendInfoSocketService.receiveInfoOnce();
    }

    /*
     * 导入人员到一体机
     * */
    private void importStaff() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        if (Tool.showConfirm("确定导入人员信息到一体机设备？", "提示")) {
            EquipmentEntity equipmentEntity = equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow());
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getServerIp(), Egci.configEntity.getServerMonitorPort());
            sendInfoSocketService.sendInfo("9#" + equipmentEntity.getIP());
            sendInfoSocketService.receiveInfoOnce();
            Tool.showMessage("设备正在导入人员信息，请勿重复导入", "提示", 0);
        }
    }

    /*
     * 删除设备
     * */
    private void deleteEquipment() {
        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
            Egci.session.delete("mapping.equipmentMapper.deleteEquipment", equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow()).getId());
            Egci.session.commit();
            refreshEquipmentList();
        }
    }

    /*
     * 新增设备
     * */
    private void addEquipment() {
        String[] str = {"一核", "二核", "三核"};
        int groupId = 0;
        String result = (String) JOptionPane.showInputDialog(null, "请选择设备属于哪一核", null, 1, null, str, str[0]);
        if (result != null) {
            if ("一核".equals(result)) {
                groupId = 2;
            } else if ("二核".equals(result)) {
                groupId = 3;
            } else {
                groupId = 4;
            }
            EquipmentEntity equipmentEntity = new EquipmentEntity();
            equipmentEntity.setName("");
            equipmentEntity.setIP("");
            equipmentEntity.setStatusSwitchSocketIP("");
            equipmentEntity.setGroupId(groupId);
            Vector v = new Vector();
            equipmentManagerModel.addRow(v);
            Egci.session.insert("mapping.equipmentMapper.addEquipment", equipmentEntity);
            Egci.session.commit();
            refreshEquipmentList();
        }
    }

    /*
     * 重新加载设备列表
     * */
    private void refreshEquipmentList() {
        equipmentManagerModel.setRowCount(0);
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            Vector v = new Vector();
            v.add(0, equipmentEntity.getName());
            v.add(1, equipmentEntity.getIP());
            v.add(2, equipmentEntity.getStatusSwitchSocketIP());
            equipmentManagerModel.addRow(v);
        }
    }
}
