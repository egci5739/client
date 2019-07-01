package com.dyw.client.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SessionService {
    private Logger logger = LoggerFactory.getLogger(SessionService.class);
    private SqlSession session;

    /*
     * 创建session
     * */
    public SqlSession createSession() {
        try {
            String resource = "config-client.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            //构建sqlSession的工厂
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            //创建能执行映射文件中sql的sqlSession
            session = sessionFactory.openSession();
            if (session == null) {
                logger.error("session对象创建失败");
            } else {
                logger.info("session对象创建成功");
            }
        } catch (Exception e) {
            logger.error("session对象创建出错", e);
        }
        return session;
    }
}
