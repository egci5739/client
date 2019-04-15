package com.dyw.client.controller;

import com.dyw.client.tool.Tool;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println(Tool.changeTimeToISO8601("2019-04-10 12:24:17"));
    }
}
