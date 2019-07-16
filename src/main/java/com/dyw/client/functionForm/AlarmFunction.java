package com.dyw.client.functionForm;

import com.dyw.client.entity.AlarmEntity;
import com.dyw.client.form.guard.AlarmForm;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlarmFunction {
    private JPanel alarmFunction;
    private JTextArea alarmArea;
    private JButton confirmButton;
    private JFrame frame;

    public AlarmFunction(AlarmForm alarmForm, AlarmEntity alarmEntity) {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alarmArea.getText().equals("")) {
                    Tool.showMessage("不可为空", "提示", 0);
                    return;
                }
                alarmEntity.setAlarmNote(alarmArea.getText());
                alarmForm.saveOtherEvent(alarmEntity);
                frame.dispose();
            }
        });
    }

    public void init() {
        frame = new JFrame("报警说明");
        frame.setContentPane(this.alarmFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
