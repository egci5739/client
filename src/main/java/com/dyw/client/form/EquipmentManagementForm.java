package com.dyw.client.form;

import com.alibaba.fastjson.JSON;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.EquipmentStatusEntity;
import com.dyw.client.service.SendInfoSocketService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private JButton cardAndFaceModeButton;//开启人脸识别
    private JButton faceModeButton;
    private JButton refreshEquipmentStatusButton;
    private JButton timeSynchronizationButton;

    private DefaultTableModel equipmentManagerModel;
    private List<EquipmentEntity> equipmentEntityList;
    private List<EquipmentStatusEntity> equipmentStatusEntityList;

    public EquipmentManagementForm() {
        //初始化设备管理表格
        String[] columnEquipmentInfo = {"设备名称", "设备IP", "切换器IP", "人员数量"};
        equipmentManagerModel = new DefaultTableModel();
        equipmentManagerModel.setColumnIdentifiers(columnEquipmentInfo);
        equipmentManagementContentTable.setModel(equipmentManagerModel);
        DefaultTableCellRenderer equipmentInfoTableCellRenderer = new DefaultTableCellRenderer();
        equipmentInfoTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        equipmentManagementContentTable.setDefaultRenderer(Object.class, equipmentInfoTableCellRenderer);
        //切换设备
        equipmentManagementContentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try {
                    if (equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow()) != null) {
                        EquipmentStatusEntity equipmentStatusEntity = equipmentStatusEntityList.get(equipmentManagementContentTable.getSelectedRow());
                        if (equipmentStatusEntity.getIsLogin().equals("0")) {
                            cardAndFaceModeButton.setEnabled(false);
                            importStaffButton.setEnabled(false);
                            faceModeButton.setEnabled(false);
                        } else {
                            cardAndFaceModeButton.setEnabled(true);
                            importStaffButton.setEnabled(true);
                            faceModeButton.setEnabled(true);
                            if (equipmentStatusEntity.getPassMode().equals("1")) {
                                faceModeButton.setEnabled(false);
                            } else {
                                cardAndFaceModeButton.setEnabled(true);
                            }
                        }
                    }
                } catch (Exception e1) {
                }
            }
        });

        //更改设备信息
        equipmentManagerModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                try {
                    EquipmentEntity equipmentEntity = equipmentEntityList.get(row);
                    if (col == 0) {
                        equipmentEntity.setEquipmentName((String) equipmentManagerModel.getValueAt(row, col));
                    } else if (col == 1) {
                        equipmentEntity.setEquipmentIp((String) equipmentManagerModel.getValueAt(row, col));
                    } else if (col == 2) {
                        equipmentEntity.setEquipmentSwitchIp((String) equipmentManagerModel.getValueAt(row, col));
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
        //加载设备信息
        refreshEquipmentStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEquipmentList();
            }
        });
        //设备时间同步
        timeSynchronizationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeSynchronization();
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
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
            sendInfoSocketService.sendInfo("5#" + equipmentEntity.getEquipmentSwitchIp() + "#0");
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
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("5#" + equipmentEntity.getEquipmentSwitchIp() + "#1");
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
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("4#" + equipmentEntity.getEquipmentIp() + "#0");
        sendInfoSocketService.receiveInfoOnce();
        faceModeButton.setEnabled(true);
        cardAndFaceModeButton.setEnabled(true);
        Tool.showMessage("切换成功", "提示", 1);
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
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("4#" + equipmentEntity.getEquipmentIp() + "#1");
        sendInfoSocketService.receiveInfoOnce();
        faceModeButton.setEnabled(false);
        cardAndFaceModeButton.setEnabled(true);
        Tool.showMessage("切换成功", "提示", 1);
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
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
            sendInfoSocketService.sendInfo("9#" + equipmentEntity.getEquipmentIp());
            sendInfoSocketService.receiveInfoOnce();
            Tool.showMessage("设备正在导入人员信息，请勿重复导入", "提示", 0);
        }
    }

    /*
     * 删除设备
     * */
    private void deleteEquipment() {
        if (equipmentManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一台设备", "提示", 0);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
            Egci.session.delete("mapping.equipmentMapper.deleteEquipment", equipmentEntityList.get(equipmentManagementContentTable.getSelectedRow()).getEquipmentId());
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
            equipmentEntity.setEquipmentName("");
            equipmentEntity.setEquipmentIp("");
            equipmentEntity.setEquipmentSwitchIp("");
            equipmentEntity.setEquipmentPermission(groupId);
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
        importStaffButton.setEnabled(false);
        faceModeButton.setEnabled(false);
        cardAndFaceModeButton.setEnabled(false);
        try {
            equipmentEntityList.clear();
            equipmentStatusEntityList.clear();
        } catch (NullPointerException ignored) {
        }
        equipmentManagerModel.setRowCount(0);
        equipmentEntityList = Egci.session.selectList("mapping.equipmentMapper.getAllEquipment");
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("3#0");
        equipmentStatusEntityList = JSON.parseArray(sendInfoSocketService.receiveInfoOnce(), EquipmentStatusEntity.class);
        int i = 0;
        for (EquipmentEntity equipmentEntity : equipmentEntityList) {
            Vector v = new Vector();
            v.add(0, equipmentEntity.getEquipmentName());
            v.add(1, equipmentEntity.getEquipmentIp());
            v.add(2, equipmentEntity.getEquipmentSwitchIp());
            v.add(3, equipmentStatusEntityList.get(i).getCardNumber());
            equipmentManagerModel.addRow(v);
            i++;
        }
    }

    /*
     * 设备时间同步
     * */
    private void timeSynchronization() {
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("0#0");
        Tool.showMessage("同步成功", "提示", 1);
    }
}
