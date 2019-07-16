package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.tool.Tool;

import javax.swing.*;

public class PassInfoFunction {
    private JPanel passInfoFunction;
    private JPanel staffImagePanel;
    private JPanel snapImagePanel;
    private JPanel passInfoPanel;
    private JLabel staffImageLabel;
    private JLabel snapImageLabel;
    private JLabel passInfoLabel;
    private JFrame frame;

    public void init(int id) {
        frame = new JFrame("通行信息");
        frame.setContentPane(this.passInfoFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        PassRecordEntity passRecordEntity = new PassRecordEntity();
        passRecordEntity.setPassRecordId(id);
        passRecordEntity = Egci.session.selectOne("mapping.passRecordMapper.getPassInfo", passRecordEntity);

        ImageIcon imageIconStaff = new ImageIcon(passRecordEntity.getPassRecordStaffImage());
        staffImageLabel.setIcon(Tool.getImageScale(imageIconStaff, imageIconStaff.getIconWidth(), imageIconStaff.getIconHeight(), staffImageLabel.getHeight(), 2));

        ImageIcon imageIconSnap = new ImageIcon(passRecordEntity.getPassRecordCaptureImage());
        snapImageLabel.setIcon(Tool.getImageScale(imageIconSnap, imageIconSnap.getIconWidth(), imageIconSnap.getIconHeight(), staffImageLabel.getHeight(), 2));

        passInfoLabel.setText("姓名：" + passRecordEntity.getPassRecordName() + "   " + "卡号：" + passRecordEntity.getPassRecordCardNumber());
    }
}
