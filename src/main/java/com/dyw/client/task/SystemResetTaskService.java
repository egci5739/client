package com.dyw.client.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class SystemResetTaskService extends TimerTask {
    private final Logger logger = LoggerFactory.getLogger(SystemResetTaskService.class);

    @Override
    public void run() {
        System.exit(0);
    }
}
