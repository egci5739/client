package com.dyw.client.timer;

import com.dyw.client.form.RegisterForm;
import com.dyw.client.task.PingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class PingTimer {
    private Logger logger = LoggerFactory.getLogger(PingTimer.class);
    private RegisterForm registerForm;

    public PingTimer(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }


    public void open() {
        Timer timer = new Timer();
        PingTask pingTask = new PingTask(registerForm);
        timer.schedule(pingTask, 1000, 20000);
        logger.info("启用系统状态监测");
    }
}
