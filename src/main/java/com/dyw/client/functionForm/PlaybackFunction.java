package com.dyw.client.functionForm;

import javax.swing.*;
import java.awt.*;

public class PlaybackFunction {
    private JPanel playbackFunction;

    public Panel getPanelPlay() {
        return panelPlay;
    }

    private Panel panelPlay = new Panel();
    private JFrame frame;

    public void init() {
        frame = new JFrame("视频回放");
        frame.setContentPane(this.playbackFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panelPlay = new java.awt.Panel();
        panelPlay.setBackground(new java.awt.Color(234, 255, 255));
        javax.swing.GroupLayout panelPlayLayout = new javax.swing.GroupLayout(panelPlay);
        panelPlay.setLayout(panelPlayLayout);
        panelPlayLayout.setHorizontalGroup(
                panelPlayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 520, Short.MAX_VALUE)
        );
        panelPlayLayout.setVerticalGroup(
                panelPlayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 378, Short.MAX_VALUE)
        );
        playbackFunction.add(panelPlay);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*
     * 退出播放窗口
     * */
    public void dispose() {
        frame.dispose();
    }
}
