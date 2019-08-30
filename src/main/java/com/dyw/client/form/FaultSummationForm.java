package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.FaultSummationEntity;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FaultSummationForm {
    private Logger logger = LoggerFactory.getLogger(FaultSummationForm.class);

    public JPanel getFaultSummation() {
        return faultSummation;
    }

    private JFrame frame;
    private JPanel faultSummation;
    private JScrollPane faultSummationScroll;
    private JTable faultSummationTable;
    private JButton clearFaultSummationButton;
    private JButton searchFaultSummationButton;

    private List<FaultSummationEntity> faultSummationEntityList;
    private DefaultTableModel faultSummationModel;
    private DefaultTableCellRenderer faultSummationTableCellRenderer;
    private RowSorter<TableModel> sorter;


    public FaultSummationForm() {
        faultSummationEntityList = Egci.session.selectList("mapping.passRecordMapper.getAllFaultSummation");
        String[] columnFaultSummation = {"姓名", "卡号", "失败次数", "最后一次失败时间"};
        faultSummationModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        faultSummationModel.setColumnIdentifiers(columnFaultSummation);
        faultSummationTable.setModel(faultSummationModel);
        faultSummationTableCellRenderer = new DefaultTableCellRenderer();
        faultSummationTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        faultSummationTable.setDefaultRenderer(Object.class, faultSummationTableCellRenderer);
        sorter = new TableRowSorter<>(faultSummationModel);
        faultSummationTable.setRowSorter(sorter);

        //查询失败统计信息
        searchFaultSummationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        //删除全部记录
        clearFaultSummationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFaultSummation();
            }
        });
    }

    /*
     * 清空全部失败统计信息
     * */
    private void clearFaultSummation() {
        if (Tool.showConfirm("确认清空？", "提示")) {
            Egci.session.delete("mapping.passRecordMapper.deleteAllFaultSummation");
            Egci.session.commit();
            search();
        }
    }

    /*
     * 查询失败统计信息
     * */
    private void search() {
        faultSummationTable.setRowSorter(null);
        faultSummationEntityList.clear();
        faultSummationModel.setRowCount(0);
        faultSummationEntityList = Egci.session.selectList("mapping.passRecordMapper.getAllFaultSummation");
        for (FaultSummationEntity faultSummationEntity : faultSummationEntityList) {
            Vector v = new Vector();
            v.add(0, faultSummationEntity.getName());
            v.add(1, faultSummationEntity.getCardNumber());
            v.add(2, faultSummationEntity.getFaultAccount());
            v.add(3, faultSummationEntity.getLastTime());
            faultSummationModel.addRow(v);
        }
        faultSummationTable.setRowSorter(sorter);
    }

    public void init() {
        frame = new JFrame("失败次数统计");
        frame.setContentPane(this.faultSummation);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
