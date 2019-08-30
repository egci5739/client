package com.dyw.client.controller;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class Test<T> {
    public static void main(String[] a) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Hello");
                final JTextArea textarea = new JTextArea();
                final JScrollPane scrollpane = new JScrollPane(textarea, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
                frame.getContentPane().add(scrollpane);
                frame.setLocationRelativeTo(null);
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentMoved(ComponentEvent e) {
                        //java.awt.Component c = e.getComponent()
                        //textarea.append(c.getBounds().toString()+"\n");
                        textarea.append(frame.getBounds().toString() + "\n");
                    }
                });
                frame.setPreferredSize(new java.awt.Dimension(600, 400));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
