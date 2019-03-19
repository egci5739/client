package com.dyw.client.form;

import com.dyw.client.controller.WebcamViewerExample;

import javax.swing.*;

public class ProtectionForm {
    private JPanel protection;

    public void init() {
        JFrame frame = new WebcamViewerExample();
        frame.setContentPane(this.protection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
