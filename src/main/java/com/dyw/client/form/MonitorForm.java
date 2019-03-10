package com.dyw.client.form;

import com.dyw.client.controller.DateSelector;
import com.dyw.client.controller.PhotoTableCellRenderer;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.entity.PassInfoEntity;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.service.DatabaseService;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MonitorForm {
    private DefaultTableModel passSuccessModel;
    private DefaultTableModel passFaultModel;
    private JPanel monitor;
    private ConfigEntity configEntity;
    private Statement statement;
    private String passCardSelectionDefaultHint = "请输入卡号";
    private String nameSelectionDefaultHint = "请输入姓名";

    private JPanel TabbedPane;
    private JTabbedPane tabbedPane1;
    private JPanel monitorRealTime;
    private JPanel monitorHistory;
    private JPanel passSuccessPanel;
    private JPanel passFaultPanel;
    private JPanel passAlarmPanel;
    private JPanel passSuccessTitlePanel;
    private JPanel passSuccessContentPanel;
    private JLabel passSuccessTitleLabel;
    private JPanel passFaultTitlePanel;
    private JPanel passFaultContentPanel;
    private JLabel passFaultTitleLabel;
    private JPanel passAlarmTitlePanel;
    private JPanel passAlarmContentPanel;
    private JLabel passAlarmTitleLabel;
    private JTable passSuccessContentTable;
    private JScrollPane passSuccessContentScroll;
    private JScrollPane passFaultContentScroll;
    private JTable passFaultContentTable;
    private JButton button1;
    private JPanel conditionSelectionPanel;
    private JPanel resultContentPanel;
    private JPanel pageSelectionPanel;
    private JButton dateChoserButton;
    private JComboBox equipmentSelectionCombo;
    private JComboBox eventSelectionCombo;
    private JTextField passCardSelectionText;
    private JTextField nameSelectionText;
    private JLabel startTimeSelectionLabel;
    private JButton startTimeSelectionButton;
    private JLabel endTimeSelectionLabel;
    private JButton endTimeSelectionButton;
    private JButton searchButton;
    private StaffEntity staffEntity;

    public static void main(String[] args) {

    }

    /*
     * 构造函数
     * */
    public MonitorForm() {
        equipmentSelectionCombo.addItem("--请选择--");    //向下拉列表中添加一项
        equipmentSelectionCombo.addItem("身份证");
        equipmentSelectionCombo.addItem("驾驶证");
        equipmentSelectionCombo.addItem("军官证");

        configEntity = Tool.getConfig(System.getProperty("user.dir") + "/config/config.xml");
        //创建数据库连接对象
        DatabaseService databaseService = new DatabaseService(configEntity.getDataBaseIp(), configEntity.getDataBasePort(), configEntity.getDataBaseName(), configEntity.getDataBasePass(), configEntity.getDataBaseLib());
        statement = databaseService.connection();
        String sql = "select * from Staff WHERE CardNumber = '666666'";
        ResultSet rs = null;
        staffEntity = new StaffEntity();
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                //如果对象中有数据，就会循环打印出来
                staffEntity.setName(rs.getString("Name"));
                staffEntity.setNameEn(rs.getString("NameEn"));
                staffEntity.setCardId(rs.getString("CardId"));
                staffEntity.setCardNumber(rs.getString("CardNumber"));
                staffEntity.setBirthday(rs.getString("Birthday"));
                staffEntity.setSex(rs.getString("Sex"));
                staffEntity.setPhoto(rs.getBytes("Photo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //初始化结果表
        String[] columnNames = {"人员底图", "抓拍图片", "比对信息"};
        passSuccessModel = new DefaultTableModel();
        passSuccessModel.setColumnIdentifiers(columnNames);
        passSuccessContentTable.setModel(passSuccessModel);
        passFaultModel = new DefaultTableModel();
        passFaultModel.setColumnIdentifiers(columnNames);
        passFaultContentTable.setModel(passFaultModel);

        TableCellRenderer tableCellRenderer = new PhotoTableCellRenderer();
        passSuccessContentTable.setDefaultRenderer(Object.class, tableCellRenderer);


        System.out.println(staffEntity.getPhoto().toString());
        Vector v = new Vector();
//        v.add(0, Base64.encodeBytes(staffEntity.getPhoto()));
//        v.add(1, Base64.encodeBytes(staffEntity.getPhoto()));
//        v.add(2, Tool.displayPassSuccessResult(passInfoEntity));
//        passSuccessModel.addRow(v);
//        passSuccessModel.addRow(v);
//        passSuccessModel.addRow(v);
        button1.setText("按钮");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                PassInfoEntity passInfoEntity = new PassInfoEntity();
                passInfoEntity.setPassName("egci");
                passInfoEntity.setPassCard("666666");
                passInfoEntity.setPassTime(df.format(new Date()));
                Vector v = new Vector();
                v.add(0, Base64.encodeBytes(staffEntity.getPhoto()));
                v.add(1, Base64.encodeBytes(staffEntity.getPhoto()));
                v.add(2, Tool.displayPassSuccessResult(passInfoEntity));
                passSuccessModel.addRow(v);
            }
        });
        //设备选择下拉框
        equipmentSelectionCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String itemSize = (String) e.getItem();
                    try {
                        System.out.println(itemSize);
                    } catch (Exception ex) {

                    }
                }
            }
        });
        //输入卡号框选中/未选中时
        passCardSelectionText.setText(passCardSelectionDefaultHint);
        passCardSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passCardSelectionText.getText().equals(passCardSelectionDefaultHint)) {
                    passCardSelectionText.setText("");
                }
            }
        });
        passCardSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (passCardSelectionText.getText().equals("")) {
                    passCardSelectionText.setText(passCardSelectionDefaultHint);
                }
            }
        });
        //输入姓名框选中/未选中时
        nameSelectionText.setText(nameSelectionDefaultHint);
        nameSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameSelectionText.getText().equals(nameSelectionDefaultHint)) {
                    nameSelectionText.setText("");
                }
            }
        });
        nameSelectionText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (nameSelectionText.getText().equals("")) {
                    nameSelectionText.setText(nameSelectionDefaultHint);
                }
            }
        });
    }

    /*
     * 初始化函数
     * */
    public void init() {
        JFrame frame = new JFrame("MonitorForm");
        frame.setContentPane(new MonitorForm().monitor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        dateChoserButton = new DateSelector();
        startTimeSelectionButton = new DateSelector();
        endTimeSelectionButton = new DateSelector();
    }
}
