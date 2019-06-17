package com.dyw.client.service;

import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SendInfoSocketService {
    private Logger logger = LoggerFactory.getLogger(SendInfoSocketService.class);
    private OutputStream os;
    private Socket socket;

    /*
     * 构造函数
     * */
    public SendInfoSocketService(String ip, short port) {
        try {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
        } catch (IOException e) {
            logger.error("创建消息发送体出错", e);
            Tool.showMessage("连接服务程序失败", "提示", 0);
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
     * 接收一次服务端消息
     * */
    public String receiveInfoOnce() {
        String info = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            info = br.readLine();
            if (info != null) {
                logger.info("接收到的消息为" + info);
                return info;
            } else {
                return "0";
            }
        } catch (IOException e) {
            logger.error("服务程序断开", e);
            return "0";
        }
    }
}
