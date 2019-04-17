package com.dyw.client.form;

import com.dyw.client.service.ExportExcelService;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportStaffForm {
    private Logger logger = LoggerFactory.getLogger(ExportStaffForm.class);

    public JPanel getExportStaffForm() {
        return exportStaffForm;
    }

    private JPanel exportStaffForm;
    private JLabel exportStaffNumberLabel;
    private JTextField exportStaffNumberText;


    private JButton exportStaffNumberButton;

    public ExportStaffForm() {
        exportStaffNumberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });
    }

    /*
     * 导出人员信息
     * */
    private void export() {
        ExportExcelService exportExcelService = new ExportExcelService();
        try {
            exportExcelService.export(Integer.parseInt(exportStaffNumberText.getText()));
        } catch (Exception e) {
            logger.error("导出人员出错", e);
            Tool.showMessage("请正确填写需要导出的人员数量", "提示", 0);
        }
    }
}
