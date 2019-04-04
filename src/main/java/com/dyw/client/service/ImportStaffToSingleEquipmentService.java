package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;

import java.util.List;

public class ImportStaffToSingleEquipmentService extends Thread {
    private String equipmentIp;//一体机ip

    public ImportStaffToSingleEquipmentService(String equipmentIp) {
        this.equipmentIp = equipmentIp;
    }

    @Override
    public synchronized void start() {
        //第一步：获取全部人员信息
        List<StaffEntity> staffEntityList = Egci.session.selectList("mapping.staffMapper.getAllStaff");
        //登陆设备

    }
}
