package com.dyw.client.tool;

import ISAPI.HttpsClientUtil;
import ISAPI.JsonFormatTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.entity.ConfigTableEntity;
import com.dyw.client.entity.PassRecordEntity;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.CandidateEntity;
//import com.dyw.client.form.ProtectionForm;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Tool {
    private static Logger logger = LoggerFactory.getLogger(Tool.class);
    private static String eventName;
    private static ISAPI.JsonFormatTool JsonFormatTool = new JsonFormatTool();

    /*
     * 读取本地配置文件
     * */
    public static ConfigEntity getConfig(List<ConfigTableEntity> configTableEntityList) {
        ConfigEntity configEntity = new ConfigEntity();
        //创建一个DocumentBuilder的对象
        for (ConfigTableEntity configTableEntity : configTableEntityList) {
            //获取了属性名
            String attrName = configTableEntity.getConfigName();
            if (attrName.equals("devicePort")) {
                configEntity.setDevicePort(Short.parseShort(configTableEntity.getConfigValue()));
            }
            if (attrName.equals("deviceName")) {
                configEntity.setDeviceName(configTableEntity.getConfigValue());
            }
            if (attrName.equals("devicePass")) {
                configEntity.setDevicePass(configTableEntity.getConfigValue());
            }
            if (attrName.equals("socketRegisterPort")) {
                configEntity.setSocketRegisterPort(Short.parseShort(configTableEntity.getConfigValue()));
            }
            if (attrName.equals("socketMonitorPort")) {
                configEntity.setSocketMonitorPort(Short.parseShort(configTableEntity.getConfigValue()));
            }
            if (attrName.equals("ntpServerIp")) {
                configEntity.setNtpServerIp(configTableEntity.getConfigValue());
            }
            if (attrName.equals("faceServerIp")) {
                configEntity.setFaceServerIp(configTableEntity.getConfigValue());
            }
            if (attrName.equals("faceServerPort")) {
                configEntity.setFaceServerPort(Short.parseShort(configTableEntity.getConfigValue()));
            }
            if (attrName.equals("nvrServerIp")) {
                configEntity.setNvrServerIp(configTableEntity.getConfigValue());
            }
            if (attrName.equals("nvrServerPort")) {
                configEntity.setNvrServerPort(Short.parseShort(configTableEntity.getConfigValue()));
            }
        }
        return configEntity;
    }

    //求两个字符串数组的并集，利用set的元素唯一性
    public static String[] union(String[] arr1, String[] arr2) {
        Set<String> set = new HashSet<String>();
        for (String str : arr1) {
            set.add(str);
        }
        for (String str : arr2) {
            set.add(str);
        }
        String[] result = {};
        return set.toArray(result);
    }

    /*
     * 获取缩放后的图片
     * */
    public static ImageIcon getImageScale(ImageIcon imageIcon, float width, float height, float panel, int type) {
        //宽度限制
        if (type == 1) {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) panel / 3 * 2, (int) (height / width * panel / 3 * 2), Image.SCALE_DEFAULT));
        }
        //高度限制
        if (type == 2) {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) (width / height * panel), (int) panel, Image.SCALE_DEFAULT));
        }
        //高、宽等同
        if (type == 3) {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance((int) (height / width * panel), (int) panel, Image.SCALE_DEFAULT));
        }
        return imageIcon;
    }

    /*
     * 拼接搜索条件
     * */
    public static String getSearchCondition(String raw) {
        return "%" + raw + "%";
    }

    /*
     * 显示通行成功结果
     * */
    public static String displayPassSuccessResult(PassRecordEntity passInfoEntity) {
        return "<html><body>卡号：" +
                passInfoEntity.getPassRecordCardNumber() +
                "<br>姓名：" +
                passInfoEntity.getPassRecordName() +
                "<br>时间：" +
                passInfoEntity.getPassRecordPassTime().toString().substring(0, 19) +
                "<br>设备：" +
                passInfoEntity.getPassRecordEquipmentName() +
                "</body></html>";
    }

    /*
     * 显示通行失败结果
     * */
    public static String displayPassFaultResult(PassRecordEntity passInfoEntity) {
        return "<html><body>卡号：" +
                passInfoEntity.getPassRecordCardNumber() +
                "<br>姓名：" +
                passInfoEntity.getPassRecordName() +
                "<br>时间：" +
                passInfoEntity.getPassRecordPassTime().toString().substring(0, 19) +
                "<br>设备：" +
                passInfoEntity.getPassRecordEquipmentName() +
                "<br>原因：" +
                Tool.eventIdToEventName(passInfoEntity.getPassRecordEventTypeId()) +
                "</body></html>";
    }

    /*
     * 显示布控报警对比信息
     * */
    public static String displayAlarmResult(String time, String deviceName, CandidateEntity candidateEntity, Map<String, String> fdLibMaps) {
        return "<html><body>时间：" +
                time +
                "<br>姓名:" +
                candidateEntity.getReserve_field().getName() +
                "<br>分值:" +
                candidateEntity.getSimilarity() +
//                "<br>名单库：    " +
//                fdLibMaps.get(candidateEntity.getBlacklist_id()) +
                "<br>抓拍机：    " +
                deviceName +
                "</body></html>";
    }

    /*
     * 将事件id转为事件名称
     * */
    public static String eventIdToEventName(int value) {
        switch (value) {
            case 9:
                eventName = "卡号不存在";
                break;
            case 105:
                eventName = "比对通过";
                break;
            case 112:
                eventName = "比对失败";
            default:
                break;
        }
        return eventName;
    }

    /*
     * 根据用户权限查看实时通行记录
     * */
    public static String getAccessPermissionInfo(int value) {
        String result = null;
        switch (value) {
            case 0:
                result = "8#1#1#1";
                break;
            case 1:
                result = "8#1#0#0";
                break;
            case 2:
                result = "8#0#1#0";
                break;
            case 3:
                result = "8#0#0#1";
                break;
            default:
                break;
        }
        return result;
    }

    /*
     * 获取当天日期：2019-3-14 格式
     * */
    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
    }

    /*
     * 获取当前时间
     * */
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    /*
     * 将通行模式权限转为中文
     * */
    public static String equipmentPassModeToName(int value) {
        String name = "";
        switch (value) {
            case 0:
                name = "卡+人脸";
                break;
            case 1:
                name = "人脸";
                break;
            default:
                break;
        }
        return name;
    }

    /*
     * 是否在线
     * */
    public static String isLogin(int value) {
        String name = "";
        switch (value) {
            case 0:
                name = "离线";
                break;
            case 1:
                name = "在线";
                break;
            default:
                break;
        }
        return name;
    }

    /*
     * 将用户权限id转为中文
     * */
    public static String accountPermissionIdToName(int value) {
        String name = "";
        switch (value) {
            case 0:
                name = "全厂";
                break;
            case 1:
                name = "一核";
                break;
            case 2:
                name = "二核";
                break;
            case 3:
                name = "三核";
                break;
            default:
                break;
        }
        return name;
    }

    /*
     *发送http指令和接收状态
     * */
    public static JSONObject sendInstructionAndReceiveStatus(int operation, String instruction, JSONObject inboundData) {
        String out = "";
        try {
            switch (operation) {
                case 1:
                    try {
                        out = HttpsClientUtil.httpsGet("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction);
                    } catch (Exception e) {
                        logger.error("执行GET指令出错", e);
                    }
                    break;
                case 2:
                    try {
                        out = HttpsClientUtil.httpsPut("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction, inboundData.toString());
                    } catch (Exception e) {
                        logger.error("执行PUT指令出错", e);
                    }
                    break;
                case 3:
                    try {
                        out = HttpsClientUtil.httpsPost("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction, inboundData.toString());
                    } catch (Exception e) {
                        logger.error("执行POST指令出错", e);
                    }
                    break;
                case 4:
                    try {
                        out = HttpsClientUtil.httpsDelete("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction);
                    } catch (Exception e) {
                        logger.error("执行DELETE指令出错", e);
                    }
                    break;
            }
            logger.info(out);
            return new JSONObject(out);
        } catch (JSONException e) {
            logger.error("发送http指令和接收状态出错", e);
            return null;
        }
    }

    /*
     *发送http指令、接收状态和数据
     * 1:GET;2:PUT;3:POST;4:DELETE
     * */
    public static JSONObject sendInstructionAndReceiveStatusAndData(int operation, String instruction, JSONObject inboundData) {
        String out = "";
        try {
            switch (operation) {
                case 1:
                    try {
                        out = HttpsClientUtil.httpsGet("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction);
                    } catch (Exception e) {
                        logger.error("执行GET指令出错", e);
                    }
                    break;
                case 2:
                    try {
                        out = HttpsClientUtil.httpsPut("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction, inboundData.toString());
                    } catch (Exception e) {
                        logger.error("执行PUT指令出错", e);
                    }
                    break;
                case 3:
                    try {
                        out = HttpsClientUtil.httpsPost("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction, inboundData.toString());
                    } catch (Exception e) {
                        logger.error("执行POST指令出错", e);
                    }
                    break;
                case 4:
                    try {
                        out = HttpsClientUtil.httpsDelete("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + instruction);
                    } catch (Exception e) {
                        logger.error("执行DELETE指令出错", e);
                    }
                    break;
            }
            logger.info(out);
            return new JSONObject(out);
        } catch (JSONException e) {
            logger.error("发送http指令、接收状态和数据", e);
            return null;
        }
    }

    /*
     *  人员信息操作函数
     *  1:新增人脸图片
     *  2：删除人员信息
     *  3.修改人员信息
     * */
    public static JSONObject faceInfoOperation(int operation, String FDID, byte[] bytePic, JSONObject deleteInboundData) {
        try {
            switch (operation) {
                case 1:
                    JSONObject jsonStorageCloud = new JSONObject();
                    jsonStorageCloud.put("FDID", FDID);
                    jsonStorageCloud.put("storageType", "dynamic");
                    String strPic = new String(bytePic, "ISO-8859-1");
                    return new JSONObject(HttpsClientUtil.doPostStorageCloud("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + "/ISAPI/Intelligent/uploadStorageCloud?format=json", jsonStorageCloud.toString(), strPic, "---------------------------------7e13971310878"));
                case 2:
                    return new JSONObject(HttpsClientUtil.doPutWithType("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + "/ISAPI/Intelligent/FDLib/FDSearch/Delete?format=json&FDID=" + FDID + "&faceLibType=blackFD", deleteInboundData.toString(), null, "application/x-www-form-urlencoded; charset=UTF-8"));
                default:
                    return null;
            }
        } catch (Exception e) {
            logger.error("人员信息操作出错", e);
            return null;
        }

    }

    /*
     * json字符串转对象数组
     * */
    public static JSONArray JSONStringToJSONArray(String info, String key) {
        Map maps = (Map) JSON.parse(info);
        return JSON.parseArray(String.valueOf(maps.get(key)));
    }

    /*
     * 提示框
     * */
    public static void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /*
     * 确认提示框
     * */
    public static Boolean showConfirm(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, 0) == 0;
    }

    /*
     * 添加双引号转为json字符串
     * */
    public static String convertToJSON(String jsonStr) {
        jsonStr = jsonStr.replace("{", "{\"");
        jsonStr = jsonStr.replace(":", "\":\"");
        jsonStr = jsonStr.replace(",", "\",\"");
        jsonStr = jsonStr.replace("}", "\"}");
        return jsonStr;
    }

    /*
     * 将数据库中的男女对应成male/female
     * */
    public static String changeGenderToMaleAndFemale(String sex) {
        try {
            switch (Integer.parseInt(sex.trim())) {
                case 1:
                    return "male";
                case 2:
                    return "female";
                default:
                    return "unknown";
            }
        } catch (Exception e) {
            return "unknown";
        }
    }

    /*
     * 分割人员名称字段
     * @return StaffEntity
     * */
    public static StaffEntity splitNameAndGetStaff(String nameInfo) {
        StaffEntity staffEntity = new StaffEntity();
        String[] staffInfo = nameInfo.split("_");
        staffEntity.setStaffName(staffInfo[0]);
        staffEntity.setStaffCardNumber(staffInfo[1]);
        staffEntity.setStaffId(Integer.parseInt(staffInfo[2]));
        return staffEntity;
    }

    /*
     * 拼接rtsp视频流地址
     * */
    public static String getRTSPAddress(String var) {
        StringBuilder stringBuilder = new StringBuilder(var);
        stringBuilder.insert(7, "admin:hik12345@");
        return stringBuilder.toString();
    }

    /**
     * 得到文件流
     *
     * @param url
     * @return
     */
    public static byte[] getURLStream(String url) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            logger.error("获取文件流出错", e);
            return null;
        }
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /*
     * 本地图片转为byte数组
     * */
    public static byte[] getPictureStream(String filePath) {
        try {
            InputStream in = null;
            in = new FileInputStream(filePath);
            byte[] data = toByteArray(in);
            in.close();
            return data;
        } catch (IOException e) {
            logger.error("本地图片转为byte数组出错", e);
            return null;
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /*
     * 判断生日格式是否满足要求
     * */
    public static String judgeBirthdayFormat(String birthday) {
        if (birthday.length() == 10) {
            return birthday;
        } else {
            return "1900-01-01";
        }
    }

    /*
     * 更改时间格式为ISO8601
     * 2019-04-10 12:24:17
     *
     * 1970-01-01T08:00:00Z
     * */
    public static String changeTimeToISO8601(String string) {
        try {
            StringBuffer buffer = new StringBuffer(string);
            buffer.replace(10, 11, "T");
            buffer.append("Z");
            return buffer.toString();
        } catch (Exception e) {
            return "2030-12-31T23:59:59Z";
        }
    }
}