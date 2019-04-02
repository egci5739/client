package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.*;
import com.dyw.client.service.AlarmTableCellRenderer;
import com.dyw.client.service.MyHttpHandlerService;
import com.dyw.client.service.SnapAlarmTableCellRenderer;
import com.dyw.client.tool.Tool;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import net.iharder.Base64;
import org.json.JSONException;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.spec.ECGenParameterSpec;
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
    private JButton playButton;

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
    private List<MonitorPointEntity> monitorPointEntityList = new ArrayList<>();//监控点列表
    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
    private List<RegionEntity> regionEntityList;//区域列表
    private HttpServer httpserver = null;
    private List<JPanel> livePreviewContentPanelList;
    private List<EmbeddedMediaPlayer> embeddedMediaPlayerList = new ArrayList<>();
    private List<EmbeddedMediaPlayerComponent> embeddedMediaPlayerComponentList = new ArrayList<>();
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表

    public IntelligentApplicationForm() {
        /*
         * 获取人脸库列表
         * */
        getFDLib();
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
        /*
         * 获取并设置抓拍机ip信息
         * */
        getSnapDeviceIpsList();
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
         * 播放视频流
         * */
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playButton.getText().equals("播放")) {
                    playRTSP();
                    playButton.setText("停止");
                } else {
                    stopRTSP();
                    playButton.setText("播放");
                }
            }
        });
    }

    /*
     * 添加报警主机信息
     * */
    private void addAlarmHost() {
        try {
            //第一步：获取全部报警主机信息，判断是否已存在
            String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":12346/alarm";
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
//                Tool.showMessage("添加报警主机成功", "提示", 0);
            } else {
                Tool.showMessage("添加报警主机失败，错误码：" + resultData.getString("errorMsg"), "提示", 0);
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
        try {
            switch (status) {
                case 0:
                    Vector vectorOne = new Vector();
                    vectorOne.add(0, Base64.encodeBytes(Tool.getURLStream(captureLibResultEntity.getImage())));
                    vectorOne.add(1, "<html><body>抓拍时间：" +
                            captureLibResultEntity.getTargetAttrs().getFaceTime() +
                            "<br>抓拍机：    " +
                            captureLibResultEntity.getTargetAttrs().getDeviceName() +
                            "</body></html>");
//                    vectorOne.add(1, captureLibResultEntity.getTargetAttrs().getFaceTime() + "\n" + captureLibResultEntity.getTargetAttrs().getDeviceName());
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
                    vectorTwo.add(2, Tool.displayAlarmResult(alarmResultEntity.getTargetAttrs().getFaceTime(), alarmResultEntity.getTargetAttrs().getDeviceName(), alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0), Egci.fdLibMaps));
                    blackAlarmContentTableModel.addRow(vectorTwo);
                    if (blackAlarmRollingStatus == 1) {
                        moveScrollBarToBottom(blackAlarmScrollBar);
                    }
                    blackAlarmBottomStatus = 0;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    /*
     * 播放视频流
     * */
    private void playRTSP() {
        embeddedMediaPlayerList.clear();
        embeddedMediaPlayerComponentList.clear();
        getMonitor();
        //加载vlc播放器相关库
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "vlc"); // vlc : libvlc.dll,libvlccore.dll和plugins目录的路径,这里我直接放到了项目根目录下
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        for (int i = 0; i < monitorPointEntityList.size(); i++) {
            if (Egci.snapDeviceIps.contains(monitorPointEntityList.get(i).getDeviceIP())) {
                String streamURL = monitorPointEntityList.get(i).getStreamURL();
                if (streamURL == null) {
                    return;
                }
                EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
                GridLayout gridBagLayout = new GridLayout(1, 1, 2, 2);
                livePreviewContentPanelList.get(i).setLayout(gridBagLayout);
                livePreviewContentPanelList.get(i).add(mediaPlayerComponent);
                livePreviewContentPanelList.get(i).updateUI();
                //设置参数并播放
                EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
                String[] options = {"rtsp-tcp", "network-caching=300"}; //配置参数 rtsp-tcp作用: 使用 RTP over RTSP (TCP) (默认关闭),network-caching=300:网络缓存300ms,设置越大延迟越大,太小视频卡顿,300较为合适
                mediaPlayer.playMedia(Tool.getRTSPAddress(streamURL), options); //播放rtsp流
                mediaPlayer.start();//停止了哈
                embeddedMediaPlayerList.add(mediaPlayer);
                embeddedMediaPlayerComponentList.add(mediaPlayerComponent);
            }
        }
    }

    /*
     * 停止视频流
     * */
    private void stopRTSP() {
        try {
            for (int i = 0; i < monitorPointEntityList.size(); i++) {
                embeddedMediaPlayerList.get(i).stop();
                livePreviewContentPanelList.get(i).remove(embeddedMediaPlayerComponentList.get(i));
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

    }

    /*
     * 获取全部监控点信息
     * */
    private void getMonitor() {
        monitorPointEntityList.clear();
        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/monitorPoint/search";
        org.json.JSONObject inboundData = new org.json.JSONObject();
        try {
            inboundData.put("searchResultPosition", 0);
            inboundData.put("maxResults", 100);
            inboundData.put("isRegion", "yes");
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
            monitorPointEntityList = JSONObject.parseArray(new org.json.JSONObject(resultData.getString("ctrlCenter")).getString("monitorPoint"), MonitorPointEntity.class);
        } catch (JSONException e) {
//            Tool.showMessage("获取监控点失败或没有添加监控点", "提示", 0);
//            e.printStackTrace();
        }
    }

    /*
     * 获取各核抓拍机ip集合
     * */
    private void getSnapDeviceIpsList() {
        Egci.snapDeviceIps.clear();
        Egci.snapDeviceIpsOne.clear();
        Egci.snapDeviceIpsTwo.clear();
        Egci.snapDeviceIpsThree.clear();
        getMonitor();
        for (MonitorPointEntity monitorPointEntity : monitorPointEntityList) {
            switch (monitorPointEntity.getRegionName()) {
                case "一核":
                    Egci.snapDeviceIpsOne.add(monitorPointEntity.getDeviceIP());
                    break;
                case "二核":
                    Egci.snapDeviceIpsTwo.add(monitorPointEntity.getDeviceIP());
                    break;
                case "三核":
                    Egci.snapDeviceIpsThree.add(monitorPointEntity.getDeviceIP());
                default:
                    break;
            }
        }
        switch (Egci.accountEntity.getAccountPermission()) {
            case 0:
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsOne);
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsTwo);
                Egci.snapDeviceIps.addAll(Egci.snapDeviceIpsThree);
                break;
            case 1:
                Egci.snapDeviceIps = Egci.snapDeviceIpsOne;
                break;
            case 2:
                Egci.snapDeviceIps = Egci.snapDeviceIpsTwo;
                break;
            case 3:
                Egci.snapDeviceIps = Egci.snapDeviceIpsThree;
                break;
            default:
                break;
        }
    }

    /*
     * 获取人脸库列表
     * */
    public void getFDLib() {
        try {
            Egci.fdLibMaps.clear();
            fdLibEntityList.clear();
            fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                Egci.fdLibMaps.put(fdLibEntity.getFDID(), fdLibEntity.getName());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
