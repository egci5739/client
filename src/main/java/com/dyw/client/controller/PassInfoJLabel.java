package com.dyw.client.controller;

import com.dyw.client.entity.PassInfoEntity;

import javax.swing.*;

public class PassInfoJLabel {
    public JLabel getPassInfo(PassInfoEntity passInfoEntity) {
        JLabel jLabel = new JLabel();
        jLabel.setText(passInfoEntity.getPassName() + passInfoEntity.getPassCard() + passInfoEntity.getPassTime());
        return jLabel;
    }
}
