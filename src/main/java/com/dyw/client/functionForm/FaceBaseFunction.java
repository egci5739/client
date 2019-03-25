package com.dyw.client.functionForm;

import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.form.ProtectionForm;
import com.dyw.client.tool.Tool;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
                try {
                    if (fdLibEntity == null) {//添加
                        String faceBaseName = baseNameText.getText();
                        String faceBaseNote = baseNoteText.getText();
                        JSONObject inboundData = new JSONObject();
                        inboundData.put("faceLibType", "blackFD");
                        inboundData.put("name", faceBaseName);
                        inboundData.put("customInfo", faceBaseNote);
                        JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, "/ISAPI/Intelligent/FDLib?format=json", inboundData);
                        if (resultData.getInt("statusCode") == 1) {
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
                        JSONObject inboundData = new JSONObject();
                        inboundData.put("name", faceBaseName);
                        inboundData.put("customInfo", faceBaseNote);
                        JSONObject resultData = Tool.sendInstructionAndReceiveStatus(2, instruction, inboundData);
                        if (resultData.getInt("statusCode") == 1) {
                            Tool.showMessage("修改成功", "结果", 0);
                            protectionForm.getFDLib();
                            frame.dispose();
                        } else {
                            Tool.showMessage("修改失败,错误码：" + resultData.getInt("statusCode"), "结果", 0);
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
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
