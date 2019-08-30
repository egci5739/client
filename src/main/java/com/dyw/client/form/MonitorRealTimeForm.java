package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.NoteEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.functionForm.AlarmHistoryFunction;
import com.dyw.client.functionForm.NoteFunction;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.PassAlarmContentTableCellRenderer;
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
import java.util.List;
import java.util.Vector;

public class MonitorRealTimeForm extends BaseFormService {
    private Logger logger = LoggerFactory.getLogger(MonitorRealTimeForm.class);

    @Override
    public JPanel getPanel() {
        return monitorRealTimePanel;
    }

    private JFrame frame;
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
    private JScrollPane passAlarmContentScroll;
    private JTable passAlarmContentTable;
    private JButton alarmHistoryButton;

    private DefaultTableModel passSuccessModel;
    private DefaultTableModel passFaultModel;
    private JScrollBar passSuccessScrollBar;//通行成功滚动条
    private int passSuccessRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private JScrollBar passFaultScrollBar;//通行成功滚动条
    private int passFaultRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private int passSuccessBottomStatus = 0;
    private int passFaultBottomStatus = 0;

    private DefaultTableModel passAlarmModel;

    private JPopupMenu searchPopupMenu;//通行失败
    private JPopupMenu alarmPopupMenu;//胁迫报警
    private int menuStatus = 0;

    public MonitorRealTimeForm() {
        List<NoteEntity> noteEntityList = Egci.session.selectList("mapping.noteMapper.getManualNote");

        String[] columnPassInfo = {"人员底图", "抓拍图片", "比对信息", "通行记录id"};
        TableCellRenderer passTableCellRenderer = new PassPhotoTableCellRenderer();
        /*
         * 比对成功
         * */
        passSuccessModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passSuccessModel.setColumnIdentifiers(columnPassInfo);
        passSuccessContentTable.setModel(passSuccessModel);
        passSuccessContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
//        passSuccessContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passSuccessContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passSuccessBottomStatus <= 3) {
                    passSuccessContentScroll.getVerticalScrollBar().setValue(passSuccessContentScroll.getVerticalScrollBar().getModel().getMaximum() - passSuccessContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passSuccessBottomStatus++;
                }
            }
        });
        passSuccessScrollBar = passSuccessContentScroll.getVerticalScrollBar();
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
        /*
         * 比对失败
         * */
        passFaultModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passFaultModel.setColumnIdentifiers(columnPassInfo);
        passFaultContentTable.setModel(passFaultModel);
        passFaultContentTable.setDefaultRenderer(Object.class, passTableCellRenderer);
//        passFaultContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        passFaultContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && passFaultBottomStatus <= 3) {
                    passFaultContentScroll.getVerticalScrollBar().setValue(passFaultContentScroll.getVerticalScrollBar().getModel().getMaximum() - passFaultContentScroll.getVerticalScrollBar().getModel().getExtent());
                    passFaultBottomStatus++;
                }
            }
        });
        passFaultScrollBar = passFaultContentScroll.getVerticalScrollBar();
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
        //通行失败右键确认
        passFaultContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = passFaultContentTable.rowAtPoint(e.getPoint());
                    searchPopupMenu.show(passFaultContentTable, e.getX(), e.getY());
                }
            }
        });
        /*
         * 通行报警
         * */
        passAlarmModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
//        String[] columnAlarmInfo = {"时间", "设备", "记录id"};
        String[] columnAlarmInfo = {"类型", "设备", "时间", "记录id", "事件类型"};
        TableCellRenderer alarmCellRenderer = new PassAlarmContentTableCellRenderer();
        passAlarmContentTable.setDefaultRenderer(Object.class, alarmCellRenderer);
        passAlarmModel.setColumnIdentifiers(columnAlarmInfo);
        passAlarmContentTable.setModel(passAlarmModel);
        passAlarmContentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = passAlarmContentTable.rowAtPoint(e.getPoint());
                    alarmPopupMenu.show(passAlarmContentTable, e.getX(), e.getY());
                }
            }
        });
        /*
         * 弹出右键菜单
         * 报警事件
         * */
        alarmPopupMenu = new JPopupMenu();
        for (NoteEntity noteEntity1 : noteEntityList) {
            if (noteEntity1.getNoteRank() == 1 && noteEntity1.getNoteType() != 2) {
                JMenu multilevelMenu = new JMenu(noteEntity1.getNoteName());
                for (NoteEntity noteEntity2 : noteEntityList) {
                    if (noteEntity2.getRelativeId() == noteEntity1.getNoteId()) {
                        JMenuItem jMenuItem = new JMenuItem(noteEntity2.getNoteName());
                        jMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                updateAlarmEvent(jMenuItem.getText(), (int) passAlarmContentTable.getValueAt(menuStatus, 3));
//                                viewStressEvent((int) passAlarmContentTable.getValueAt(menuStatus, 3));
                            }
                        });
                        multilevelMenu.add(jMenuItem);
                    }
                }
                alarmPopupMenu.add(multilevelMenu);
            }
        }

        /*
         * 弹出右键菜单
         * 通行失败
         * */
        searchPopupMenu = new JPopupMenu();
        for (NoteEntity noteEntity : noteEntityList) {
            if (noteEntity.getNoteType() == 2) {
                JMenuItem jMenuItem = new JMenuItem(noteEntity.getNoteName());
                jMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addEvent(searchPopupMenu.getComponentIndex(jMenuItem), jMenuItem.getText(), (int) passFaultContentTable.getValueAt(menuStatus, 3));
                    }
                });
                searchPopupMenu.add(jMenuItem);
            }
        }

        //隐藏通行id项
        hideColumn(passSuccessContentTable, 3);
        hideColumn(passFaultContentTable, 3);
        hideColumn(passAlarmContentTable, 3);
        hideColumn(passAlarmContentTable, 4);
        /*
         * 报警历史查询
         * */
        alarmHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AlarmHistoryFunction alarmHistoryFunction = new AlarmHistoryFunction();
                alarmHistoryFunction.init();
            }
        });
    }

    /*
     * 更新报警事件
     * */
    private void updateAlarmEvent(String noteName, int passId) {
        if (noteName.equals("最近通行信息")) {
            viewStressEvent(passId);
        } else if (noteName.equals("自定义")) {
            AlarmEntity alarmEntity = new AlarmEntity();
            alarmEntity.setAlarmId(passId);
            NoteFunction noteFunction = new NoteFunction(this, alarmEntity);
            noteFunction.init();
        } else {
            AlarmEntity alarmEntity = new AlarmEntity();
            alarmEntity.setAlarmNote(noteName);
            alarmEntity.setAlarmId(passId);
            alarmEntity.setOperator(Egci.accountEntity.getAccountName());
            Egci.session.update("mapping.alarmMapper.updateAlarmNote", alarmEntity);
            Egci.session.commit();
            passAlarmModel.removeRow(menuStatus);
        }
    }

    /*
     * 胁迫时间点通行信息查询
     * */
    private void viewStressEvent(int valueAt) {
        AlarmEntity alarmEntity = Egci.session.selectOne("mapping.alarmMapper.getSingleAlarmInfo", valueAt);
        Egci.monitorHistoryForm.stressAlarmSearchHistory(alarmEntity);
    }

    /*
     * 新增通行记录
     * */
    public void addPassInfo(PassRecordEntity passInfoEntity) {
        try {
            Vector v = new Vector();
            if (passInfoEntity.getPassRecordPassResult() == 1) {
                v.add(0, Base64.encodeBytes(passInfoEntity.getPassRecordStaffImage()));
                v.add(1, Base64.encodeBytes(passInfoEntity.getPassRecordCaptureImage()));
                v.add(2, Tool.displayPassSuccessResult(passInfoEntity));
                v.add(3, passInfoEntity.getPassRecordId());
                if (passSuccessModel.getRowCount() > Egci.configEntity.getDisplayMonitorRowCount()) {
                    passSuccessModel.removeRow(0);
                }
                passSuccessModel.addRow(v);
                if (passSuccessRollingStatus == 1) {
                    moveScrollBarToBottom(passSuccessScrollBar);
                    passSuccessBottomStatus = 0;
                }
                passSuccessContentTable.repaint();//attention
            } else if (passInfoEntity.getPassRecordPassResult() == 0 || passInfoEntity.getPassRecordPassResult() == 2 || passInfoEntity.getPassRecordPassResult() == 3) {
                v.add(0, Base64.encodeBytes(passInfoEntity.getPassRecordStaffImage()));
                v.add(1, Base64.encodeBytes(passInfoEntity.getPassRecordCaptureImage()));
                v.add(2, Tool.displayPassFaultResult(passInfoEntity));
                v.add(3, passInfoEntity.getPassRecordId());
                if (passFaultModel.getRowCount() > Egci.configEntity.getDisplayMonitorRowCount()) {
                    passFaultModel.removeRow(0);
                }
                passFaultModel.addRow(v);
                if (passFaultRollingStatus == 1) {
                    moveScrollBarToBottom(passFaultScrollBar);
                    passFaultBottomStatus = 0;
                }
                passFaultContentTable.repaint();//attention
            }
        } catch (Exception e) {
            logger.error("新增通行记录出错", e);
        }
    }

    /*
     * 新增报警信息
     * */
    @Override
    public void addAlarmInfo(AlarmEntity alarmEntity) {
        try {
            Vector v = new Vector();
            v.add(0, alarmEntity.getAlarmName());
            v.add(1, alarmEntity.getAlarmEquipmentName());
            v.add(2, Tool.getCurrentTime());
            v.add(3, alarmEntity.getAlarmId());
            passAlarmModel.addRow(v);
            logger.info("报警信息");
        } catch (Exception e) {
            logger.error("新增报警信息出错", e);
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
     * 更新通行失败
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
     * 存储事件为 其他 的报警记录
     * */
    @Override
    public void saveOtherEvent(AlarmEntity alarmEntity) {
        alarmEntity.setOperator(Egci.accountEntity.getAccountName());
        Egci.session.update("mapping.alarmMapper.updateAlarmNote", alarmEntity);
        Egci.session.commit();
        passAlarmModel.removeRow(menuStatus);
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