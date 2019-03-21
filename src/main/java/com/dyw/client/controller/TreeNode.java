package com.dyw.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.tool.Tool;

import java.util.*;

public class TreeNode {
    public static void main(String[] args) {
        String info = "{\n" +
                "   \"errorCode\":1,\n" +
                "   \"errorMsg\":\"OK.\",\n" +
                "   \"statusCode\":1,\n" +
                "   \"subStatusCode\":\"ok\",\n" +
                "   \"statusString\":\"OK\",\n" +
                "   \"FDLib\":\n" +
                "   [\n" +
                "      {\n" +
                "         \"FDID\":\"1\",\n" +
                "         \"faceLibType\":\"blackFD\",\n" +
                "         \"name\":\"人脸1\",\n" +
                "         \"customInfo\":\"\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"FDID\":\"2\",\n" +
                "         \"faceLibType\":\"blackFD\",\n" +
                "         \"name\":\"测试名单\",\n" +
                "         \"customInfo\":\"\"\n" +
                "      }\n" +
                "\n" +
                "   ]\n" +
                "\n" +
                "}";
        List<FDLibEntity> libEntityList = JSONObject.parseArray(Tool.JSONStringToJSONArray(info, "FDLib").toJSONString(), FDLibEntity.class);
        System.out.println(libEntityList.get(0).getName());
    }
}
