package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.FaceCollectionEntity;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RegisterReceiveInfoSocketService extends Thread {
    private final Logger logger = LoggerFactory.getLogger(RegisterReceiveInfoSocketService.class);
    private OutputStream os;
    private Socket socket;
    private RegisterForm registerForm;

    /*
     * 构造函数
     * */
    public RegisterReceiveInfoSocketService(RegisterForm registerForm) {
        try {
            socket = new Socket(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketRegisterPort());
            this.registerForm = registerForm;
            os = socket.getOutputStream();
        } catch (IOException e) {
            logger.error("创建消息发送体出错", e);
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
                String info = null;
                info = br.readLine();
                if (info != null) {
                    logger.info("接收到的消息为" + info);
                    if (info.equals("error")) {
                        registerForm.changeCommunicationStatus(3);
                        Egci.workStatus = 3;
//                        registerForm.reconnectToServer();//attention
                    } else {
                        try {
                            FaceCollectionEntity collectionEntity = Egci.session.selectOne("mapping.staffMapper.getStaffCollectionInfo", Integer.parseInt(info));
                            Egci.session.commit();
                            registerForm.fillCollectionInfo(collectionEntity);
                        } catch (NumberFormatException e) {
                            registerForm.changeCommunicationStatus(0);
                            Egci.workStatus = 0;
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("服务端关闭连接", e);
//                Tool.showMessage("与服务程序断开连接", "提示", 1);
                Egci.workStatus = 1;
                registerForm.changeCommunicationStatus(1);
                break;
            }
        }
    }

    @Override
    public void run() {
        receiveInfo();
    }
}
