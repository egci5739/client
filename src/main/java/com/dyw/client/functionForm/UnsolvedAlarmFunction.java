package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.NoteEntity;
import com.dyw.client.service.BaseAlarmInterface;
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

public class UnsolvedAlarmFunction implements BaseAlarmInterface {
    private Logger logger = LoggerFactory.getLogger(UnsolvedAlarmFunction.class);
    private JFrame frame;
    private JPanel unsolvedAlarmFunction;
    private JPanel unsolvedAlarmContentPanel;
    private JScrollPane unsolvedAlarmContentScroll;
    private JTable unsolvedAlarmContentTable;

    private DefaultTableModel unsolvedAlarmModel;
    private RowSorter<TableModel> sorter;
    private JPopupMenu popupMenu;
    private int menuStatus = 0;


    public UnsolvedAlarmFunction() {
        List<NoteEntity> noteEntityList = Egci.session.selectList("mapping.configMapper.getNote");
        //初始化报警结果表格
        String[] columnalarmInfo = {"报警名称", "报警详情", "报警时间", "报警id"};
        unsolvedAlarmModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        unsolvedAlarmModel.setColumnIdentifiers(columnalarmInfo);
        unsolvedAlarmContentTable.setModel(unsolvedAlarmModel);
        sorter = new TableRowSorter<TableModel>(unsolvedAlarmModel);
        unsolvedAlarmContentTable.setRowSorter(sorter);
        unsolvedAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        /*
         * 弹出右键菜单
         * */
        popupMenu = new JPopupMenu();
        for (NoteEntity noteEntity : noteEntityList) {
            JMenuItem jMenuItem = new JMenuItem(noteEntity.getNoteName());
            jMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addEvent(popupMenu.getComponentIndex(jMenuItem), jMenuItem.getText(), (int) unsolvedAlarmContentTable.getValueAt(menuStatus, 3));
                }
            });
            popupMenu.add(jMenuItem);
        }
        unsolvedAlarmContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = unsolvedAlarmContentTable.rowAtPoint(e.getPoint());
                    popupMenu.show(unsolvedAlarmContentTable, e.getX(), e.getY());
                }
            }
        });
        try {
            AlarmEntity alarmEntity = new AlarmEntity();
            alarmEntity.setAlarmPermission(Egci.accountEntity.getAccountPermission());
            alarmEntity.setAlarmStatus(0);
            List<AlarmEntity> alarmEntityList = Egci.session.selectList("mapping.alarmMapper.getUnsolvedAlarm", alarmEntity);
            for (AlarmEntity alarmEntity1 : alarmEntityList) {
                addAlarmInfo(alarmEntity1);
            }
        } catch (Exception e) {
            logger.error("查询未处理报警出错", e);
        }
        hideColumn(unsolvedAlarmContentTable, 3);
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
            unsolvedAlarmModel.removeRow(menuStatus);
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
     * 新增报警记录
     * */
    public void addAlarmInfo(AlarmEntity alarmEntity) {
        try {
            Vector v = new Vector();
            v.add(0, alarmEntity.getAlarmName());
            v.add(1, alarmEntity.getAlarmDetail());
            v.add(2, alarmEntity.getCreateTime());
            v.add(3, alarmEntity.getAlarmId());
            unsolvedAlarmModel.addRow(v);
        } catch (Exception e) {
            logger.error("新增报警记录出错", e);
        }

    }

    /*
     * 存储事件为 其他 的通行记录
     * */
    @Override
    public void saveOtherEvent(AlarmEntity alarmEntity) {
        alarmEntity.setOperator(Egci.accountEntity.getAccountName());
        Egci.session.update("mapping.alarmMapper.updateAlarmNote", alarmEntity);
        Egci.session.commit();
        unsolvedAlarmModel.removeRow(menuStatus);
    }

    public void init() {
        frame = new JFrame("未处理报警");
        frame.setContentPane(this.unsolvedAlarmFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
