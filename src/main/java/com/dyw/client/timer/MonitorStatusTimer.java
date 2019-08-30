package com.dyw.client.timer;

import com.dyw.client.form.MonitorRealTimeForm;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.task.MonitorStatusTask;
import com.dyw.client.task.PingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class MonitorStatusTimer {
    private Logger logger = LoggerFactory.getLogger(MonitorStatusTimer.class);
    private BaseFormService monitorRealTimeForm;

    public MonitorStatusTimer(BaseFormService monitorRealTimeForm) {
        this.monitorRealTimeForm = monitorRealTimeForm;
    }

    public void open() {
        Timer timer = new Timer();
        MonitorStatusTask pingTask = new MonitorStatusTask(monitorRealTimeForm);
        timer.schedule(pingTask, 240000, 20000);
//        timer.schedule(pingTask, 2400, 20000);//attention
        logger.info("启用监控端状态监测");
    }
}
