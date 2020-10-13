package com.dyw.client.task;

import com.dyw.client.controller.Egci;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.service.NetStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class PingTask extends TimerTask {
    private final Logger logger = LoggerFactory.getLogger(PingTask.class);
    private final NetStateService netStateService;
    private final RegisterForm registerForm;

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
            if (!netStateService.ping(Egci.configEntity.getSocketIp()) && !netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {//与服务器和采集设备断开
                registerForm.changeCommunicationStatus(4);
            } else if (!netStateService.ping(Egci.configEntity.getSocketIp())) {//与服务器网络断开
                registerForm.changeCommunicationStatus(2);
            } else if (Egci.workStatus == 1) {//与服务程序断开
                registerForm.changeCommunicationStatus(1);
                if (netStateService.ping(Egci.configEntity.getSocketIp())) {
                    registerForm.reconnectToServer();
                }
            } else if (!netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {//与采集设备断开
                registerForm.changeCommunicationStatus(3);
                if (netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {
                    registerForm.reconnectToServer();
                }
            } else {
                registerForm.changeCommunicationStatus(0);
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
