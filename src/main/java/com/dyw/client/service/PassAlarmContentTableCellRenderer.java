package com.dyw.client.service;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PassAlarmContentTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        System.out.println(value.toString());
        if (value.equals("胁迫报警")) {
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        } else if (value.equals("离线报警")) {
            setBackground(Color.YELLOW);
            setForeground(Color.BLACK);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
