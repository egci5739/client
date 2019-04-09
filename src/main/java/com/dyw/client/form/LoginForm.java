package com.dyw.client.form;

import ISAPI.HttpsClientUtil;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.service.NetStateService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class LoginForm {
    private JFrame frame;
    private JPanel Login;
    private JPanel loginTitlePanel;
    private JPanel loginContentPanel;
    private JLabel LoginTitleLabel;
    private JTextField accountNameText;
    private JTextField accountPassText;
    private JLabel accountNameLabel;
    private JLabel accountPassLabel;
    private JButton loginButton;
    private JButton resetButton;
    private Logger logger;

    public LoginForm() {
        logger = LoggerFactory.getLogger(LoginForm.class);
        //登陆
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        //重置
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
    }

    /*
     * 登陆
     * */
    private void login() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountName(accountNameText.getText());
        accountEntity.setAccountPass(accountPassText.getText());
        if (accountEntity.getAccountName().equals("") || accountEntity.getAccountPass().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入用户名和密码", "登陆提示", 0);
            return;
        }
        Egci.accountEntity = Egci.session.selectOne("mapping.accountMapper.verifyAccount", accountEntity);
        if (Egci.accountEntity == null) {
            JOptionPane.showMessageDialog(null, "用户名或密码错误", "登陆提示", 0);
            return;
        }
        if (accountEntity.getAccountRole() != 2) {
            if (loginFaceServer().equals("error")) {
                JOptionPane.showMessageDialog(null, "登陆脸谱服务器失败", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        ApplicationForm applicationForm = new ApplicationForm();
        applicationForm.init();
        frame.dispose();
    }

    /*
     * 登陆脸谱服务器
     * */
    private String loginFaceServer() {
        String result = "";
        try {
            NetStateService netStateService = new NetStateService();
            if (!netStateService.ping(Egci.configEntity.getFaceServerIp())) {
                //ping不通脸谱服务器
                System.out.println("服务ip:" + Egci.configEntity.getFaceServerIp());
                return "error";
            }
            // 登陆脸谱服务器
            HttpsClientUtil.httpsClientInit(Egci.configEntity.getFaceServerIp(), Egci.configEntity.getFaceServerPort(), "admin", "hik12345");
            //登录校验代码
            String strUrl = "/ISAPI/Security/userCheck";
            String strOut = "";
            strOut = HttpsClientUtil.httpsGet("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + strUrl);
            if (strOut.equals("error")) {
                result = "error";
            } else {
                //解析返回的xml文件
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new ByteArrayInputStream(strOut.getBytes("UTF-8")));
                Element employees = document.getRootElement();
                for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                    Element employee = (Element) i.next();
                    if (employee.getName().equals("statusValue") && 0 == employee.getText().compareTo("200")) {
//                    JOptionPane.showMessageDialog(null, "登陆成功", "Information", JOptionPane.INFORMATION_MESSAGE);
                        result = "success";
                        Egci.faceServerStatus = 1;
                    }
                }
            }
        } catch (Exception e) {
            result = "error";
        }
        return result;
    }

    /*
     * 重置
     * */
    private void reset() {
        accountNameText.setText("");
        accountPassText.setText("");
    }

    /*
     * 初始化登陆页面
     * */
    public void init() {
        frame = new JFrame("LoginForm");
        frame.setContentPane(this.Login);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        //点击回车键登陆
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setDefaultButton(loginButton);
        frame.setVisible(true);
    }
}
