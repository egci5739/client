package com.dyw.client.task;

import com.dyw.client.controller.Egci;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.MonitorReceiveInfoSocketService;
import com.dyw.client.service.NetStateService;
import com.dyw.client.service.ProtectionReceiveInfoSocketService;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class ProtectionStatusTask extends TimerTask {
    private final Logger logger = LoggerFactory.getLogger(ProtectionStatusTask.class);

    public ProtectionStatusTask() {
    }

    @Override
    public void run() {
        try {
            NetStateService netStateService = new NetStateService();
            if (!netStateService.ping(Egci.configEntity.getSocketIp())) {
                Egci.protectionWorkStatus = 0;
            } else if (netStateService.ping(Egci.configEntity.getSocketIp()) && Egci.protectionWorkStatus == 1) {
                //不执行任何操作
            } else {
                //创建接收通行信息的socket对象
                ProtectionReceiveInfoSocketService protectionReceiveInfoSocketService = new ProtectionReceiveInfoSocketService();
                protectionReceiveInfoSocketService.sendInfo(Tool.getAccessPermissionInfo(Egci.accountEntity.getAccountPermission()));
                protectionReceiveInfoSocketService.start();
            }
        } catch (Exception e) {
            logger.error("重新连接到布控服务程序出错", e);
            Egci.protectionWorkStatus = 0;
        }
    }
}
