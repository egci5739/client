package com.dyw.client.form;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TabbedForm {
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JPanel onePanel;

    public TabbedForm() {
        tabbedPane2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TabbedForm");
        frame.setContentPane(new TabbedForm().form);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel form;
    private JPanel twoPanel;
    private JLabel twoLabel;
    private JLabel oneLabel;
}
