package com.dyw.client.service;


import java.sql.DriverManager;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {
    private Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    //连接数据库
    private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String dbURL;
    private String dataBaseName;
    private String dataBasePass;
    private Statement statement = null;

    public DatabaseService(String dataBaseIp, short dataBasePort, String dataBaseName, String dataBasePass, String dataBaseLib) {
        dbURL = "jdbc:sqlserver://" + dataBaseIp + ":" + dataBasePort + ";DatabaseName=" + dataBaseLib;
        this.dataBaseName = dataBaseName;
        this.dataBasePass = dataBasePass;
    }

    public Statement connection() {
        try {
            Class.forName(driverName);
            statement = DriverManager.getConnection(dbURL, dataBaseName, dataBasePass).createStatement();
            logger.info("连接数据库成功");
            return statement;
        } catch (Exception e) {
            logger.error("连接数据库失败：" + e);
            return statement;
        }
    }
}
