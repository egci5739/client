package com.dyw.client.task;

import com.dyw.client.controller.Egci;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.service.NetStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class PingTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(PingTask.class);
    private NetStateService netStateService;
    private RegisterForm registerForm;

    public PingTask(RegisterForm registerForm) {
        netStateService = new NetStateService();
        this.registerForm = registerForm;
    }

    /*
     *状态说明：
     * 0：通信正常
     * 1：服务程序断开
     * 2：服务器网络异常
     * 3：采集设备网络异常
     * 4：全部网络已断开
     * */
    @Override
    public void run() {
        try {
            if (!netStateService.ping(Egci.configEntity.getServerIp()) && !netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {
                registerForm.changeCommunicationStatus(4);
            } else if (!netStateService.ping(Egci.configEntity.getServerIp())) {
                registerForm.changeCommunicationStatus(2);
            } else if (Egci.workStatus == 1) {
                registerForm.changeCommunicationStatus(5);
                registerForm.changeCommunicationStatus(1);
                registerForm.reconnectToServer();
            } else if (!netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {
                registerForm.changeCommunicationStatus(3);
            } else {
                registerForm.changeCommunicationStatus(0);
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
