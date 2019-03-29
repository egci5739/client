package com.dyw.client.functionForm;

import com.dyw.client.form.ProtectionForm;
import com.dyw.client.tool.Tool;
import org.json.JSONException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FaceInfoFunction {
    private JFrame frame;
    private JPanel FaceInfoFunction;
    private JTextField faceInfoNameText;
    private JComboBox faceInfoSexComboBox;
    private JTextField faceInfoBirthdayText;
    private JTextField faceInfoNoteText;
    private JButton faceInfoConfirmButton;
    private JButton faceInfoCancelButton;
    private JLabel faceInfoNameLabel;
    private JLabel faceInfoSexLabel;
    private JLabel faceInfoBirthdayLabel;
    private JLabel faceInfoNoteLabel;
    private JButton faceInfoUploadPictureButton;
    private JLabel faceInfoPictureLabel;
    private byte[] pictureBytes;

    public FaceInfoFunction(final String fdid, final ProtectionForm protectionForm) {
        //设置性别选项
        faceInfoSexComboBox.addItem("unknown");
        faceInfoSexComboBox.addItem("male");
        faceInfoSexComboBox.addItem("female");
        //获取本地图片
        faceInfoUploadPictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser();
                jf.showOpenDialog(null);//显示打开的文件对话框
                File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
                String s = f.getAbsolutePath();//返回路径名
                //JOptionPane弹出对话框类，显示绝对路径名
                pictureBytes = Tool.getPictureStream(s);
                ImageIcon imageIcon = new ImageIcon(pictureBytes);
                faceInfoPictureLabel.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), 210, 1));
            }
        });
        //确认
        faceInfoConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (faceInfoNameText.getText().equals("") || faceInfoBirthdayText.getText().equals("") || pictureBytes == null) {
                    Tool.showMessage("请完善人员信息", "提示", 0);
                    return;
                }
                try {
                    //第一步：先将人员图片发送到脸谱服务器，获取faceURL
                    org.json.JSONObject inboundData = new org.json.JSONObject();
                    org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, fdid, pictureBytes, null);
                    if (resultFaceUrlData == null) {
                        Tool.showMessage("添加失败", "提示", 0);
                        return;
                    }
                    String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
                    inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
                    inboundData.put("faceLibType", "blackFD");
                    inboundData.put("FDID", fdid);
                    inboundData.put("name", faceInfoNameText.getText());//名字_卡号_id
                    inboundData.put("gender", faceInfoSexComboBox.getSelectedItem());
                    inboundData.put("bornTime", faceInfoBirthdayText.getText());
                    org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
                    if (resultData.getInt("statusCode") == 1) {
                        Tool.showMessage("添加成功", "提示", 0);
                        protectionForm.showSelectBase();
                        frame.dispose();
                    } else {
                        Tool.showMessage("添加失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //取消
        faceInfoCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void init() {
        frame = new JFrame("FaceInfoFunction");
        frame.setContentPane(this.FaceInfoFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
