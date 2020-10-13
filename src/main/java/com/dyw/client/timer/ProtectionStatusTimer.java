package com.dyw.client.timer;

import com.dyw.client.task.ProtectionStatusTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class ProtectionStatusTimer {
    private final Logger logger = LoggerFactory.getLogger(ProtectionStatusTimer.class);

    public ProtectionStatusTimer() {
    }

    public void open() {
        Timer timer = new Timer();
        ProtectionStatusTask protectionStatusTask = new ProtectionStatusTask();
        timer.schedule(protectionStatusTask, 240000, 20000);
//        timer.schedule(pingTask, 2400, 20000);//attention
        logger.info("启用布控端状态监测");
    }
}
