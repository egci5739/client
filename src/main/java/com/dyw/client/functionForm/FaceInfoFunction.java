package com.dyw.client.functionForm;

import javax.swing.*;

public class FaceInfoFunction {
    private JPanel FaceInfoFunction;
    private JTextField faceInfoCardText;
    private JButton faceInfoConfirmButton;
    private JButton faceInfoCancelButton;
    private JLabel faceInfoCardLabel;

    public void init() {
        JFrame frame = new JFrame("FaceInfoFunction");
        frame.setContentPane(this.FaceInfoFunction);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
