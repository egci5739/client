package com.dyw.client.controller;

import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.form.RegisterForm;
import com.dyw.client.service.DatabaseService;
import com.dyw.client.service.SessionService;
import com.dyw.client.timer.PingTimer;
import com.dyw.client.tool.Tool;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

public class Egci {
    private static Logger logger;
    public static ConfigEntity configEntity;
    public static Statement statement;
    public static SqlSession session;
    private static RegisterForm registerForm;
    public static int workStatus;

    /*
     * 初始化客户端程序
     * */
    public static void initClient() {
        //初始化状态
        workStatus = 0;
        //初始化日志对象
        logger = LoggerFactory.getLogger(Egci.class);
        //获取配置文件
        configEntity = Tool.getConfig(System.getProperty("user.dir") + "/config/config.xml");
        //创建数据库连接对象
        DatabaseService databaseService = new DatabaseService(configEntity.getDataBaseIp(), configEntity.getDataBasePort(), configEntity.getDataBaseName(), configEntity.getDataBasePass(), configEntity.getDataBaseLib());
        statement = databaseService.connection();
        //创建session对象
        SessionService sessionService = new SessionService();
        session = sessionService.createSession();
        //创建客户端
        registerForm = new RegisterForm();
        registerForm.init();
        //启用ping功能
        PingTimer pingTimer = new PingTimer(registerForm);
        pingTimer.open();
    }

    /*
     * 主函数
     * */
    public static void main(String[] args) {
        Egci.initClient();
    }
}
