package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public LoginForm() {
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
        //判断登陆的客户端：0：系统管理；1：办证客户端；2：监控客户端
        switch (Egci.accountEntity.getAccountRole()) {
            case 0:
                //创建系统客户端
                Egci.systemForm = new SystemForm();
                Egci.systemForm.init();
                frame.setVisible(false);
                break;
            case 1:
                //创建办证客户端
                Egci.registerForm = new RegisterForm();
                Egci.registerForm.init();
                frame.setVisible(false);
                break;
            case 2:
                //创建监控客户端
                Egci.monitorForm = new MonitorForm();
                Egci.monitorForm.init();
                frame.setVisible(false);
                break;
            default:
                break;
        }
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //点击回车键登陆
        frame.getRootPane().setDefaultButton(loginButton);
        frame.setVisible(true);
    }
}