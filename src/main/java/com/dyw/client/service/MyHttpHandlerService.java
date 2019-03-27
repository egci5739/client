package com.dyw.client.service;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.protection.AlarmResultEntity;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyHttpHandlerService implements HttpHandler {
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
            List<AlarmResultEntity> alarmResultEntityList = new ArrayList<>();
            try {
                alarmResultEntityList = JSONObject.parseArray(new org.json.JSONObject(strout).getString("alarmResult"), AlarmResultEntity.class);
                System.out.println("这里是看点：" + alarmResultEntityList.size() + alarmResultEntityList.get(0).getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getReserve_field().getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
