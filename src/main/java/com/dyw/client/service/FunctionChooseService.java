package com.dyw.client.service;

import com.dyw.client.controller.Egci;
import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.FunctionEntity;
import com.dyw.client.functionForm.AccountFunction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionChooseService {
    private AccountFunction accountFunction;
    private AccountEntity accountEntity;
    private List<JCheckBox> jCheckBoxList = new ArrayList<>();
    private Map<String, Integer> map = new HashMap<>();
    private JFrame jf;

    public FunctionChooseService(AccountFunction accountFunction, AccountEntity accountEntity) {
        this.accountFunction = accountFunction;
        this.accountEntity = accountEntity;
    }

    public void init() {
        jf = new JFrame("测试窗口");
        jf.setSize(250, 250);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        //获取功能选项组
        List<FunctionEntity> functionEntityList = Egci.session.selectList("mapping.configMapper.getFunction");
        for (FunctionEntity functionEntity : functionEntityList) {
            JCheckBox jCheckBox = new JCheckBox(functionEntity.getFunctionName());
            panel.add(jCheckBox);
            jCheckBoxList.add(jCheckBox);
            map.put(functionEntity.getFunctionName(), functionEntity.getFunctionSort());
        }
        JButton jButton = new JButton("确定");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFunction();
            }
        });
        panel.add(jButton);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setContentPane(panel);
        jf.setVisible(true);
    }

    /*
     * 获取功能函数
     * */
    private void getFunction() {
        String functionInfo = "";
        for (JCheckBox jCheckBox : jCheckBoxList) {
            if (jCheckBox.isSelected()) {
                functionInfo += map.get(jCheckBox.getText()) + ",";
            }
        }
        try {
            accountEntity.setAccountFunction(functionInfo.substring(0, functionInfo.length() - 1));
        } catch (StringIndexOutOfBoundsException ignored) {
        }
        accountFunction.refreshFunction();
        jf.dispose();
    }
}
