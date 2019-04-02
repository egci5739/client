package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.form.PersonManagementForm;
import com.dyw.client.tool.Tool;
import org.json.JSONException;

import javax.swing.*;
import java.util.List;

public class ImportPersonProgressService extends Thread {
    private JProgressBar jProgressBar;
    private PersonManagementForm personManagementForm;
    private String FDID;
    private JButton jButton;

    public ImportPersonProgressService(JProgressBar jProgressBar, PersonManagementForm personManagementForm, String FDID, JButton jButton) {
        this.jProgressBar = jProgressBar;
        this.personManagementForm = personManagementForm;
        this.FDID = FDID;
        this.jButton = jButton;
    }

    @Override
    public void run() {
        //设置进度条信息
        jProgressBar.setVisible(true);
        jProgressBar.setMaximum(100);
        jProgressBar.setMinimum(0);
        jProgressBar.setString("正在导入，请稍后");
        jProgressBar.setStringPainted(true);
        List<StaffEntity> staffEntityList = Egci.session.selectList("mapping.staffMapper.getAllStaff");
        int num = 0;
        for (StaffEntity staffEntity : staffEntityList) {
            String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, FDID, staffEntity.getPhoto(), null);
            org.json.JSONObject inboundData = new org.json.JSONObject();
            try {
                inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
                inboundData.put("faceLibType", "blackFD");
                inboundData.put("FDID", FDID);
                inboundData.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
                inboundData.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
                if (staffEntity.getBirthday() == null) {
                    staffEntity.setBirthday("1900-01-01");
                }
                inboundData.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getBirthday()));
                org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            num++;
            jProgressBar.setValue((int) ((float) num / (float) staffEntityList.size() * 100));
        }
        jButton.setEnabled(true);
        jProgressBar.setVisible(false);
    }
}
