package com.dyw.client.form;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.functionForm.AccountFunction;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class AccountManagementForm extends BaseFormService {
    private JFrame frame;
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
        /*
         * 删除用户
         * */
        accountDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
    }

    /**
     * 删除用户信息
     */
    private void deleteAccount() {
        if (accountManagementContentTable.getSelectedRow() < 0) {
            Tool.showMessage("请先选择一个账户", "提示", 1);
            return;
        }
        if (Tool.showConfirm("确认删除?", "提示")) {
            Egci.session.delete("mapping.accountMapper.deleteAccount", accountEntityList.get(accountManagementContentTable.getSelectedRow()));
            Egci.session.commit();
            showAccountInfo();
        }
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

    @Override
    public JPanel getPanel() {
        return accountManagementForm;
    }
}
