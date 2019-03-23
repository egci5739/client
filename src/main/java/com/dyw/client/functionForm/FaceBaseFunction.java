package com.dyw.client.functionForm;

import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.form.ProtectionForm;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FaceBaseFunction {
    private JTextField baseNameText;
    private JTextField baseNoteText;
    private JButton confirmButton;
    private JButton cancelButton;
    private JLabel baseNameLabel;
    private JLabel baseNoteLabel;
    private JPanel FaceBaseFunction;
    private JFrame frame;

    /*
     *status   0:添加；1：修改
     * */
    public FaceBaseFunction(final ProtectionForm protectionForm, final FDLibEntity fdLibEntity) {
        if (fdLibEntity != null) {
            baseNameText.setText(fdLibEntity.getName());
            baseNoteText.setText(fdLibEntity.getCustomInfo());
        }
        //操作人脸库
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fdLibEntity == null) {//添加
                    String faceBaseName = baseNameText.getText();
                    String faceBaseNote = baseNoteText.getText();
                    String inboundData = "{\"faceLibType\":\"blackFD\"," + "\"name\":\"" + faceBaseName + "\"," + "\"" + faceBaseNote + "\":\"\"}";
                    int resultStatus = Tool.sendInstructionAndReceiveStatus(3, "/ISAPI/Intelligent/FDLib?format=json", inboundData);
                    if (1 == resultStatus) {
                        Tool.showMessage("添加成功", "结果", 0);
                        protectionForm.getFDLib();
                        frame.dispose();
                    } else {
                        Tool.showMessage("添加失败", "结果", 0);
                    }
                } else {//修改
                    String faceBaseName = baseNameText.getText();
                    String faceBaseNote = baseNoteText.getText();
                    String instruction = "/ISAPI/Intelligent/FDLib?format=json&FDID=" + fdLibEntity.getFDID() + "&faceLibType=" + fdLibEntity.getFaceLibType();
                    String inboundData = "{\"name\":\"" + faceBaseName + "\",\"customInfo\":\"" + faceBaseNote + "\"}";
                    int resultStatus = Tool.sendInstructionAndReceiveStatus(2, "/ISAPI/Intelligent/FDLib?format=json&FDID=" + fdLibEntity.getFDID() + "&faceLibType=" + fdLibEntity.getFaceLibType(), inboundData);
                    if (1 == resultStatus) {
                        Tool.showMessage("修改成功", "结果", 0);
                        protectionForm.getFDLib();
                        frame.dispose();
                    } else {
                        Tool.showMessage("修改失败,错误码：" + resultStatus, "结果", 0);
                    }
                }
            }
        });
        //取消
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void init() {
        frame = new JFrame("FaceBaseFunction");
        frame.setContentPane(this.FaceBaseFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
