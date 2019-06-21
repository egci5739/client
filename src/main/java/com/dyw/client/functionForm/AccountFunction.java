package com.dyw.client.functionForm;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.FunctionEntity;
import com.dyw.client.form.AccountManagementForm;
import com.dyw.client.service.FunctionChooseService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountFunction {
    private JPanel accountFunction;
    private JPanel accountContentPanel;
    private JPanel accountToolbarPanel;
    private JTextField accountNameText;
    private JTextField accountPassText;
    private JComboBox accountPermissionCombo;
    private JLabel accountNameLabel;
    private JLabel accountPassLabel;
    private JLabel accountFunctionLabel;
    private JLabel accountPermissionLabel;
    private JButton accountAddConfirmButton;
    private JButton accountAddCancelButton;
    private JButton functionChooseButton;

    private AccountManagementForm accountManagementForm;
    private AccountEntity accountEntity = new AccountEntity();
    private JFrame frame;

    public AccountFunction(AccountManagementForm accountManagementForm) {
        this.accountManagementForm = accountManagementForm;
        /*
         * 初始化权限选择下拉框
         * */
        accountPermissionCombo.addItem("全厂");
        accountPermissionCombo.addItem("一核");
        accountPermissionCombo.addItem("二核");
        accountPermissionCombo.addItem("三核");
        /*
         * 点击功能选择框
         * */
        functionChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFunction();
            }
        });
        /*
         * 新增用户
         * */
        accountAddConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
            }
        });
    }

    /*
     * 新增用户
     * */
    private void addAccount() {
        if (accountNameText.getText().equals("") || accountPassText.getText().equals("")) {
            Tool.showMessage("请检查用户名、密码、功能是否填写完成", "提示", 1);
            return;
        }
        accountEntity.setAccountPermission(accountPermissionCombo.getSelectedIndex());
        accountEntity.setAccountName(accountNameText.getText());
        accountEntity.setAccountPass(accountPassText.getText());
        Egci.session.insert("mapping.accountMapper.addAccount", accountEntity);
        Egci.session.commit();
        frame.dispose();
        accountManagementForm.showAccountInfo();
    }

    /*
     * 功能选择
     * */
    private void chooseFunction() {
        FunctionChooseService functionChooseService = new FunctionChooseService(this, accountEntity);
        functionChooseService.init();
    }

    /*
     * 重载功能显示
     * */
    public void refreshFunction() {
        if (accountEntity.getAccountFunction() == null) {
            return;
        }
        functionChooseButton.setText(accountEntity.getAccountFunction());
    }

    public void init() {
        frame = new JFrame("AccountFunction");
        frame.setContentPane(this.accountFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
