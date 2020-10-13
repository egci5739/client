package com.dyw.client.controller;

import javax.swing.*;

public class Test {
    public static void main(String[] args) {
        TimeSeriesChart timeSeriesChart = new TimeSeriesChart();
        JFrame frame = new JFrame("Login Example");
        // Setting the width and height of frame
        frame.setSize(350, 200);
        frame.add(timeSeriesChart.getChartPanel());
        frame.setVisible(true);
    }
}
