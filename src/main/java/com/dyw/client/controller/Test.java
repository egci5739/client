package com.dyw.client.controller;

import com.dyw.client.tool.Tool;

import java.text.ParseException;

public class Test extends javax.swing.JDialog {
    public static void main(String[] args) {
        Tool.showMessage(String.valueOf(Runtime.getRuntime().maxMemory()), "", 1);
        while (true) ;
    }
}
