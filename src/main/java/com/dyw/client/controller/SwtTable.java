package com.dyw.client.controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SwtTable {

    public static void main(String[] args) {

        new FrmMains().setVisible(true);

    }
}

class FrmMains extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable productTable;

    private Vector<Vector<Object>> productData;

    public FrmMains() {

        this.setTitle("列表");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();

        JButton btnRefresh = new JButton("修改");

        btnRefresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 端口正则表达式
                String dk = "^[0-9]*$";
                // 时间正则表达式
//				String time = "^[0-9]*$";
                // IP地址正则表达式
                String ip = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\\."
                        + "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\\."
                        + "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])\\."
                        + "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
                // 登录名正则表达式
                String dl = "^[A-Za-z0-9]+$";
                // Map<Integer,String> map = new HashMap<>();
                // map.put(1, dk);
                // map.put(5, dk);
                // map.put(11, dk);
                // map.put(12, dk);
                // map.put(19, dk);
                // map.put(26, dk);
                // map.put(28, dk);
                // map.put(9, time);
                // map.put(13, time);
                // map.put(14, time);
                // map.put(15, time);
                // map.put(16, time);
                // map.put(17, time);
                // map.put(20, time);
                // map.put(21, time);
                // map.put(22, time);
                // map.put(4, ip);
                // map.put(10, ip);
                // map.put(18, ip);
                // map.put(23, ip);
                // map.put(24, ip);
                // map.put(25, ip);
                // map.put(27, ip);
                // map.put(2, dl);
                // map.put(6, dl);
                // map.put(3, dl);
                // map.put(7, dl);
                // map.put(8, dl);
                int ii = productTable.getSelectedRow();
                if (ii == -1) {
                    JOptionPane.showMessageDialog(null, "请选中你要修改的行");
                    return;
                }
                int Id = (int) productTable.getValueAt(productTable.getSelectedRow(), 0);
                String Value = (String) productTable.getValueAt(productTable.getSelectedRow(), 2);
                String Note = (String) productTable.getValueAt(productTable.getSelectedRow(), 3);
                boolean isMatch = Pattern.matches(dk, Value);
                boolean isMatchs = Pattern.matches(ip, Value);
                boolean isMatchss = Pattern.matches(dl, Value);
                // System.out.println(isMatch);
                //端口格式错误
                if (isMatch == false
                        && (Id == 1 || Id == 5 || Id == 11 || Id == 12 || Id == 19 || Id == 26 || Id == 28)) {
                    JOptionPane.showMessageDialog(null, "格式错误,请重新输入");
                    return;
                }
                //时间格式错误
                if (isMatch == false && (Id == 9 || Id == 13 || Id == 14 || Id == 15 || Id == 16 || Id == 17 || Id == 20
                        || Id == 21 || Id == 22)) {
                    JOptionPane.showMessageDialog(null, "格式错误,请重新输入");
                    return;
                }
                //IP格式错误
                if (isMatchs == false
                        && (Id == 4 || Id == 10 || Id == 18 || Id == 23 || Id == 24 || Id == 25 || Id == 27)) {
                    JOptionPane.showMessageDialog(null, "格式错误,请重新输入");
                    return;
                }
                //登录名格式错误
                if (isMatchss == false && (Id == 2 || Id == 3 || Id == 6 || Id == 7 || Id == 8)) {
                    JOptionPane.showMessageDialog(null, "格式错误,请重新输入");
                    return;
                }
//				if(isdl==true&&Id==13){
//					JOptionPane.showMessageDialog(null, "只能输入0或者2");
//					return;
//				}
                System.out.println(productTable.getValueAt(productTable.getSelectedRow(), 0));
                System.out.println((String) productTable.getValueAt(productTable.getSelectedRow(), 2));
                System.out.println((String) productTable.getValueAt(productTable.getSelectedRow(), 3));
                UpdateProductList(Value, Note);
            }
        });
        toolbar.add(btnRefresh);

        this.add(toolbar, BorderLayout.NORTH);

        TableColumnModel columnModel = new DefaultTableColumnModel();
        TableColumn column = new TableColumn(0, 50);
        column.setHeaderValue("编号");
        columnModel.addColumn(column);

        column = new TableColumn(1, 100);
        column.setHeaderValue("名称");
        columnModel.addColumn(column);

        column = new TableColumn(1, 100);
        column.setHeaderValue("价值");
        columnModel.addColumn(column);

        column = new TableColumn(1, 100);
        column.setHeaderValue("注释");
        columnModel.addColumn(column);

        this.productData = new Vector<Vector<Object>>();

        Vector<Object> columnsData = new Vector<Object>();
        columnsData.add("configId");
        columnsData.add("configName");
        columnsData.add("configValue");
        columnsData.add("configNote");
        this.productTable = new JTable(productData, columnsData);
        this.add(new JScrollPane(productTable), BorderLayout.CENTER);
        reloadProductList();
    }

    // 加载数据
    private void reloadProductList() {

        this.productData.clear();

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 数据库用户
            String user = "sa";

            // 数据库密码
            String password = "hik12345";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:51178; DatabaseName=FaceRecognition", user, password);

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select configId,configName,configValue,configNote  from ConfigTable");

            while (rs.next()) {
                Vector<Object> product = new Vector<Object>();
                product.add(rs.getInt(1));
                product.add(rs.getString(2));
                product.add(rs.getString(3));
                product.add(rs.getString(4));

                this.productData.add(product);
                productTable.updateUI();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改数据
    private void UpdateProductList(String configValue, String configNote) {
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 数据库用户
            String user = "sa";

            // 数据库密码
            String password = "hik12345";

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:51178; DatabaseName=FaceRecognition", user, password);

            Statement stmt = conn.createStatement();

            String Value = (String) productTable.getValueAt(productTable.getSelectedRow(), 2);
            String Note = (String) productTable.getValueAt(productTable.getSelectedRow(), 3);
            String sql = "update ConfigTable set configValue=" + "'" + Value + "'" + " where configNote=" + "'" + Note
                    + "'";
            PreparedStatement pStatement = conn.prepareStatement(sql);

            int i = pStatement.executeUpdate();
            if (i > 0) {
                System.out.println("修改成功");
            } else {
                System.out.println("修改失败");
            }
            pStatement.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
