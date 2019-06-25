package com.dyw.client.functionForm;

import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.form.MonitorRealTimeForm;
import com.dyw.client.tool.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoteFunction {
    private JPanel noteFunction;
    private JTextArea noteArea;
    private JButton confirmButton;
    private JFrame frame;
    private MonitorRealTimeForm monitorRealTimeForm;
    private PassRecordEntity passRecordEntity;

    public NoteFunction(MonitorRealTimeForm monitorRealTimeForm, PassRecordEntity passRecordEntity) {
        this.monitorRealTimeForm = monitorRealTimeForm;
        this.passRecordEntity = passRecordEntity;
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (noteArea.getText().equals("")) {
                    Tool.showMessage("不可为空", "提示", 0);
                    return;
                }
                passRecordEntity.setPassRecordNote(noteArea.getText());
                monitorRealTimeForm.saveOtherEvent(passRecordEntity);
                frame.dispose();
            }
        });
    }


    public void init() {
        frame = new JFrame("事件说明");
        frame.setContentPane(this.noteFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
