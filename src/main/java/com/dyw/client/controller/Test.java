package com.dyw.client.controller;

import java.awt.BorderLayout;
import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;


public class Test extends JFrame {


    private JPanel contentPane;

    public static void main(String[] args) {
        Test frame = new Test();
        frame.setVisible(true);
    }


    public Test() {
        setBounds(100, 100, 350, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JButton button = new DateSelector();
        contentPane.add(button, BorderLayout.CENTER);
    }


}