package com.dyw.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.protection.AlarmResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

//import com.dyw.client.form.MonitorForm;

public class ProtectionReceiveInfoSocketService extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ProtectionReceiveInfoSocketService.class);
    private OutputStream os;
    private Socket socket;

    /*
     * 构造函数
     * */
    public ProtectionReceiveInfoSocketService() {
        try {
            socket = new Socket(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketProtectionPort());
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
            logger.error("发送消息到布控端出错", e);
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
                            Egci.protectionWorkStatus = 1;
                        } else {
                            try {
                                Egci.intelligentApplicationForm.showAlarmInfo(JSON.parseObject(info, AlarmResultEntity.class));
                            } catch (Exception e) {
                                logger.error("接收单个布控报警出错", e);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("接收数据出错", e);
                    }
                }
            } catch (IOException e) {
                logger.error("布控端关闭连接", e);
                Egci.protectionWorkStatus = 0;
                break;
            }
        }
    }

    @Override
    public void run() {
        receiveInfo();
    }
}
