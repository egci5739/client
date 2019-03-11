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

    @Override
    public void run() {
        try {
            if (!netStateService.ping(Egci.configEntity.getServerIp()) || !netStateService.ping(Egci.configEntity.getFaceCollectionIp())) {
                registerForm.changeCommunicationStatus(2);
                logger.info("网络异常");
            } else if (Egci.workStatus == 0) {
                registerForm.changeCommunicationStatus(0);
                logger.info("网络正常");

            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}
