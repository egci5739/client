package com.dyw.client.controller;

import com.dyw.client.tool.Tool;
import net.iharder.Base64;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;

public class PhotoTableCellRenderer extends DefaultTableCellRenderer {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 0 || column == 1) {
            //调用基类方法
            ImageIcon imageIcon = null;
            try {
                imageIcon = new ImageIcon(Base64.decode(value.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JLabel(Tool.getImageScale(imageIcon, 120, 160, 160, 2));
        } else {
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    //该类继承与JLabel，Graphics用于绘制单元格,绘制红线
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        final BasicStroke stroke = new BasicStroke(2.0f);
        g2.setColor(Color.RED);
        g2.setStroke(stroke);
        g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
    }

}
