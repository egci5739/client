package com.dyw.client.form.guard;

import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.functionForm.PassInfoFunction;
import com.dyw.client.service.BaseFormService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.Vector;

public class AccessRecordForm extends BaseFormService {
    private JFrame frame;
    private JPanel accessRecordForm;
    private JPanel accessRecordTitle;
    private JPanel accessRecordContent;
    private JLabel accessRecordTitleLabel;
    private JCheckBox accessRecordTitleCheckBox;
    private JButton accessRecordTitleButton;
    private JScrollPane accessRecordContentScroll;
    private JTable accessRecordContentTable;

    private DefaultTableModel accessRecordModel;
    private JScrollBar accessRecordScrollBar;//通行滚动条

    private int accessRecordBottomStatus = 0;
    private int accessRecordRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private int menuStatus = 0;
    private JPopupMenu popupMenu;


    @Override
    public JPanel getPanel() {
        return accessRecordForm;
    }

    public AccessRecordForm() {
        //初始化通行结果表格
        String[] columnPassInfo = {"通行结果", "通行时间", "通行设备", "人员信息", "记录id"};
        accessRecordModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        accessRecordModel.setColumnIdentifiers(columnPassInfo);
        accessRecordContentTable.setModel(accessRecordModel);
        accessRecordContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && accessRecordBottomStatus <= 3) {
                    accessRecordContentScroll.getVerticalScrollBar().setValue(accessRecordContentScroll.getVerticalScrollBar().getModel().getMaximum() - accessRecordContentScroll.getVerticalScrollBar().getModel().getExtent());
                    accessRecordBottomStatus++;
                }
            }
        });
//        accessRecordContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        accessRecordScrollBar = accessRecordContentScroll.getVerticalScrollBar();
        //是否滚动
        accessRecordTitleCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    accessRecordRollingStatus = 1;
                } else {
                    accessRecordRollingStatus = 0;
                }
            }
        });
        //清空通行记录
        accessRecordTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accessRecordModel.setRowCount(0);
            }
        });
        //右键
        JMenuItem jMenuItem = new JMenuItem("查看详情");
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PassInfoFunction passInfoFunction = new PassInfoFunction();
                passInfoFunction.init((Integer) accessRecordContentTable.getValueAt(menuStatus, 4));
            }
        });
        popupMenu = new JPopupMenu();
        accessRecordContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.add(jMenuItem);
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = accessRecordContentTable.rowAtPoint(e.getPoint());
                    popupMenu.show(accessRecordContentTable, e.getX(), e.getY());
                }
            }
        });
        //隐藏id
        hideColumn(accessRecordContentTable, 4);
    }

    /*
     * 新增通行记录
     * */
    public void addPassInfo(PassRecordEntity passInfoEntity) {
        Vector v = new Vector();
        v.add(0, changeInfo(passInfoEntity.getPassRecordPassResult()));
        v.add(1, passInfoEntity.getPassRecordPassTime());
        v.add(2, passInfoEntity.getPassRecordEquipmentName());
        v.add(3, passInfoEntity.getPassRecordName() + "#" + passInfoEntity.getPassRecordCardNumber());
        v.add(4, passInfoEntity.getPassRecordId());
        if (accessRecordModel.getRowCount() > 1000) {
            accessRecordModel.removeRow(0);
        }
        accessRecordModel.addRow(v);
        if (accessRecordRollingStatus == 1) {
            moveScrollBarToBottom(accessRecordScrollBar);
            accessRecordBottomStatus = 0;
        }
    }

    public void init() {
        frame = new JFrame("通行记录");
        frame.setContentPane(this.accessRecordForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private String changeInfo(int result) {
        String info = "";
        switch (result) {
            case 0:
                info = "无此卡号";
                return info;
            case 1:
                info = "比对通过";
                return info;
            case 2:
                info = "比对失败";
                return info;
            default:
                info = "比对失败";
                return info;
        }
    }

    /*
     * 隐藏某一页
     * */
    private void hideColumn(JTable table, int index) {
        TableColumn tc = table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setMinWidth(0);
        tc.setWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }

    /*
     * 将滚动条移到底部
     * */
    private void moveScrollBarToBottom(JScrollBar jScrollBar) {
        if (jScrollBar != null) {
            jScrollBar.setValue(jScrollBar.getMaximum());
        }
    }
}
