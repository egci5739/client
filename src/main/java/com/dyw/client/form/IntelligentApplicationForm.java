package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.protection.*;
import com.dyw.client.functionForm.SearchByPicForm;
import com.dyw.client.service.AlarmTableCellRenderer;
import com.dyw.client.service.MyHttpHandlerService;
import com.dyw.client.service.SnapAlarmTableCellRenderer;
import com.dyw.client.tool.Tool;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import net.iharder.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.spec.ECGenParameterSpec;
import java.util.*;
import java.util.List;

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

    private Logger logger = LoggerFactory.getLogger(IntelligentApplicationForm.class);
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

    private JPopupMenu searchPopupMenu;
    private JMenuItem searchMenuItem = new JMenuItem("查找人员库");
    private JMenuItem viewMenuItem = new JMenuItem("查看录像");
    private int menuStatus = 0;

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
            logger.error("获取控制中心和区域出错", e);
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
        //黑名单报警
        snapAlarmContentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnSnapAlarmInfo = {"抓拍图", "底图", "报警信息"};
        snapAlarmContentTableModel.setColumnIdentifiers(columnSnapAlarmInfo);
        snapAlarmContentTable.setModel(snapAlarmContentTableModel);
        TableCellRenderer snapAlarmCellRenderer = new AlarmTableCellRenderer();
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
        //白名单报警
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
        //陌生人
        whiteAlarmContentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnWhiteAlarmInfo = {"抓拍图", "时间", "图片URL"};
        whiteAlarmContentTableModel.setColumnIdentifiers(columnWhiteAlarmInfo);
        whiteAlarmContentTable.setModel(whiteAlarmContentTableModel);
        TableCellRenderer whiteAlarmCellRenderer = new SnapAlarmTableCellRenderer();
        whiteAlarmContentTable.setDefaultRenderer(Object.class, whiteAlarmCellRenderer);
        hideColumn(whiteAlarmContentTable, 2);
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
        /*
         * 弹出右键菜单
         * */
        whiteAlarmContentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //在table显示
                    searchPopupMenu = new JPopupMenu();
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = whiteAlarmContentTable.rowAtPoint(e.getPoint());
                    searchPopupMenu.add(searchMenuItem);
                    searchPopupMenu.add(viewMenuItem);
                    searchPopupMenu.show(whiteAlarmContentTable, e.getX(), e.getY());
                }
            }
        });
        /*
         * 以图搜图
         * */
        searchMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByPhoto((String) whiteAlarmContentTable.getValueAt(menuStatus, 2));//待解决
            }
        });
        /*
         * 查看录像
         * */
        viewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Tool.showMessage((String) whiteAlarmContentTable.getValueAt(menuStatus, 2), "提示", 1);
                Tool.showMessage("未启用", "提示", 1);
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
            logger.error("添加报警主机出错", e);
        }
    }

    /*
     * 监听报警消息
     * */
    private void monitorAlarmInfo() {
        HttpServerProvider provider = HttpServerProvider.provider();
        try {
            httpserver = provider.createHttpServer(new InetSocketAddress(12346), 100);
            httpserver.createContext("/alarm", new MyHttpHandlerService(this));
            httpserver.setExecutor(null);
            httpserver.start();
        } catch (IOException e) {
            logger.error("监听报警消息出错", e);
        }
    }

    /*
     * 显示报警信息
     * status  0：抓拍图；1：名单报警；
     * */
    public void showAlarmInfo(CaptureLibResultEntity captureLibResultEntity, AlarmResultEntity alarmResultEntity) {
        try {
            if (Egci.fdLibIDForStranger.equals(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getBlacklist_id())) {
                //陌生人报警
                Vector vectorThree = new Vector();
                vectorThree.add(0, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getImage())));
                vectorThree.add(1, "<html><body>抓拍时间：" +
                        alarmResultEntity.getTargetAttrs().getFaceTime() +
                        "<br>抓拍机：" +
                        alarmResultEntity.getTargetAttrs().getDeviceName() +
                        "</body></html>");
                vectorThree.add(2, alarmResultEntity.getImage());
                whiteAlarmContentTableModel.addRow(vectorThree);
                if (whiteAlarmRollingStatus == 1) {
                    moveScrollBarToBottom(whiteAlarmScrollBar);
                    whiteAlarmBottomStatus = 0;
                }
            } else if (Egci.fdLibIDForStaff.equals(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getBlacklist_id())) {
                //白名单报警
                Vector vectorTwo = new Vector();
                vectorTwo.add(0, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getImage())));
                vectorTwo.add(1, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getHuman_data().get(0).getFace_picurl())));
                vectorTwo.add(2, Tool.displayAlarmResult(alarmResultEntity.getTargetAttrs().getFaceTime(), alarmResultEntity.getTargetAttrs().getDeviceName(), alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0), Egci.fdLibMaps));
                blackAlarmContentTableModel.addRow(vectorTwo);
                if (blackAlarmRollingStatus == 1) {
                    moveScrollBarToBottom(blackAlarmScrollBar);
                    blackAlarmBottomStatus = 0;
                }
            } else if (Egci.fdLibIDForBlack.equals(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getBlacklist_id())) {
                //黑名单报警
                Vector vectorOne = new Vector();
                vectorOne.add(0, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getImage())));
                vectorOne.add(1, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getHuman_data().get(0).getFace_picurl())));
                vectorOne.add(2, Tool.displayAlarmResult(alarmResultEntity.getTargetAttrs().getFaceTime(), alarmResultEntity.getTargetAttrs().getDeviceName(), alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0), Egci.fdLibMaps));
                snapAlarmContentTableModel.addRow(vectorOne);
                if (snapAlarmRollingStatus == 1) {
                    moveScrollBarToBottom(snapAlarmScrollBar);
                    snapAlarmBottomStatus = 0;
                }
            }
        } catch (Exception e) {
            logger.error("显示报警结果出错", e);
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
        } catch (IndexOutOfBoundsException e) {
            logger.error("停止视频流出错", e);
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
            logger.error("获取监控点出错", e);
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
    private void getFDLib() {
        try {
            Egci.fdLibMaps.clear();

            fdLibEntityList.clear();
            fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                Egci.fdLibMaps.put(fdLibEntity.getFDID(), fdLibEntity.getName());
                switch (fdLibEntity.getName()) {
                    case "电厂人员库MSR":
                        Egci.fdLibIDForStranger = fdLibEntity.getFDID();
                        break;
                    case "电厂人员库":
                        Egci.fdLibIDForStaff = fdLibEntity.getFDID();
                        break;
                    case "黑名单":
                        Egci.fdLibIDForBlack = fdLibEntity.getFDID();
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e1) {
            logger.error("获取人脸库列表出错", e1);
        }
    }

    /*
     * 以图搜图
     * */
    private void searchByPhoto(String faceURL) {
        String instructionSearchByPic = "/ISAPI/Intelligent/FDLib/searchByPic?format=json";
        org.json.JSONObject inboundDataSearchByPhoto = new org.json.JSONObject();
        Map<String, Object> mapSearchByPhoto = new HashMap<>();
        JSONArray jsonArraySearchByPhoto = new JSONArray();
        try {
            inboundDataSearchByPhoto.put("searchResultPosition", 0);
            inboundDataSearchByPhoto.put("maxResults", 20);
            inboundDataSearchByPhoto.put("modelMaxNum", 20);
            inboundDataSearchByPhoto.put("dataType", "URL");
            inboundDataSearchByPhoto.put("startTime", "1900-01-01");
            inboundDataSearchByPhoto.put("endTime", "2030-01-01");
            inboundDataSearchByPhoto.put("maxSimilarity", 1.00);
            inboundDataSearchByPhoto.put("minSimilarity", 0.00);
            mapSearchByPhoto.put("FDID", Egci.fdLibIDForStaff);
            jsonArraySearchByPhoto.put(0, mapSearchByPhoto);
            inboundDataSearchByPhoto.put("FDLib", jsonArraySearchByPhoto);
            inboundDataSearchByPhoto.put("faceURL", faceURL);
            org.json.JSONObject resultDataSearchByPhoto = Tool.sendInstructionAndReceiveStatusAndData(3, instructionSearchByPic, inboundDataSearchByPhoto);
            if (resultDataSearchByPhoto.getInt("statusCode") == 1) {
                List<SearchByPicEntity> searchByPicEntityList = JSONObject.parseArray(resultDataSearchByPhoto.getString("MatchList"), SearchByPicEntity.class);
                SearchByPicForm searchByPicForm = new SearchByPicForm(searchByPicEntityList);
                searchByPicForm.init();
            } else {
                Tool.showMessage("以图搜图错误，错误码：" + resultDataSearchByPhoto.getInt("statusCode"), "提示", 0);
            }
        } catch (JSONException e) {
            logger.error("以图搜图出错", e);
        }
    }

    /*
     * 隐藏图片url这一页
     * */
    protected void hideColumn(JTable table, int index) {
        TableColumn tc = table.getColumnModel().getColumn(index);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setMinWidth(0);
        tc.setWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(index).setMinWidth(0);
    }
}