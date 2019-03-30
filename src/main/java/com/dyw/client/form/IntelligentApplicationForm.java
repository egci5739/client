package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.entity.protection.*;
import com.dyw.client.service.AlarmTableCellRenderer;
import com.dyw.client.service.MyHttpHandlerService;
import com.dyw.client.service.SnapAlarmTableCellRenderer;
import com.dyw.client.tool.Tool;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import net.iharder.Base64;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class IntelligentApplicationForm {
    public JPanel getIntelligentApplicationForm() {
        return intelligentApplicationForm;
    }

    private JPanel intelligentApplicationForm;
    private JPanel intelligentApplicationPanel;
    private JPanel livePreviewPanel;
    private JPanel livePreviewTitlePanel;
    private JLabel livePreviewTitleLabel;
    private JPanel livePreviewContentPanel;
    private JPanel livePreviewContentOnePanel;
    private JPanel livePreviewContentTwoPanel;
    private JPanel livePreviewContentThreePanel;
    private JPanel livePreviewContentFourPanel;
    private JPanel livePreviewContentFivePanel;
    private JPanel livePreviewContentSixPanel;
    private JPanel livePreviewContentSevenPanel;
    private JPanel livePreviewContentEightPanel;
    private JPanel blackAlarmPanel;
    private JPanel blackAlarmTitlePanel;
    private JLabel blackAlarmTitleLabel;
    private JCheckBox blackAlarmRollingCheckBOx;
    private JButton blackAlarmClearButton;
    private JPanel blackAlarmContentPanel;
    private JScrollPane blackAlarmContentScroll;
    private JTable blackAlarmContentTable;
    private JPanel whiteAlarmPanel;
    private JPanel whiteAlarmTitlePanel;
    private JLabel whiteAlarmTitleLabel;
    private JCheckBox whiteAlarmRollingCheckBOx;
    private JButton whiteAlarmClearButton;
    private JPanel whiteAlarmContentPanel;
    private JScrollPane whiteAlarmContentScroll;
    private JTable whiteAlarmContentTable;
    private JPanel snapAlarmPanel;
    private JPanel snapAlarmTitlePanel;
    private JLabel snapAlarmTitleLabel;
    private JCheckBox snapAlarmRollingCheckBOx;
    private JButton snapAlarmClearButton;
    private JPanel snapAlarmContentPanel;
    private JScrollPane snapAlarmContentScroll;
    private JTable snapAlarmContentTable;

    private DefaultTableModel snapAlarmContentTableModel;
    private DefaultTableModel blackAlarmContentTableModel;
    private DefaultTableModel whiteAlarmContentTableModel;
    private JScrollBar snapAlarmScrollBar;
    private JScrollBar blackAlarmScrollBar;
    private JScrollBar whiteAlarmScrollBar;
    private int snapAlarmRollingStatus = 1;
    private int blackAlarmRollingStatus = 1;
    private int whiteAlarmRollingStatus = 1;
    private int snapAlarmBottomStatus = 0;
    private int blackAlarmBottomStatus = 0;
    private int whiteAlarmBottomStatus = 0;
    private List<MonitorPointEntity> monitorPointEntityList;//监控点列表
    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
    private List<RegionEntity> regionEntityList;//区域列表
    public HttpServer httpserver = null;
    private List<JPanel> livePreviewContentPanelList;
    public Map<String, String> fdLibMaps;//人脸库集合

    public IntelligentApplicationForm() {
        //抓拍图
        snapAlarmContentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnSnapAlarmInfo = {"抓拍图", "时间"};
        snapAlarmContentTableModel.setColumnIdentifiers(columnSnapAlarmInfo);
        snapAlarmContentTable.setModel(snapAlarmContentTableModel);
        TableCellRenderer snapAlarmCellRenderer = new SnapAlarmTableCellRenderer();
        snapAlarmContentTable.setDefaultRenderer(Object.class, snapAlarmCellRenderer);
        snapAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && snapAlarmBottomStatus <= 3) {
                    snapAlarmContentScroll.getVerticalScrollBar().setValue(snapAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - snapAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
                    snapAlarmBottomStatus++;
                }
            }
        });
        snapAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        snapAlarmScrollBar = snapAlarmContentScroll.getVerticalScrollBar();
        snapAlarmRollingCheckBOx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    snapAlarmRollingStatus = 1;
                } else {
                    snapAlarmRollingStatus = 0;
                }
            }
        });
        snapAlarmClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snapAlarmContentTableModel.setRowCount(0);
            }
        });
        //黑名单
        blackAlarmContentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnBlackAlarmInfo = {"抓拍图", "底图", "报警信息"};
        blackAlarmContentTableModel.setColumnIdentifiers(columnBlackAlarmInfo);
        blackAlarmContentTable.setModel(blackAlarmContentTableModel);
        TableCellRenderer blackAlarmCellRenderer = new AlarmTableCellRenderer();
        blackAlarmContentTable.setDefaultRenderer(Object.class, blackAlarmCellRenderer);
        blackAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && blackAlarmBottomStatus <= 3) {
                    blackAlarmContentScroll.getVerticalScrollBar().setValue(blackAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - blackAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
                    blackAlarmBottomStatus++;
                }
            }
        });
        blackAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        blackAlarmScrollBar = blackAlarmContentScroll.getVerticalScrollBar();
        blackAlarmRollingCheckBOx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    blackAlarmRollingStatus = 1;
                } else {
                    blackAlarmRollingStatus = 0;
                }
            }
        });
        blackAlarmClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackAlarmContentTableModel.setRowCount(0);
            }
        });
        //白名单
        whiteAlarmContentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnWhiteAlarmInfo = {"抓拍图", "底图", "报警信息"};
        whiteAlarmContentTableModel.setColumnIdentifiers(columnWhiteAlarmInfo);
        whiteAlarmContentTable.setModel(whiteAlarmContentTableModel);
        TableCellRenderer whiteAlarmCellRenderer = new AlarmTableCellRenderer();
        whiteAlarmContentTable.setDefaultRenderer(Object.class, whiteAlarmCellRenderer);
        whiteAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && whiteAlarmBottomStatus <= 3) {
                    whiteAlarmContentScroll.getVerticalScrollBar().setValue(whiteAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - whiteAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
                    whiteAlarmBottomStatus++;
                }
            }
        });
        whiteAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
        whiteAlarmScrollBar = whiteAlarmContentScroll.getVerticalScrollBar();
        whiteAlarmRollingCheckBOx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    whiteAlarmRollingStatus = 1;
                } else {
                    whiteAlarmRollingStatus = 0;
                }
            }
        });
        whiteAlarmClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whiteAlarmContentTableModel.setRowCount(0);
            }
        });
        /*
         * 添加报警主机信息
         * */
        addAlarmHost();
        /*
         * 监听报警消息
         * */
        monitorAlarmInfo();
        try {
            //获取根控制中心
            ctrlCenterEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter?source=device", null).getString("ctrlCenter"), CtrlCenterEntity.class).get(0);
            //获取区域列表
            regionEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID(), null).getString("region"), RegionEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        livePreviewContentPanelList = new ArrayList<>();
        livePreviewContentPanelList.add(livePreviewContentOnePanel);
        livePreviewContentPanelList.add(livePreviewContentTwoPanel);
        livePreviewContentPanelList.add(livePreviewContentThreePanel);
        livePreviewContentPanelList.add(livePreviewContentFourPanel);
        livePreviewContentPanelList.add(livePreviewContentFivePanel);
        livePreviewContentPanelList.add(livePreviewContentSixPanel);
        livePreviewContentPanelList.add(livePreviewContentSevenPanel);
        livePreviewContentPanelList.add(livePreviewContentEightPanel);
        monitorPointEntityList = new ArrayList<>();
    }

    /*
     * 添加报警主机信息
     * */
    private void addAlarmHost() {
        try {
            //第一步：获取全部报警主机信息，判断是否已存在
            String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":12346/alarm";
//            System.out.println(url);
            String instructionGet = "/ISAPI/Event/notification/httpHosts?format=json";
            List<HttpHostNotificationEntity> httpHostNotificationEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, instructionGet, null).getString("HttpHostNotification"), HttpHostNotificationEntity.class);
            for (HttpHostNotificationEntity httpHostNotificationEntity : httpHostNotificationEntityList) {
                if (httpHostNotificationEntity.getUrl().equals(url)) {
                    return;
                }
            }
            //新增报警主机信息
            String instruction = "/ISAPI/Event/notification/httpHosts?format=json";
            org.json.JSONObject inboundDataIn = new org.json.JSONObject();
            org.json.JSONObject inboundDataOut = new org.json.JSONObject();
            inboundDataIn.put("url", url);
            inboundDataIn.put("protocolType", "HTTP");//这里要注意
            inboundDataIn.put("parameterFormatType", "json");
            inboundDataIn.put("addressingFormatType", "ipaddress");
            inboundDataIn.put("httpAuthenticationMethod", "none");//这里要注意
            inboundDataIn.put("eventType", "alarmResult,captureResult,HFPD");
            inboundDataOut.put("HttpHostNotification", inboundDataIn);
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataOut);
            if (resultData.getInt("statusCode") == 1) {
                Tool.showMessage("添加报警主机成功", "提示", 0);
            } else {
                Tool.showMessage("添加报警主机失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
            }
        } catch (JSONException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /*
     * 监听报警消息
     * */
    public void monitorAlarmInfo() {
        HttpServerProvider provider = HttpServerProvider.provider();
        try {
            httpserver = provider.createHttpServer(new InetSocketAddress(12346), 100);
            httpserver.createContext("/alarm", new MyHttpHandlerService(this));
            httpserver.setExecutor(null);
            httpserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 显示报警信息
     * status  0：抓拍图；1：名单报警；
     * */
    public void showAlarmInfo(int status, CaptureLibResultEntity captureLibResultEntity, AlarmResultEntity alarmResultEntity) {
        switch (status) {
            case 0:
                Vector vectorOne = new Vector();
                vectorOne.add(0, Base64.encodeBytes(Tool.getURLStream(captureLibResultEntity.getImage())));
                vectorOne.add(1, captureLibResultEntity.getTargetAttrs().getFaceTime() + "\n" + captureLibResultEntity.getTargetAttrs().getDeviceName());
                snapAlarmContentTableModel.addRow(vectorOne);
                if (snapAlarmRollingStatus == 1) {
                    moveScrollBarToBottom(snapAlarmScrollBar);
                }
                snapAlarmBottomStatus = 0;
                break;
            case 1:
                Vector vectorTwo = new Vector();
                vectorTwo.add(0, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getImage())));
                vectorTwo.add(1, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getHuman_data().get(0).getFace_picurl())));
                vectorTwo.add(2, Tool.displayAlarmResult(alarmResultEntity.getTargetAttrs().getFaceTime(), alarmResultEntity.getTargetAttrs().getDeviceName(), alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0), fdLibMaps));
                blackAlarmContentTableModel.addRow(vectorTwo);
                if (blackAlarmRollingStatus == 1) {
                    moveScrollBarToBottom(blackAlarmScrollBar);
                }
                blackAlarmBottomStatus = 0;
                break;
            default:
                break;
        }
    }

    /*
     * 将滚动条移到底部
     * */
    private void moveScrollBarToBottom(JScrollBar jScrollBar) {
        if (jScrollBar != null) {
            jScrollBar.setValue(jScrollBar.getMaximum());
        }
    }
}
