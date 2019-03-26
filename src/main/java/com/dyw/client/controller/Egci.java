package com.dyw.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.form.*;
import com.dyw.client.service.DatabaseService;
import com.dyw.client.service.SessionService;
import com.dyw.client.timer.PingTimer;
import com.dyw.client.tool.Tool;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Egci {
    private static Logger logger;
    public static ConfigEntity configEntity;
    public static SqlSession session;
    public static RegisterForm registerForm;
    public static int workStatus;
    public static MonitorForm monitorForm;
    public static AccountEntity accountEntity;
    private static LoginForm loginForm;
    public static SystemForm systemForm;
    public static ProtectionForm protectionForm;

    /*
     * 初始化客户端程序
     * */
    public static void initClient() {
        accountEntity = new AccountEntity();
        //初始化系统状态
        workStatus = 0;
        //初始化日志对象
        logger = LoggerFactory.getLogger(Egci.class);
        //获取配置文件
        configEntity = Tool.getConfig(System.getProperty("user.dir") + "/config/config.xml");
        //创建session对象
        SessionService sessionService = new SessionService();
        session = sessionService.createSession();
        //创建登陆客户端
        loginForm = new LoginForm();
        loginForm.init();
    }

    /*
     * 主函数
     * */
    public static void main(String[] args) {
        Egci.initClient();
    }
}
