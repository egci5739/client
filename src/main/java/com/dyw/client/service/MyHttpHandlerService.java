package com.dyw.client.service;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
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
    private List<AlarmResultEntity> alarmResultEntityList = new ArrayList<>();
    private List<CaptureLibResultEntity> captureLibResultEntityList = new ArrayList<>();

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
            alarmResultEntityList.clear();
            captureLibResultEntityList.clear();
            try {
                org.json.JSONObject resultData = new org.json.JSONObject(strout);
                switch (resultData.getString("eventType")) {
                    case "captureResult":
                        captureLibResultEntityList = JSONObject.parseArray(resultData.getString("captureLibResult"), CaptureLibResultEntity.class);
                        if (Egci.snapDeviceIps.contains(captureLibResultEntityList.get(0).getTargetAttrs().getDeviceIP())) {
                            intelligentApplicationForm.showAlarmInfo(0, captureLibResultEntityList.get(0), null);
                        }
                        break;
                    case "alarmResult":
                        alarmResultEntityList = JSONObject.parseArray(resultData.getString("alarmResult"), AlarmResultEntity.class);
                        if (Egci.snapDeviceIps.contains(alarmResultEntityList.get(0).getTargetAttrs().getDeviceIP())) {
                            intelligentApplicationForm.showAlarmInfo(1, null, alarmResultEntityList.get(0));
                        }
                        break;
                    default:
                        break;
                }
//                if (resultData.getString("eventType").equalsIgnoreCase("alarmResult")) {
//                    alarmResultEntityList = JSONObject.parseArray(resultData.getString("alarmResult"), AlarmResultEntity.class);
//                    intelligentApplicationForm.showAlarmInfo(1, null, alarmResultEntityList.get(0));
//                } else if (resultData.getString("eventType").equalsIgnoreCase("captureResult")) {
//                    captureLibResultEntityList = JSONObject.parseArray(resultData.getString("captureLibResult"), CaptureLibResultEntity.class);
//                    intelligentApplicationForm.showAlarmInfo(0, captureLibResultEntityList.get(0), null);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
