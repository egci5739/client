package com.dyw.client.controller;

import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.form.*;
import com.dyw.client.service.SessionService;
import com.dyw.client.tool.Tool;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Egci {
    private static Logger logger = LoggerFactory.getLogger(Egci.class);
    public static ConfigEntity configEntity;
    public static SqlSession session;
    public static int workStatus;
    public static AccountEntity accountEntity;
    private static LoginForm loginForm;
    public static Map<String, String> fdLibMaps = new HashMap<>();//布控人脸库
    public static String fdLibIDForStranger;//给陌生人员用的ID
    public static String fdLibIDForStaff;//给电厂人员用的ID
    public static String fdLibIDForBlack;//给黑名单人员用的ID
    public static List<String> snapDeviceIpsOne = new ArrayList<>();//一核抓拍设备
    public static List<String> snapDeviceIpsTwo = new ArrayList<>();//二核抓拍设备
    public static List<String> snapDeviceIpsThree = new ArrayList<>();//三核抓拍设备
    public static List<String> snapDeviceIps = new ArrayList<>();//根据权限设置抓拍设备ip组
    public static int faceServerStatus = 0;//脸谱服务器状态：0禁用  1启用

    /*
     * 初始化客户端程序
     * */
    private static void initClient() {
        accountEntity = new AccountEntity();
        //初始化系统状态
        workStatus = 0;
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
