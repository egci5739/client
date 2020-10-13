package com.dyw.client.service;

import com.alibaba.fastjson.JSON;
import com.dyw.client.controller.Egci;
//import com.dyw.client.form.MonitorForm;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.form.MonitorRealTimeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class MonitorReceiveInfoSocketService extends Thread {
    private final Logger logger = LoggerFactory.getLogger(MonitorReceiveInfoSocketService.class);
    private OutputStream os;
    private Socket socket;

    /*
     * 构造函数
     * MonitorRealTime
     * */
    public MonitorReceiveInfoSocketService() {
        try {
            socket = new Socket(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());//attention
            os = socket.getOutputStream();
        } catch (IOException e) {
            logger.error("创建消息发送体出错", e);
//            JOptionPane.showMessageDialog(null, "连接服务程序失败", "错误提示", 0);
        }
    }

    /*
     * 发送消息到服务端
     * */
    public void sendInfo(String info) {
        try {
            os.write((info + "\n").getBytes());
            os.flush();
        } catch (Exception e) {
            logger.error("发送消息到服务端出错", e);
        }
    }

    /*
     * 持续接收服务端消息
     * */
    private void receiveInfo() {
        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String info;
                info = br.readLine();
                if (info != null) {
//                    logger.info("接收到的消息为" + info);
                    try {
                        if (info.equals("success")) {
                            Egci.monitorWorkStatus = 1;
                            Egci.equipmentTreeForm.changeStatus(1);
                        } else if (info.split("#")[0].equals("status")) {
//                            equipmentEntityList = JSON.parseArray(info.split("#")[1], EquipmentEntity.class);
                            Egci.equipmentTreeForm.getEquipmentStatus();
                        } else if (info.split("#")[0].equals("alarm")) {
//                            Egci.alarmForm.addAlarmInfo(JSON.parseObject(info.split("#")[1], AlarmEntity.class));
                            Egci.monitorRealTimeForm.addAlarmInfo(JSON.parseObject(info.split("#")[1], AlarmEntity.class));

                        } else if (info.split("#")[0].equals("stress")) {
                            Egci.monitorRealTimeForm.addAlarmInfo(JSON.parseObject(info.split("#")[1], AlarmEntity.class));
                        } else {
                            PassRecordEntity passInfoEntity = Egci.session.selectOne("mapping.passRecordMapper.getPassInfo", info);
                            if (passInfoEntity != null) {
                                try {
                                    Egci.monitorRealTimeForm.addPassInfo(passInfoEntity);
                                } catch (Exception ignored) {

                                }
                                Egci.accessRecordForm.addPassInfo(passInfoEntity);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("接收数据出错", e);
                        SessionService sessionService = new SessionService();//attention
                        Egci.session = sessionService.createSession();
                    }
                }
            } catch (IOException e) {
                logger.error("服务端关闭连接", e);
                Egci.monitorWorkStatus = 0;
                Egci.equipmentTreeForm.changeStatus(0);
//                Tool.showMessage("正在重新连接服务器...", "提示", 1);
                break;
            }
        }
    }

    @Override
    public void run() {
        receiveInfo();
    }
}
