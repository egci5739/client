package com.dyw.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ChangeModeService {
    private final Logger logger = LoggerFactory.getLogger(ChangeModeService.class);
    private OutputStream os;
    private Socket socket;

    /*
     * 构造函数
     * */
    public ChangeModeService(String socketIp, int socketPort) {
        try {
            socket = new Socket(socketIp, socketPort);
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
            os.write(info.getBytes());
            os.flush();
        } catch (IOException e) {
            logger.error("发送消息到服务端出错", e);
        }
    }

    /*
     * 接收一次服务端消息
     * */
    public void receiveInfoOnce() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info = null;
            info = br.readLine();
            if (info != null) {
                logger.info("接收到的消息为" + info);
            }
        } catch (IOException e) {
            logger.error("服务程序断开", e);
        }
    }
}
