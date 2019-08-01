package com.dyw.client.task;

import com.dyw.client.controller.Egci;
import com.dyw.client.form.MonitorRealTimeForm;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.NetStateService;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class MonitorStatusTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(MonitorStatusTask.class);
    private NetStateService netStateService;
    private MonitorRealTimeForm monitorRealTimeForm;

    public MonitorStatusTask(MonitorRealTimeForm monitorRealTimeForm) {
        netStateService = new NetStateService();
        this.monitorRealTimeForm = monitorRealTimeForm;
    }

    @Override
    public void run() {
        try {
            NetStateService netStateService = new NetStateService();
            if (netStateService.ping(Egci.configEntity.getSocketIp()) && Egci.monitorWorkStatus == 1) {
            } else {
                //创建接收通行信息的socket对象
                MonitorReceiveInfoSocketService monitorReceiveInfoSocketService = new MonitorReceiveInfoSocketService();
                monitorReceiveInfoSocketService.sendInfo(Tool.getAccessPermissionInfo(Egci.accountEntity.getAccountPermission()));
                monitorReceiveInfoSocketService.start();
//                Egci.monitorWorkStatus = 0;
            }
        } catch (Exception e) {
            logger.error("重新连接到服务程序出错", e);
            Egci.monitorWorkStatus = 0;
        }
    }
}
