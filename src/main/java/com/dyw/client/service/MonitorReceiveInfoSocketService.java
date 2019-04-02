package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.CollectionEntity;
import com.dyw.client.entity.PassInfoEntity;
//import com.dyw.client.form.MonitorForm;
import com.dyw.client.form.MonitorRealTimeForm;
import com.dyw.client.form.RegisterForm;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MonitorReceiveInfoSocketService extends Thread {
    private Logger logger = LoggerFactory.getLogger(MonitorReceiveInfoSocketService.class);
    private OutputStream os;
    private Socket socket;
    private MonitorRealTimeForm monitorRealTimeForm;

    /*
     * 构造函数
     * */
    public MonitorReceiveInfoSocketService(MonitorRealTimeForm monitorRealTimeForm) {
        try {
            socket = new Socket(Egci.configEntity.getServerIp(), Egci.configEntity.getServerPort());
            this.monitorRealTimeForm = monitorRealTimeForm;
            os = socket.getOutputStream();
        } catch (IOException e) {
            logger.error("创建消息发送体出错", e);
            JOptionPane.showMessageDialog(null, "连接服务程序失败", "错误提示", 0);
        }
    }

    /*
     * 发送消息到服务端
     * */
    public void sendInfo(String info) {
        try {
            os.write((info + "\n").getBytes());
            os.flush();
        } catch (IOException e) {
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
                String info = null;
                info = br.readLine();
                if (info == null) {
                } else {
                    logger.info("接收到的消息为" + info);
                    try {
                        PassInfoEntity passInfoEntity = Egci.session.selectOne("mapping.passInfoMapper.getPassInfo", info);
                        if (passInfoEntity != null) {
                            monitorRealTimeForm.addPassInfo(passInfoEntity);
                        }
                    } catch (TooManyResultsException ignored) {

                    }
                }
            } catch (IOException e) {
                logger.error("服务端关闭连接", e);
                Egci.workStatus = 1;
//                monitorForm.changeCommunicationStatus(1);
                break;
            }
        }
    }

    @Override
    public void run() {
//        monitorForm.changeCommunicationStatus(0);
        Egci.workStatus = 0;
        receiveInfo();
    }
}
