package com.dyw.client.timer;

import com.dyw.client.form.MonitorRealTimeForm;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.task.MonitorStatusTask;
import com.dyw.client.task.PingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class MonitorStatusTimer {
    private Logger logger = LoggerFactory.getLogger(MonitorStatusTimer.class);
    private MonitorRealTimeForm monitorRealTimeForm;

    public MonitorStatusTimer(MonitorRealTimeForm monitorRealTimeForm) {
        this.monitorRealTimeForm = monitorRealTimeForm;
    }

    public void open() {
        Timer timer = new Timer();
        MonitorStatusTask pingTask = new MonitorStatusTask(monitorRealTimeForm);
        timer.schedule(pingTask, 240000, 20000);
        logger.info("启用监控端状态监测");
    }
}
