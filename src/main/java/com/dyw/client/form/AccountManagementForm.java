package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.functionForm.AccountFunction;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class AccountManagementForm {
    public JPanel getAccountManagementForm() {
        return accountManagementForm;
    }

    private JPanel accountManagementForm;
    private JPanel accountManagementPanel;
    private JPanel accountManagementToolBarPanel;
    private JPanel accountManagementContentPanel;
    private JScrollPane accountManagementContentScroll;
    private JTable accountManagementContentTable;
    private JButton accountAddButton;
    private JButton accountDeleteButton;
    private DefaultTableModel accountManagementModel;
    private List<AccountEntity> accountEntityList;

    public AccountManagementForm() {
        //初始化用户管理表格
        String[] columnAccountInfo = {"用户", "密码", "功能", "权限"};
        accountManagementModel = new DefaultTableModel();
        accountManagementModel.setColumnIdentifiers(columnAccountInfo);
        accountManagementContentTable.setModel(accountManagementModel);
        DefaultTableCellRenderer accountTableCellRenderer = new DefaultTableCellRenderer();
        accountTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        accountManagementContentTable.setDefaultRenderer(Object.class, accountTableCellRenderer);
        showAccountInfo();
        /*
         * 新增用户
         * */
        accountAddButton.addActionListener(new ActionListener() {
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
        AccountFunction accountFunction = new AccountFunction(this);
        accountFunction.init();
    }

    /*
     * 加载用户信息
     * */
    public void showAccountInfo() {
        accountManagementModel.setRowCount(0);
        accountEntityList = Egci.session.selectList("mapping.accountMapper.getAllAccount");
        for (AccountEntity accountEntity : accountEntityList) {
            Vector v = new Vector();
            v.add(0, accountEntity.getAccountName());
            v.add(1, accountEntity.getAccountPass());
            v.add(2, accountEntity.getAccountFunction());
            v.add(3, Tool.accountPermissionIdToName(accountEntity.getAccountPermission()));
            accountManagementModel.addRow(v);
        }
    }
}
