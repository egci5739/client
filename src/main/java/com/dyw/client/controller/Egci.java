package com.dyw.client.controller;

import com.dyw.client.HCNetSDK;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.form.*;
import com.dyw.client.service.SessionService;
import com.dyw.client.tool.Tool;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Egci {
    private static Logger logger = LoggerFactory.getLogger(Egci.class);
    public static Map<String, String> functions = new HashMap<>();
    public static ConfigEntity configEntity;
    public static SqlSession session;
    public static int workStatus;
    public static AccountEntity accountEntity = new AccountEntity();
    private static LoginForm loginForm;
    public static Map<String, String> fdLibMaps = new HashMap<>();//布控人脸库
    public static String fdLibIDForStranger;//给陌生人员用的ID
    public static String fdLibIDForStaff;//给电厂人员用的ID
    public static String fdLibIDForBlack;//给黑名单人员用的ID
    public static String fdLibIDForVideo;//给历史视频回放用的ID
    public static List<String> snapDeviceIpsOne = new ArrayList<>();//一核抓拍设备
    public static List<String> snapDeviceIpsTwo = new ArrayList<>();//二核抓拍设备
    public static List<String> snapDeviceIpsThree = new ArrayList<>();//三核抓拍设备
    public static List<String> snapDeviceIps = new ArrayList<>();//根据权限设置抓拍设备ip组
    public static int faceServerStatus = 0;//脸谱服务器状态：0禁用  1启用
    //初始化静态对象
    public static HCNetSDK hcNetSDK;
    public static Map<String, Integer> cameraMap = new HashMap<>();//抓拍机信息

    /*
     * 初始化客户端程序
     * */
    private static void initClient() {
        //初始化系统状态
        workStatus = 0;
        //初始化SDK静态对象
        try {
            hcNetSDK = HCNetSDK.INSTANCE;
        } catch (Exception e) {
            logger.error("初始化SDK静态对象，失败", e);
        }
        //初始化SDK
        if (!hcNetSDK.NET_DVR_Init()) {
            logger.error("SDK初始化失败");
            return;
        }
        //初始化session对象
        try {
            SessionService sessionService = new SessionService();
            session = sessionService.createSession();
        } catch (Exception e) {
            logger.error("创建session对象失败", e);
        }
        //读取配置文件
        try {
            configEntity = Tool.getConfig(session.selectList("mapping.configMapper.getConfig"));
        } catch (Exception e) {
            logger.error("读取配置信息出错", e);
            Tool.showMessage(e.getMessage(), "连接数据库出错", 0);
            return;
        }
        //初始化抓拍机map
        try {
            List<EquipmentEntity> cameraList = session.selectList("mapping.equipmentMapper.getALlCamera");
            for (EquipmentEntity equipmentEntity : cameraList) {
                cameraMap.put(equipmentEntity.getEquipmentName(), equipmentEntity.getEquipmentChannel());
            }
        } catch (Exception e) {
            logger.error("初始化抓拍机map出错", e);
        }
        /*
         * 查看系统资源状态
         * */
        logger.info("Runtime max: " + mb(Runtime.getRuntime().maxMemory()));
        MemoryMXBean m = ManagementFactory.getMemoryMXBean();

        logger.info("Non-heap: " + mb(m.getNonHeapMemoryUsage().getMax()));
        logger.info("Heap: " + mb(m.getHeapMemoryUsage().getMax()));

        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {
            logger.info("Pool: " + mp.getName() + " (type " + mp.getType() + ")" + " = " + mb(mp.getUsage().getMax()));
        }
        //创建登陆客户端
        loginForm = new LoginForm();
        loginForm.init();
    }

    /*
     * 主函数
     * */
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        Egci.initClient();
    }

    static String mb(long s) {
        return String.format("%d (%.2f M)", s, (double) s / (1024 * 1024));
    }
}
