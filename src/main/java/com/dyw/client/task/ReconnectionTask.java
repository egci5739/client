package com.dyw.client.task;

import com.dyw.client.form.RegisterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class ReconnectionTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(ReconnectionTask.class);
    private RegisterForm registerForm;

    public ReconnectionTask(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

    @Override
    public void run() {
        try {
            registerForm.reconnectToServer();
            logger.info("重连了");
        } catch (Exception e) {
            logger.error("自动重连到服务器失败", e);
        }
    }
}
