package com.dyw.client.service;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.protection.AlarmResultEntity;
import com.dyw.client.entity.protection.CaptureLibResultEntity;
import com.dyw.client.form.IntelligentApplicationForm;
//import com.dyw.client.form.ProtectionForm;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyHttpHandlerService implements HttpHandler {
    private IntelligentApplicationForm intelligentApplicationForm;

    public MyHttpHandlerService(IntelligentApplicationForm intelligentApplicationForm) {
        this.intelligentApplicationForm = intelligentApplicationForm;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            InputStreamReader ISR = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(ISR);
            String strout = "";
            String temp = "";
            while ((temp = br.readLine()) != null) {
                strout = strout + temp;
            }
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
            List<AlarmResultEntity> alarmResultEntityList;
            List<CaptureLibResultEntity> captureLibResultEntityList;
            try {
                org.json.JSONObject resultData = new org.json.JSONObject(strout);
                if (resultData.getString("eventType").equalsIgnoreCase("alarmResult")) {
                    alarmResultEntityList = JSONObject.parseArray(resultData.getString("alarmResult"), AlarmResultEntity.class);
                    System.out.println("报警数据：" + resultData);
                    intelligentApplicationForm.showAlarmInfo(1, null, alarmResultEntityList.get(0));
                } else if (resultData.getString("eventType").equalsIgnoreCase("captureResult")) {
                    System.out.println("抓拍数据：" + resultData);
                    captureLibResultEntityList = JSONObject.parseArray(resultData.getString("captureLibResult"), CaptureLibResultEntity.class);
                    intelligentApplicationForm.showAlarmInfo(0, captureLibResultEntityList.get(0), null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
