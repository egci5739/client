package com.dyw.client.functionForm;

import com.dyw.client.entity.DataAnalysisEntity;
import com.dyw.client.service.ChartService;

import javax.swing.*;
import java.util.List;

public class ChartFunction {
    private JPanel chartFunction;
    private JFrame frame;
    private final List<DataAnalysisEntity> dataAnalysisEntityList;

    public ChartFunction(List<DataAnalysisEntity> dataAnalysisEntityList) {
        this.dataAnalysisEntityList = dataAnalysisEntityList;
    }


    public void init() {
        ChartService chartService = new ChartService(dataAnalysisEntityList, "视图", "事件类型", "数量", 1);
        frame = new JFrame("柱状图");
        frame.setContentPane(this.chartFunction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(chartService.getChartPanel());
    }
}
