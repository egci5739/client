package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.tool.Tool;

import java.io.File;

public class ImportService {
    public void import1() {
        //获取所有文件
        File fileAll = new File("H:\\FaceRecognition\\client\\staffInfo");
        File[] tempList = fileAll.listFiles();
        for (File file : tempList) {
            StaffEntity staffEntity = new StaffEntity();
            String[] staffInfo = file.getName().substring(0, file.getName().length() - 4).split("_");
            staffEntity.setStaffName(staffInfo[0]);
            staffEntity.setStaffCardNumber(staffInfo[1]);
            staffEntity.setStaffImage(Tool.getPictureStream(file.getAbsolutePath()));
            Egci.session.insert("mapping.staffMapper.insertStaff", staffEntity);
            Egci.session.commit();
        }
    }
}
