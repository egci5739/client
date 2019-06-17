package com.dyw.client.controller;

import com.dyw.client.tool.Tool;

import java.text.ParseException;

public class Test extends javax.swing.JDialog {
    public static void main(String[] args) {
        String function = "\"功能一\", \"功能二\", \"功能三\"";
        String[] functions = function.split(",");
        System.out.println(functions[1]);

    }
}
