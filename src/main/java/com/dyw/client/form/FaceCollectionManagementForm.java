package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.FaceCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class FaceCollectionManagementForm {
    public JPanel getFaceCollectionManagementForm() {
        return faceCollectionManagementForm;
    }

    private JPanel faceCollectionManagementForm;
    private JPanel faceCollectionManagementPanel;
    private JPanel faceCollectionManagementToolBarPanel;
    private JButton faceCollectionManagementAddButton;
    private JButton faceCollectionManagementDeleteButton;
    private JPanel faceCollectionManagementContentPanel;
    private JScrollPane faceCollectionManagementContentScroll;
    private JTable faceCollectionManagementContentTable;

    private Logger logger = LoggerFactory.getLogger(FaceCollectionManagementForm.class);
    private DefaultTableModel faceCollectionModel;
    private List<FaceCollectionEntity> faceCollectionEntityList;

    public FaceCollectionManagementForm() {
        String[] columnFaceCollectionInfo = {"设备名称", "设备IP", "关联主机IP"};
        faceCollectionModel = new DefaultTableModel();
        faceCollectionModel.setColumnIdentifiers(columnFaceCollectionInfo);
        faceCollectionManagementContentTable.setModel(faceCollectionModel);
        refreshFaceEquipmentList();
        //修改采集设备信息
        faceCollectionModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                try {
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
                } catch (IndexOutOfBoundsException e1) {
                    logger.error("修改采集设备信息出错", e1);
                    return;
                }
            }
        });
        //新增采集设备
        faceCollectionManagementAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFaceEquipment();
            }
        });
        //删除采集设备
        faceCollectionManagementDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFaceEquipment();
            }
        });
    }

    /*
     * 新增采集设备
     * */
    private void addFaceEquipment() {
        FaceCollectionEntity faceCollectionEntity = new FaceCollectionEntity();
        faceCollectionEntity.setFaceCollectionName("");
        faceCollectionEntity.setFaceCollectionIp("");
        faceCollectionEntity.setHostIp("");
        Vector v = new Vector();
        faceCollectionModel.addRow(v);
        Egci.session.insert("mapping.faceCollectionMapper.addFaceCollection", faceCollectionEntity);
        Egci.session.commit();
        refreshFaceEquipmentList();
    }

    /*
     * 刷新采集设备列表
     * */
    private void refreshFaceEquipmentList() {
        faceCollectionModel.setRowCount(0);
        faceCollectionEntityList = Egci.session.selectList("mapping.faceCollectionMapper.getAllFaceCollection");
        for (FaceCollectionEntity faceCollectionEntity : faceCollectionEntityList) {
            Vector v1 = new Vector();
            v1.add(0, faceCollectionEntity.getFaceCollectionName());
            v1.add(1, faceCollectionEntity.getFaceCollectionIp());
            v1.add(2, faceCollectionEntity.getHostIp());
            faceCollectionModel.addRow(v1);
        }
    }

    /*
     * 删除采集设备
     * */
    private void deleteFaceEquipment() {
        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
            Egci.session.delete("mapping.faceCollectionMapper.deleteFaceCollection", faceCollectionEntityList.get(faceCollectionManagementContentTable.getSelectedRow()).getId());
            Egci.session.commit();
            refreshFaceEquipmentList();
        }
    }
}