package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.NoteEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.functionForm.NoteFunction;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.NetStateService;
import com.dyw.client.service.PassPhotoTableCellRenderer;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MonitorRealTimeForm {
    private Logger logger = LoggerFactory.getLogger(MonitorRealTimeForm.class);

    public JPanel getMonitorRealTimePanel() {
        return monitorRealTimePanel;
    }

    private JPanel monitorRealTimePanel;
    private JPanel monitorRealTime;
    private JPanel passSuccessPanel;
    private JPanel passSuccessTitlePanel;
    private JLabel passSuccessTitleLabel;
    private JCheckBox passSuccessRollingCheckBox;
    private JButton paaSuccessClearButton;
    private JPanel passSuccessContentPanel;
    private JScrollPane passSuccessContentScroll;
    private JTable passSuccessContentTable;
    private JPanel passFaultPanel;
    private JPanel passFaultTitlePanel;
    private JLabel passFaultTitleLabel;
    private JCheckBox passFaultRollingCheckBox;
    private JButton paaFaultClearButton;
    private JPanel passFaultContentPanel;
    private JScrollPane passFaultContentScroll;
    private JTable passFaultContentTable;
    private JPanel passAlarmPanel;
    private JPanel passAlarmTitlePanel;
    private JLabel passAlarmTitleLabel;
    private JPanel passAlarmContentPanel;

    private DefaultTableModel passSuccessModel;
    private DefaultTableModel passFaultModel;
    private JScrollBar passSuccessScrollBar;//通行成功滚动条
    private int passSuccessRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private JScrollBar passFaultScrollBar;//通行成功滚动条
    private int passFaultRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private int passSuccessBottomStatus = 0;
    private int passFaultBottomStatus = 0;

    private JPopupMenu searchPopupMenu;
    private int menuStatus = 0;

    public MonitorRealTimeForm() {
        List<NoteEntity> noteEntityList = Egci.session.selectList("mapping.configMapper.getNote");
        //初始化通行结果表格
        String[] columnPassInfo = {"人员底图", "抓拍图片", "比对信息", "通行记录id"};
        passSuccessModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passSuccessModel.setColumnIdentifiers(columnPassInfo);
        passSuccessContentTable.setModel(passSuccessModel);
        passFaultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passFaultModel.setColumnIdentifiers(columnPassInfo);
        passFaultContentTable.setModel(passFaultModel);
        //表格中显示图片
        TableCellRenderer passTableCellRenderer = new PassPhotoTableCellRenderer();
        passSuccessContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
        passFaultContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
        passSuccessContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passSuccessBottomStatus <= 3) {
                    passSuccessContentScroll.getVerticalScrollBar().setValue(passSuccessContentScroll.getVerticalScrollBar().getModel().getMaximum() - passSuccessContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passSuccessBottomStatus++;
                }
            }
        });
        passSuccessContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passFaultContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passFaultBottomStatus <= 3) {
                    passFaultContentScroll.getVerticalScrollBar().setValue(passFaultContentScroll.getVerticalScrollBar().getModel().getMaximum() - passFaultContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passFaultBottomStatus++;
                }
            }
        });
        passFaultContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passSuccessScrollBar = passSuccessContentScroll.getVerticalScrollBar();
        passFaultScrollBar = passFaultContentScroll.getVerticalScrollBar();
        //通行成功是否滚动
        passSuccessRollingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    passSuccessRollingStatus = 1;
                } else {
                    passSuccessRollingStatus = 0;
                }
            }
        });
        //清空通行成功记录
        paaSuccessClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passSuccessModel.setRowCount(0);
            }
        });
        //通行失败是否滚动
        passFaultRollingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    passFaultRollingStatus = 1;
                } else {
                    passFaultRollingStatus = 0;
                }
            }
        });
        //清空通行失败记录
        paaFaultClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passFaultModel.setRowCount(0);
            }
        });
        /*
         * 弹出右键菜单
         * */
        //在table显示
        searchPopupMenu = new JPopupMenu();
        for (NoteEntity noteEntity : noteEntityList) {
            JMenuItem jMenuItem = new JMenuItem(noteEntity.getNoteName());
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addEvent(searchPopupMenu.getComponentIndex(jMenuItem), jMenuItem.getText(), (int) passFaultContentTable.getValueAt(menuStatus, 3));
                }
            });
            searchPopupMenu.add(jMenuItem);
        }
        passFaultContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = passFaultContentTable.rowAtPoint(e.getPoint());
                    searchPopupMenu.show(passFaultContentTable, e.getX(), e.getY());
                }
            }
        });
        //隐藏通行id项
        hideColumn(passSuccessContentTable, 3);
        hideColumn(passFaultContentTable, 3);
    }

    /*
     * 新增通行记录
     * */
    public void addPassInfo(PassRecordEntity passInfoEntity) {
        try {
            Vector v = new Vector();
            v.add(0, Base64.encodeBytes(passInfoEntity.getPassRecordStaffImage()));
            v.add(1, Base64.encodeBytes(passInfoEntity.getPassRecordCaptureImage()));
            if (passInfoEntity.getPassRecordPassResult() == 1) {
                v.add(2, Tool.displayPassSuccessResult(passInfoEntity));
                passSuccessModel.addRow(v);
                if (passSuccessRollingStatus == 1) {
                    moveScrollBarToBottom(passSuccessScrollBar);
                    passSuccessBottomStatus = 0;
                }
            } else {
                v.add(2, Tool.displayPassFaultResult(passInfoEntity));
                passFaultModel.addRow(v);
                if (passFaultRollingStatus == 1) {
                    moveScrollBarToBottom(passFaultScrollBar);
                    passFaultBottomStatus = 0;
                }
            }
            v.add(3, passInfoEntity.getPassRecordId());
        } catch (Exception e) {
            logger.error("新增通行记录出错", e);
        }
    }

    /*
     * 将滚动条移到底部
     * */
    private void moveScrollBarToBottom(JScrollBar jScrollBar) {
        if (jScrollBar != null) {
            jScrollBar.setValue(jScrollBar.getMaximum());
        }
    }

    /*
     * 更新失败失败
     * */
    private void addEvent(int noteId, String noteName, int passId) {
        PassRecordEntity passRecordEntity = new PassRecordEntity();
        passRecordEntity.setPassRecordId(passId);
        passRecordEntity.setPassRecordNoteId(noteId);
        if (noteId == 0) {
            NoteFunction noteFunction = new NoteFunction(this, passRecordEntity);
            noteFunction.init();
        } else {
            passRecordEntity.setPassRecordNote(noteName);
            Egci.session.update("mapping.passRecordMapper.updatePassRecordNote", passRecordEntity);
            Egci.session.commit();
            passFaultModel.removeRow(menuStatus);
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
     * 存储事件为 其他 的通行记录
     * */
    public void saveOtherEvent(PassRecordEntity passRecordEntity) {
        Egci.session.update("mapping.passRecordMapper.updatePassRecordNote", passRecordEntity);
        Egci.session.commit();
        passFaultModel.removeRow(menuStatus);
    }
}