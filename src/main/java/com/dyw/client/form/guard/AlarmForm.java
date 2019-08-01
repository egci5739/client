package com.dyw.client.form.guard;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.NoteEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.functionForm.AlarmFunction;
import com.dyw.client.functionForm.NoteFunction;
import com.dyw.client.functionForm.UnsolvedAlarmFunction;
import com.dyw.client.service.BaseAlarmInterface;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

public class AlarmForm implements BaseAlarmInterface {
    private Logger logger = LoggerFactory.getLogger(AlarmForm.class);

    private JPanel alarmForm;
    private JPanel alarmTitlePanel;
    private JPanel alarmContentPanel;
    private JCheckBox alarmTitleCheckBox;
    private JButton alarmTitleButton;
    private JLabel alarmTitleLabel;
    private JTable alarmContentTable;
    private JScrollPane alarmContentScroll;
    private JButton unsolvedAlarmButton;
    private JFrame frame;
    private DefaultTableModel alarmModel;
    private int alarmBottomStatus = 0;
    private JScrollBar alarmScrollBar;//通行滚动条
    private int alarmRollingStatus = 1;//通行成功页面滚动状态:0：禁止；1：滚动
    private JPopupMenu popupMenu;
    private int menuStatus = 0;

    private RowSorter<TableModel> sorter;


    public AlarmForm() {
        List<NoteEntity> noteEntityList = Egci.session.selectList("mapping.configMapper.getNote");
        //初始化报警结果表格
        String[] columnalarmInfo = {"报警名称", "报警详情", "报警时间", "报警id"};
        alarmModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        alarmModel.setColumnIdentifiers(columnalarmInfo);
        alarmContentTable.setModel(alarmModel);
        sorter = new TableRowSorter<TableModel>(alarmModel);
        alarmContentTable.setRowSorter(sorter);
        alarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && alarmBottomStatus <= 3) {
                    alarmContentScroll.getVerticalScrollBar().setValue(alarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - alarmContentScroll.getVerticalScrollBar().getModel().getExtent());
                    alarmBottomStatus++;
                }
            }
        });
        alarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        alarmScrollBar = alarmContentScroll.getVerticalScrollBar();
        //是否滚动
        alarmTitleCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    alarmRollingStatus = 1;
                } else {
                    alarmRollingStatus = 0;
                }
            }
        });
        /*
         * 清空页面
         * */
        alarmTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alarmModel.setRowCount(0);
            }
        });
        /*
         * 弹出右键菜单
         * */
        popupMenu = new JPopupMenu();
        for (NoteEntity noteEntity : noteEntityList) {
            JMenuItem jMenuItem = new JMenuItem(noteEntity.getNoteName());
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addEvent(popupMenu.getComponentIndex(jMenuItem), jMenuItem.getText(), (int) alarmContentTable.getValueAt(menuStatus, 3));
                }
            });
            popupMenu.add(jMenuItem);
        }
        alarmContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = alarmContentTable.rowAtPoint(e.getPoint());
                    popupMenu.show(alarmContentTable, e.getX(), e.getY());
                }
            }
        });
//        try {
//            AlarmEntity alarmEntity = new AlarmEntity();
//            alarmEntity.setAlarmPermission(Egci.accountEntity.getAccountPermission());
//            alarmEntity.setAlarmStatus(0);
//            List<AlarmEntity> alarmEntityList = Egci.session.selectList("mapping.alarmMapper.getUnsolvedAlarm", alarmEntity);
//            for (AlarmEntity alarmEntity1 : alarmEntityList) {
//                addAlarmInfo(alarmEntity1);
//            }
//        } catch (Exception e) {
//            logger.error("查询未处理报警出错", e);
//        }
        hideColumn(alarmContentTable, 3);
        /*
         * 打开未处理报警窗口
         * */
        unsolvedAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnsolvedAlarmFunction unsolvedAlarmFunction = new UnsolvedAlarmFunction();
                unsolvedAlarmFunction.init();
            }
        });
    }

    /*
     * 新增报警记录
     * */
    public void addAlarmInfo(AlarmEntity alarmEntity) {
        try {
            Vector v = new Vector();
            v.add(0, alarmEntity.getAlarmName());
            v.add(1, alarmEntity.getAlarmDetail());
            v.add(2, Tool.getCurrentTime());
            v.add(3, alarmEntity.getAlarmId());
            alarmModel.addRow(v);
            if (alarmRollingStatus == 1) {
                moveScrollBarToBottom(alarmScrollBar);
                alarmBottomStatus = 0;
            }
        } catch (Exception e) {
            logger.error("新增报警记录出错", e);
        }

    }

    /*
     * 更新报警信息
     * */
    private void addEvent(int noteId, String noteName, int passId) {
        AlarmEntity alarmEntity = new AlarmEntity();
        alarmEntity.setAlarmId(passId);
        alarmEntity.setAlarmNoteId(noteId);
        if (noteId == 0) {
            AlarmFunction alarmFunction = new AlarmFunction(this, alarmEntity);
            alarmFunction.init();
        } else {
            alarmEntity.setAlarmNote(noteName);
            alarmEntity.setOperator(Egci.accountEntity.getAccountName());
            Egci.session.update("mapping.alarmMapper.updateAlarmNote", alarmEntity);
            Egci.session.commit();
            alarmModel.removeRow(menuStatus);
        }
    }

    public void init() {
        frame = new JFrame("报警记录");
        frame.setContentPane(this.alarmForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*
     * 存储事件为 其他 的通行记录
     * */
    @Override
    public void saveOtherEvent(AlarmEntity alarmEntity) {
        alarmEntity.setOperator(Egci.accountEntity.getAccountName());
        Egci.session.update("mapping.alarmMapper.updateAlarmNote", alarmEntity);
        Egci.session.commit();
        alarmModel.removeRow(menuStatus);
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
}
