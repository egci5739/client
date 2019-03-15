package com.dyw.client.tool;

import com.dyw.client.entity.AccountEntity;
import com.dyw.client.entity.ConfigEntity;
import com.dyw.client.entity.PassInfoEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.Calendar;

public class Tool {
    private static String eventName;

    /*
     * 读取本地配置文件
     * */
    public static ConfigEntity getConfig(String path) {
        ConfigEntity configEntity = new ConfigEntity();
        //创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建一个DocumentBuilder的对象
        try {
            //创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
            Document document = db.parse(path);
            //获取所有book节点的集合
            NodeList bookList = document.getElementsByTagName("config");
            //通过nodelist的getLength()方法可以获取bookList的长度
            //遍历每一个book节点
            //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
            for (int i = 0; i < bookList.getLength(); i++) {
                Node book = bookList.item(i);
                NodeList childNodes = book.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        //获取了属性名
                        String attrName = childNodes.item(j).getNodeName();
                        if (attrName.equals("devicePort")) {
                            configEntity.setDevicePort(Short.parseShort(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("deviceName")) {
                            configEntity.setDeviceName(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("devicePass")) {
                            configEntity.setDevicePass(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("dataBaseIp")) {
                            configEntity.setDataBaseIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("dataBasePort")) {
                            configEntity.setDataBasePort(Short.parseShort(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("dataBaseName")) {
                            configEntity.setDataBaseName(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("dataBasePass")) {
                            configEntity.setDataBasePass(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("dataBaseLib")) {
                            configEntity.setDataBaseLib(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("dataBaseTime")) {
                            configEntity.setDataBaseTime(Long.parseLong(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("queueIp")) {
                            configEntity.setQueueIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("socketPort")) {
                            configEntity.setSocketPort(Short.parseShort(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("testIp")) {
                            configEntity.setTestIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("synchronization")) {
                            configEntity.setSynchronization(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("synchronizationHour")) {
                            configEntity.setSynchronizationHour(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("synchronizationMinute")) {
                            configEntity.setSynchronizationMinute(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("synchronizationSecond")) {
                            configEntity.setSynchronizationSecond(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("synchronizationTime")) {
                            configEntity.setSynchronizationTime(Long.parseLong(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("onGuardIp")) {
                            configEntity.setOnGuardIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("onGuardPort")) {
                            configEntity.setOnGuardPort(Short.parseShort(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("alarmTime")) {
                            configEntity.setAlarmTime(Long.parseLong(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("pushTime")) {
                            configEntity.setPushTime(Long.parseLong(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("callBackTime")) {
                            configEntity.setCallBackTime(Long.parseLong(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeStatus1")) {
                            configEntity.setExitTimeStatus1(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeStatus2")) {
                            configEntity.setExitTimeStatus2(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeHour1")) {
                            configEntity.setExitTimeHour1(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeHour2")) {
                            configEntity.setExitTimeHour2(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeMinute1")) {
                            configEntity.setExitTimeMinute1(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeMinute2")) {
                            configEntity.setExitTimeMinute2(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeSecond1")) {
                            configEntity.setExitTimeSecond1(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("exitTimeSecond2")) {
                            configEntity.setExitTimeSecond2(Integer.parseInt(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("serverIp")) {
                            configEntity.setServerIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                        if (attrName.equals("serverPort")) {
                            configEntity.setServerPort(Short.parseShort(childNodes.item(j).getFirstChild().getNodeValue()));
                        }
                        if (attrName.equals("faceCollectionIp")) {
                            configEntity.setFaceCollectionIp(childNodes.item(j).getFirstChild().getNodeValue());
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configEntity;
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
    public static String displayPassSuccessResult(PassInfoEntity passInfoEntity) {
        return "<html><body>卡号：" +
                passInfoEntity.getCardNumber() +
                "<br>姓名：" +
                passInfoEntity.getStaffName() +
                "<br>时间：" +
                passInfoEntity.getDate() +
                "<br>设备：" +
                passInfoEntity.getEquipmentName() +
                "</body></html>";
    }

    /*
     * 显示通行失败结果
     * */
    public static String displayPassFaultResult(PassInfoEntity passInfoEntity) {
        return "<html><body>卡号：" +
                passInfoEntity.getCardNumber() +
                "<br>姓名：" +
                passInfoEntity.getStaffName() +
                "<br>时间：" +
                passInfoEntity.getDate() +
                "<br>设备：" +
                passInfoEntity.getEquipmentName() +
                "<br>原因：" +
                Tool.eventIdToEventName(passInfoEntity.getEventTypeId()) +
                "</body></html>";
    }

    /*
     * 历史遗留问题
     * 核数和sortid不配对
     * */
    public static int getGroupId(int value) {
        int result = 0;
        if (value == 1)
            result = 2;
        else if (value == 2)
            result = 3;
        else if (value == 3)
            result = 4;
        return result;
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
}
