//package com.dyw.client.form;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.dyw.client.controller.Egci;
//import com.dyw.client.entity.StaffEntity;
//import com.dyw.client.entity.protection.*;
//import com.dyw.client.functionForm.EquipmentFunction;
//import com.dyw.client.functionForm.FaceBaseFunction;
//import com.dyw.client.functionForm.FaceInfoFunction;
//import com.dyw.client.functionForm.MonitorFunction;
//import com.dyw.client.service.AlarmTableCellRenderer;
//import com.dyw.client.service.MyHttpHandlerService;
//import com.dyw.client.service.SnapAlarmTableCellRenderer;
//import com.dyw.client.tool.Tool;
//import com.sun.jna.Native;
//import com.sun.jna.NativeLibrary;
//import com.sun.net.httpserver.HttpServer;
//import com.sun.net.httpserver.spi.HttpServerProvider;
//import net.iharder.Base64;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import uk.co.caprica.vlcj.binding.LibVlc;
//import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
//import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;
//
//
//import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//import java.util.*;
//import java.util.List;
//
//public class ProtectionForm {
//    public static Map<String, String> fdLibMaps;//人脸库集合
//    private Logger logger = LoggerFactory.getLogger(ProtectionForm.class);
//    private DefaultTableModel deviceManagementContentTableModel;
//    private DefaultTableModel personManagementContentResultTableModel;
//    private DefaultTableModel monitorManagementContentTableModel;
//    //    private List<FDLibEntity> libEntityList;//人脸库列表
//    private List<String> FDLibNames;//人脸库名称列表
//    private List<FaceInfoEntity> faceInfoEntityList;//人员列表
//    private List<FDLibEntity> fdLibEntityList;//人脸库列表
//    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
//    private List<RegionEntity> regionEntityList;//区域列表
//    private String FDID;//人脸库ID
//    private List<DeviceEntity> deviceEntityList;//设备列表
//    private List<MonitorPointEntity> monitorPointEntityList;//监控点列表
//
//    private List<RelateInfoEntity> relateInfoEntityList;//布控信息表
//    private List<PlanInfoEntity> planInfoEntityList;//布控计划表
//
//    private JPanel protection;
//    private JTabbedPane tabbedPane1;
//    private JPanel intelligentApplicationPanel;
//    private JPanel personManagementPanel;
//    private JPanel monitorManagementPanel;
//    private JPanel resourceManagementPanel;
//    private JPanel resourceManagementTitlePanel;
//    private JPanel resourceManagementContentPanel;
//    private JScrollPane resourceManagementContentScroll;
//    private JTable resourceManagementContentTable;
//    private JButton addEquipmentButton;
//    private JButton deleteEquipmentButton;
//    private JPanel personManagementContentPanel;
//    private JPanel personManagementBasePanel;
//    private JPanel personManagementBaseToolBarPanel;
//    private JPanel personManagementBaseListPanel;
//    private JPanel personManagementBaseConditionPanel;
//    private JPanel personManagementBaseConditionSearchButtonPanel;
//    private JButton personManagementBaseAddButton;
//    private JButton personManagementBaseEditButton;
//    private JButton personManagementBaseDeleteButton;
//    private JScrollPane personManagementBaseListScroll;
//    private JList personManagementBaseList;
//    private JPanel personManagementContentToolBarPanel;
//    private JPanel personManagementContentResultPanel;
//    private JPanel personManagementContentSelectPagePanel;
//    private JButton personManagementContentToolBarAddByCardButton;
//    private JButton personManagementContentToolBarDeleteButton;
//    private JTable personManagementContentResultTable;
//    private JScrollPane personManagementContentResultScroll;
//    private JButton personManagementContentToolBarImportButton;
//    private JPanel livePreviewPanel;
//    private JPanel blackAlarmPanel;
//    private JPanel whiteAlarmPanel;
//    private JPanel snapAlarmPanel;
//    private JPanel livePreviewTitlePanel;
//    private JPanel livePreviewContentPanel;
//    private JLabel livePreviewTitleLabel;
//    private JPanel livePreviewContentOnePanel;
//    private JPanel livePreviewContentTwoPanel;
//    private JPanel livePreviewContentThreePanel;
//    private JPanel livePreviewContentFourPanel;
//    private JPanel livePreviewContentFivePanel;
//    private JPanel livePreviewContentSixPanel;
//    private JPanel livePreviewContentSevenPanel;
//    private JPanel livePreviewContentEightPanel;
//    private List<JPanel> livePreviewContentPanelList;
//    private JPanel monitorManagementTolBarPanel;
//    private JPanel monitorManagementContentPanel;
//    private JButton monitorManagementAddButton;
//    private JButton monitorManagementEditButton;
//    private JButton monitorManagementDeleteButton;
//    private JScrollPane monitorManagementContentScroll;
//    private JTable monitorManagementContentTable;
//    //==================================
//    private JPanel blackAlarmTitlePanel;
//    private JPanel blackAlarmContentPanel;
//    private JPanel whiteAlarmTitlePanel;
//    private JPanel whiteAlarmContentPanel;
//    private JPanel snapAlarmTitlePanel;
//    private JPanel snapAlarmContentPanel;
//    private JLabel blackAlarmTitleLabel;
//    private JLabel whiteAlarmTitleLabel;
//    private JLabel snapAlarmTitleLabel;
//    private JTable snapAlarmContentTable;
//    private JScrollPane snapAlarmContentScroll;
//    private JTable blackAlarmContentTable;
//    private JTable whiteAlarmContentTable;
//    private JScrollPane blackAlarmContentScroll;
//    private JScrollPane whiteAlarmContentScroll;
//    private JCheckBox blackAlarmRollingCheckBOx;
//    private JButton blackAlarmClearButton;
//    private JCheckBox snapAlarmRollingCheckBOx;
//    private JButton snapAlarmClearButton;
//    private JCheckBox whiteAlarmRollingCheckBOx;
//    private JButton whiteAlarmClearButton;
//    private JButton personManagementContentToolBarAddButton;
//    private DefaultTableModel snapAlarmContentTableModel;
//    private DefaultTableModel blackAlarmContentTableModel;
//    private DefaultTableModel whiteAlarmContentTableModel;
//
//    private JScrollBar snapAlarmScrollBar;
//    private JScrollBar blackAlarmScrollBar;
//    private JScrollBar whiteAlarmScrollBar;
//    private int snapAlarmRollingStatus = 1;
//    private int blackAlarmRollingStatus = 1;
//    private int whiteAlarmRollingStatus = 1;
//    private int snapAlarmBottomStatus = 0;
//    private int blackAlarmBottomStatus = 0;
//    private int whiteAlarmBottomStatus = 0;
//
//
//    public HttpServer httpserver = null;
//
//
//    /*
//     * 构造函数
//     * */
//    public ProtectionForm() {
//        fdLibMaps = new HashMap<>();
//        //抓拍图
//        snapAlarmContentTableModel = new DefaultTableModel() {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        String[] columnSnapAlarmInfo = {"抓拍图", "时间"};
//        snapAlarmContentTableModel.setColumnIdentifiers(columnSnapAlarmInfo);
//        snapAlarmContentTable.setModel(snapAlarmContentTableModel);
//        TableCellRenderer snapAlarmCellRenderer = new SnapAlarmTableCellRenderer();
//        snapAlarmContentTable.setDefaultRenderer(Object.class, snapAlarmCellRenderer);
//        snapAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && snapAlarmBottomStatus <= 3) {
//                    snapAlarmContentScroll.getVerticalScrollBar().setValue(snapAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - snapAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
//                    snapAlarmBottomStatus++;
//                }
//            }
//        });
//        snapAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
//        snapAlarmScrollBar = snapAlarmContentScroll.getVerticalScrollBar();
//        snapAlarmRollingCheckBOx.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                JCheckBox jcb = (JCheckBox) e.getItem();
//                // 判断是否被选择
//                if (jcb.isSelected()) {
//                    snapAlarmRollingStatus = 1;
//                } else {
//                    snapAlarmRollingStatus = 0;
//                }
//            }
//        });
//        snapAlarmClearButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                snapAlarmContentTableModel.setRowCount(0);
//            }
//        });
//        //黑名单
//        blackAlarmContentTableModel = new DefaultTableModel() {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        String[] columnBlackAlarmInfo = {"抓拍图", "底图", "报警信息"};
//        blackAlarmContentTableModel.setColumnIdentifiers(columnBlackAlarmInfo);
//        blackAlarmContentTable.setModel(blackAlarmContentTableModel);
//        TableCellRenderer blackAlarmCellRenderer = new AlarmTableCellRenderer();
//        blackAlarmContentTable.setDefaultRenderer(Object.class, blackAlarmCellRenderer);
//        blackAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && blackAlarmBottomStatus <= 3) {
//                    blackAlarmContentScroll.getVerticalScrollBar().setValue(blackAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - blackAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
//                    blackAlarmBottomStatus++;
//                }
//            }
//        });
//        blackAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
//        blackAlarmScrollBar = blackAlarmContentScroll.getVerticalScrollBar();
//        blackAlarmRollingCheckBOx.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                JCheckBox jcb = (JCheckBox) e.getItem();
//                // 判断是否被选择
//                if (jcb.isSelected()) {
//                    blackAlarmRollingStatus = 1;
//                } else {
//                    blackAlarmRollingStatus = 0;
//                }
//            }
//        });
//        blackAlarmClearButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                blackAlarmContentTableModel.setRowCount(0);
//            }
//        });
//        //白名单
//        whiteAlarmContentTableModel = new DefaultTableModel() {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        String[] columnWhiteAlarmInfo = {"抓拍图", "底图", "报警信息"};
//        whiteAlarmContentTableModel.setColumnIdentifiers(columnWhiteAlarmInfo);
//        whiteAlarmContentTable.setModel(whiteAlarmContentTableModel);
//        TableCellRenderer whiteAlarmCellRenderer = new AlarmTableCellRenderer();
//        whiteAlarmContentTable.setDefaultRenderer(Object.class, whiteAlarmCellRenderer);
//        whiteAlarmContentScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                if (e.getAdjustmentType() == AdjustmentEvent.TRACK && whiteAlarmBottomStatus <= 3) {
//                    whiteAlarmContentScroll.getVerticalScrollBar().setValue(whiteAlarmContentScroll.getVerticalScrollBar().getModel().getMaximum() - whiteAlarmContentScroll.getVerticalScrollBar().getModel().getExtent());
//                    whiteAlarmBottomStatus++;
//                }
//            }
//        });
//        whiteAlarmContentScroll.getVerticalScrollBar().setUnitIncrement(20);
//        whiteAlarmScrollBar = whiteAlarmContentScroll.getVerticalScrollBar();
//        whiteAlarmRollingCheckBOx.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                JCheckBox jcb = (JCheckBox) e.getItem();
//                // 判断是否被选择
//                if (jcb.isSelected()) {
//                    whiteAlarmRollingStatus = 1;
//                } else {
//                    whiteAlarmRollingStatus = 0;
//                }
//            }
//        });
//        whiteAlarmClearButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                whiteAlarmContentTableModel.setRowCount(0);
//            }
//        });
//        /*
//         * 添加报警主机信息
//         * */
//        addAlarmHost();
//        /*
//         * 监听报警消息
//         * */
//        monitorAlarmInfo();
//        try {
//            //获取根控制中心
//            ctrlCenterEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter?source=device", null).getString("ctrlCenter"), CtrlCenterEntity.class).get(0);
//            //获取区域列表
//            regionEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID(), null).getString("region"), RegionEntity.class);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        livePreviewContentPanelList = new ArrayList<>();
//        livePreviewContentPanelList.add(livePreviewContentOnePanel);
//        livePreviewContentPanelList.add(livePreviewContentTwoPanel);
//        livePreviewContentPanelList.add(livePreviewContentThreePanel);
//        livePreviewContentPanelList.add(livePreviewContentFourPanel);
//        livePreviewContentPanelList.add(livePreviewContentFivePanel);
//        livePreviewContentPanelList.add(livePreviewContentSixPanel);
//        livePreviewContentPanelList.add(livePreviewContentSevenPanel);
//        livePreviewContentPanelList.add(livePreviewContentEightPanel);
//        fdLibEntityList = new ArrayList<>();
//        faceInfoEntityList = new ArrayList<>();
//        FDLibNames = new ArrayList<>();
//        deviceEntityList = new ArrayList<>();
////        libEntityList = new ArrayList<>();
//        monitorPointEntityList = new ArrayList<>();
//        FDID = null;
//        /*
//         * 资源管理
//         * */
//        //初始化全部设备表格
//        deviceManagementContentTableModel = new DefaultTableModel();
//        //添加设备
//        addEquipmentButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addEquipment();
//            }
//        });
//        //删除设备或监控点
//        deleteEquipmentButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteEquipment();
//            }
//        });
//        //加载监控点信息
//        getMonitor();
//
//        /*
//         * 名单管理
//         * */
//        //初始化名单库人脸显示列表
//        String[] columnPersonManagementContentResultInfo = {"姓名", "性别", "出生日期", "卡号"};
//        personManagementContentResultTableModel = new DefaultTableModel();
//        personManagementContentResultTableModel.setColumnIdentifiers(columnPersonManagementContentResultInfo);
//        personManagementContentResultTable.setModel(personManagementContentResultTableModel);
//        //获取人脸库列表
//        getFDLib();
//        //添加人脸库
//        personManagementBaseAddButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addFDLib();
//            }
//        });
//        //修改人脸库
//        personManagementBaseEditButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                editFDLib();
//            }
//        });
//        //选择某一个人脸库
//        personManagementBaseList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (e.getValueIsAdjusting()) {
//                    return;
//                }
//                //显示某一个人脸库中的人员信息
//                showSelectBase();
//            }
//        });
//        //按卡号添加人员
//        personManagementContentToolBarAddByCardButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addFaceByCard();
//            }
//        });
//        //添加人员
//        personManagementContentToolBarAddButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addFace();
//            }
//        });
//        //删除人脸库
//        personManagementBaseDeleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteFDLib();
//            }
//        });
//        //删除人员信息
//        personManagementContentToolBarDeleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteFace();
//            }
//        });
//        //导入全部人员
//        personManagementContentToolBarImportButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                importAllFace();
//            }
//        });
//
//        /*
//         * 布控管理
//         * */
//        relateInfoEntityList = new ArrayList<>();
//        //初始化名单库人脸显示列表
//        String[] columnMonitorManagementContentInfo = {"布控名称", "布控类型", "布控对象", "布控范围", "布控时段", "布控创建时间"};
//        monitorManagementContentTableModel = new DefaultTableModel();
//        monitorManagementContentTableModel.setColumnIdentifiers(columnMonitorManagementContentInfo);
//        monitorManagementContentTable.setModel(monitorManagementContentTableModel);
//        //获取布控列表
//        getMonitorList();
//        //添加布控
//        monitorManagementAddButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addMonitor();
//            }
//        });
//        //删除布控
//        monitorManagementDeleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deleteMonitor();
//            }
//        });
//        //编辑布控
//        monitorManagementEditButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                editMonitor();
//            }
//        });
//
//    }
//
//    /*
//     * 添加报警主机信息
//     * */
//    private void addAlarmHost() {
//        try {
//            //第一步：获取全部报警主机信息，判断是否已存在
//            String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":12346/alarm";
////            System.out.println(url);
//            String instructionGet = "/ISAPI/Event/notification/httpHosts?format=json";
//            List<HttpHostNotificationEntity> httpHostNotificationEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, instructionGet, null).getString("HttpHostNotification"), HttpHostNotificationEntity.class);
//            for (HttpHostNotificationEntity httpHostNotificationEntity : httpHostNotificationEntityList) {
//                if (httpHostNotificationEntity.getUrl().equals(url)) {
//                    return;
//                }
//            }
//            //新增报警主机信息
//            String instruction = "/ISAPI/Event/notification/httpHosts?format=json";
//            org.json.JSONObject inboundDataIn = new org.json.JSONObject();
//            org.json.JSONObject inboundDataOut = new org.json.JSONObject();
//            inboundDataIn.put("url", url);
//            inboundDataIn.put("protocolType", "HTTP");//这里要注意
//            inboundDataIn.put("parameterFormatType", "json");
//            inboundDataIn.put("addressingFormatType", "ipaddress");
//            inboundDataIn.put("httpAuthenticationMethod", "none");//这里要注意
//            inboundDataIn.put("eventType", "alarmResult,captureResult,HFPD");
//            inboundDataOut.put("HttpHostNotification", inboundDataIn);
//            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundDataOut);
//            if (resultData.getInt("statusCode") == 1) {
//                Tool.showMessage("添加报警主机成功", "提示", 0);
//            } else {
//                Tool.showMessage("添加报警主机失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
//            }
//        } catch (JSONException | UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 监听报警消息
//     * */
//    public void monitorAlarmInfo() {
//        HttpServerProvider provider = HttpServerProvider.provider();
//        try {
//            httpserver = provider.createHttpServer(new InetSocketAddress(12346), 100);
//            httpserver.createContext("/alarm", new MyHttpHandlerService(this));
//            httpserver.setExecutor(null);
//            httpserver.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    //=====================================================================================
//    /*
//     *名单管理
//     * */
//    //获取人脸库列表
//    public void getFDLib() {
//        try {
//            fdLibMaps.clear();
//            fdLibEntityList.clear();
//            FDLibNames.clear();
//            fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
//            for (FDLibEntity fdLibEntity : fdLibEntityList) {
//                FDLibNames.add(fdLibEntity.getName());
//                fdLibMaps.put(fdLibEntity.getFDID(), fdLibEntity.getName());
//            }
//            System.out.println(faceInfoEntityList);
//            personManagementBaseList.setListData(FDLibNames.toArray());
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    //创建人脸库
//    public void addFDLib() {
//        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, null);
//        faceBaseFunction.init();
//    }
//
//    //修改人脸库
//    private void editFDLib() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, fdLibEntityList.get(personManagementBaseList.getSelectedIndex()));
//        faceBaseFunction.init();
//    }
//
//    //按卡号添加人脸
//    private void addFaceByCard() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        String cardNumber = JOptionPane.showInputDialog(null, "请输入卡号", "添加新用户", 1);
//        org.json.JSONObject inboundData = new org.json.JSONObject();
//        //从数据库查询人员信息
//        try {
//            if (cardNumber != null) {
//                StaffEntity staffEntity = Egci.session.selectOne("mapping.staffMapper.getResultStaffWithCard", cardNumber);
//                if (staffEntity == null) {
//                    Tool.showMessage("人员不存在", "提示", 0);
//                } else {
//                    //第一步：先将人员图片发送到脸谱服务器，获取faceURL
//                    org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, FDID, staffEntity.getPhoto(), null);
//                    if (resultFaceUrlData == null) {
//                        Tool.showMessage("添加失败", "提示", 0);
//                        return;
//                    } else {
//                        inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
//                    }
//                    String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
//                    inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
//                    inboundData.put("faceLibType", "blackFD");
//                    inboundData.put("FDID", FDID);
//                    inboundData.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
//                    inboundData.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
//                    inboundData.put("bornTime", staffEntity.getBirthday());
//                    org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
//                    if (resultData.getInt("statusCode") == 1) {
//                        Tool.showMessage("添加成功", "提示", 0);
//                        showSelectBase();
//                    } else {
//                        Tool.showMessage("添加失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
//                    }
//                }
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    //添加人脸信息
//    private void addFace() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        FaceInfoFunction faceInfoFunction = new FaceInfoFunction(FDID, this);
//        faceInfoFunction.init();
//    }
//
//    //删除人脸库
//    private void deleteFDLib() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        try {
//            if (!Tool.showConfirm("确认删除", "删除提示")) {
//                return;
//            }
//            String instruction = "/ISAPI/Intelligent/FDLib?format=json&FDID=" + fdLibEntityList.get(personManagementBaseList.getSelectedIndex()).getFDID() + "&faceLibType=blackFD";
//            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
//            if (resultData.getInt("statusCode") == 1) {
//                Tool.showMessage("删除成功", "提示", 0);
//                getFDLib();
//            } else {
//                Tool.showMessage("删除失败", "提示", 0);
//            }
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    /*
//     * 显示选定人脸库中的人员信息
//     * */
//    public void showSelectBase() {
//        personManagementContentResultTableModel.setRowCount(0);
//        FDLibEntity fdLibEntity = fdLibEntityList.get(personManagementBaseList.getSelectedIndex());
//        FDID = fdLibEntity.getFDID();
//        System.out.println("FDID是：" + FDID);
//        try {
//            if (fdLibEntityList.get(personManagementBaseList.getSelectedIndex()) != null) {
//                //获取人脸库中的人脸数据
////                String inboundData = "{\"searchResultPosition\":0,\"maxResults\":100,\"faceLibType\":\"blackFD\",\"FDID\":\"" + FDID + "\"}";
//                String instruction = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
//                org.json.JSONObject inboundData = new org.json.JSONObject();
//                inboundData.put("searchResultPosition", 0);
//                inboundData.put("maxResults", 100);
//                inboundData.put("faceLibType", "blackFD");
//                inboundData.put("FDID", FDID);
//                faceInfoEntityList.clear();
//                faceInfoEntityList = JSON.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData).getString("MatchList"), FaceInfoEntity.class);
//                for (FaceInfoEntity faceInfoEntity : faceInfoEntityList) {
//                    if (faceInfoEntity.getName().contains("_")) {//显示卡号
//                        StaffEntity staffEntity = Tool.splitNameAndGetStaff(faceInfoEntity.getName());
//                        Vector v = new Vector();
//                        v.add(0, staffEntity.getName());
//                        v.add(1, faceInfoEntity.getGender());
//                        v.add(2, faceInfoEntity.getBornTime());
//                        v.add(3, staffEntity.getCardNumber());
//                        personManagementContentResultTableModel.addRow(v);
//                    } else {//没有卡号
//                        Vector v = new Vector();
//                        v.add(0, faceInfoEntity.getName());
//                        v.add(1, faceInfoEntity.getGender());
//                        v.add(2, faceInfoEntity.getBornTime());
//                        v.add(3, "");
//                        personManagementContentResultTableModel.addRow(v);
//                    }
//                }
//            }
//        } catch (ArrayIndexOutOfBoundsException | NullPointerException | JSONException ignored) {
//
//        }
//    }
//
//    /*
//     * 删除人员信息
//     * */
//    private void deleteFace() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        if (Tool.showConfirm("是否删除人员", "提示")) {
//            org.json.JSONObject deleteInboundData = new org.json.JSONObject();
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("value", faceInfoEntityList.get(personManagementContentResultTable.getSelectedRow()).getFPID());
//            JSONArray jsonarry = new JSONArray();
//            try {
//                jsonarry.put(0, map);
//                deleteInboundData.put("FPID", jsonarry);
//                org.json.JSONObject resultData = Tool.faceInfoOperation(2, FDID, null, deleteInboundData);
//                if (resultData.getInt("statusCode") == 1) {
//                    Tool.showMessage("删除成功", "提示", 0);
//                    showSelectBase();
//                } else {
//                    Tool.showMessage("删除失败", "提示", 0);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /*
//     * 导入全部人脸信息
//     * */
//    private void importAllFace() {
//        if (FDID == null) {
//            Tool.showMessage("请选择一个人脸库", "提示", 0);
//            return;
//        }
//        List<StaffEntity> staffEntityList = Egci.session.selectList("mapping.staffMapper.getAllStaff");
//        for (StaffEntity staffEntity : staffEntityList) {
//            String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
//            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, FDID, staffEntity.getPhoto(), null);
//            org.json.JSONObject inboundData = new org.json.JSONObject();
//            try {
//                inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
//                inboundData.put("faceLibType", "blackFD");
//                inboundData.put("FDID", FDID);
//                inboundData.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
//                inboundData.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
//                inboundData.put("bornTime", staffEntity.getBirthday());
//                org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
//                showSelectBase();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //==================================================================================
//    /*
//     * 资源管理
//     * */
//    /*
//     * 获取全部设备列表
//     * 暂时不用
//     * */
//    public void getDevice() {
//        deviceEntityList.clear();
//        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device/search";
//        org.json.JSONObject inboundData = new org.json.JSONObject();
//        try {
//            String[] columnResourceManagementInfo = {"设备名称", "类型", "IP地址", "端口"};
//            deviceManagementContentTableModel.setColumnIdentifiers(columnResourceManagementInfo);
//            inboundData.put("searchResultPosition", 0);
//            inboundData.put("maxResults", 100);
//            String resultDevice = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData).getString("ctrlCenter");
//            deviceEntityList = JSONObject.parseArray(new org.json.JSONObject(resultDevice).getString("device"), DeviceEntity.class);
//            resourceManagementContentTable.setModel(deviceManagementContentTableModel);
//            deviceManagementContentTableModel.setRowCount(0);
//            for (DeviceEntity deviceEntity : deviceEntityList) {
//                Vector vector = new Vector();
//                vector.add(0, deviceEntity.getDeviceName());
//                vector.add(1, deviceEntity.getDeviceType());
//                vector.add(2, deviceEntity.getDeviceIP());
//                vector.add(3, deviceEntity.getDevicePort());
//                deviceManagementContentTableModel.addRow(vector);
//            }
//        } catch (JSONException e) {
//            Tool.showMessage("获取设备失败或没有添加设备", "提示", 0);
//        }
//    }
//
//    /*
//     * 添加设备
//     * */
//    private void addEquipment() {
//        EquipmentFunction equipmentFunction = new EquipmentFunction(ctrlCenterEntity, regionEntityList, this);
//        equipmentFunction.init();
//    }
//
//    /*
//     * 删除设备或监控点
//     * */
//    private void deleteEquipment() {
//        if (resourceManagementContentTable.getSelectedRow() == -1) {
//            Tool.showMessage("请先选择设备", "提示", 0);
//            return;
//        }
//        if (!Tool.showConfirm("确认删除", "提示")) {
//            return;
//        }
//        try {
//            //第一步：先进行撤防操作
//            MonitorPointEntity monitorPointEntity = monitorPointEntityList.get(resourceManagementContentTable.getSelectedRow());
//            String instructionGuard = "/ISAPI/SDT/Management/Unguard";
//            org.json.JSONObject inboundDataGuard = new org.json.JSONObject();
//            Map<String, Object> mapGuard = new HashMap<>();
//            JSONArray jsonArrayGuard = new JSONArray();
//            mapGuard.put("monitorPointID", monitorPointEntity.getMonitorPointID());
//            jsonArrayGuard.put(0, mapGuard);
//            inboundDataGuard.put("monitorPoint", jsonArrayGuard);
//            org.json.JSONObject resultDataGuard = Tool.sendInstructionAndReceiveStatus(3, instructionGuard, inboundDataGuard);
//            if (resultDataGuard.getInt("statusCode") == 1) {
//                //第二步：删除设备
//                String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device/delete";
//                org.json.JSONObject inboundDataEquipment = new org.json.JSONObject();
//                HashMap<String, Object> mapEquipment = new HashMap<String, Object>();
//                mapEquipment.put("DeviceID", monitorPointEntity.getDeviceID());
//                JSONArray jsonArrayEquipment = new JSONArray();
//                jsonArrayEquipment.put(0, mapEquipment);
//                inboundDataEquipment.put("Device", jsonArrayEquipment);
//                org.json.JSONObject resultDataEquipment = Tool.sendInstructionAndReceiveStatus(2, instruction, inboundDataEquipment);
//                if (resultDataEquipment.getInt("statusCode") == 1) {
//                    Tool.showMessage("删除成功", "提示", 0);
//                    getMonitor();
//                } else {
//                    Tool.showMessage("删除设备失败，错误码：" + resultDataEquipment.getInt("statusCode"), "提示", 0);
//                }
//            } else {
//                Tool.showMessage("撤防失败,错误码：" + resultDataGuard.getInt("statusCode"), "提示", 0);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //获取全部监控点信息
//    public void getMonitor() {
//        monitorPointEntityList.clear();
//        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/monitorPoint/search";
//        org.json.JSONObject inboundData = new org.json.JSONObject();
//        try {
//            String[] columnResourceMonitorInfo = {"设备名称", "区域", "IP地址", "端口", "布防状态"};
//            deviceManagementContentTableModel.setColumnIdentifiers(columnResourceMonitorInfo);
//            resourceManagementContentTable.setModel(deviceManagementContentTableModel);
//            deviceManagementContentTableModel.setRowCount(0);
//            inboundData.put("searchResultPosition", 0);
//            inboundData.put("maxResults", 100);
//            inboundData.put("isRegion", "yes");
//            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
//            monitorPointEntityList = JSONObject.parseArray(new org.json.JSONObject(resultData.getString("ctrlCenter")).getString("monitorPoint"), MonitorPointEntity.class);
//            for (MonitorPointEntity monitorPointEntity : monitorPointEntityList) {
//                Vector vector = new Vector();
//                vector.add(0, monitorPointEntity.getMonitorPointName());
//                vector.add(1, monitorPointEntity.getRegionName());
//                vector.add(2, monitorPointEntity.getDeviceIP());
//                vector.add(3, monitorPointEntity.getDevicePort());
//                vector.add(4, monitorPointEntity.getIsGuard());
//                deviceManagementContentTableModel.addRow(vector);
//            }
//        } catch (JSONException e) {
//            Tool.showMessage("获取监控点失败或没有添加监控点", "提示", 0);
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 布控管理
//     * */
//    /*
//     * 获取全部布控信息
//     * */
//    public void getMonitorList() {
//        relateInfoEntityList.clear();
//        monitorManagementContentTableModel.setRowCount(0);
//        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json";
//        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(1, instruction, null);
//        try {
//            relateInfoEntityList = JSONObject.parseArray(resultData.getString("relateInfo"), RelateInfoEntity.class);
//            for (RelateInfoEntity relateInfoEntity : relateInfoEntityList) {
//                Vector vector = new Vector();
//                vector.add(0, relateInfoEntity.getName());
//                vector.add(1, relateInfoEntity.getListType());
//                vector.add(2, relateInfoEntity.getFDID());
//                vector.add(3, relateInfoEntity.getCameraName());
//                vector.add(4, relateInfoEntity.getPlanInfo().get(0).getStartTime() + "-" + relateInfoEntity.getPlanInfo().get(0).getEndTime());
//                vector.add(5, relateInfoEntity.getCreateTime());
//                monitorManagementContentTableModel.addRow(vector);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 添加布控
//     * */
//    private void addMonitor() {
//        MonitorFunction monitorFunction = new MonitorFunction(null, fdLibEntityList, monitorPointEntityList, this);
//        monitorFunction.init();
//    }
//
//    /*
//     * 编辑布控
//     * */
//    private void editMonitor() {
//        if (monitorManagementContentTable.getSelectedRow() == -1) {
//            Tool.showMessage("请先选择布控信息", "提示", 0);
//            return;
//        }
//        RelateInfoEntity relateInfoEntity = relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow());
//        MonitorFunction monitorFunction = new MonitorFunction(relateInfoEntity, fdLibEntityList, monitorPointEntityList, this);
//        monitorFunction.init();
//    }
//
//    /*
//     * 删除布控
//     * */
//    private void deleteMonitor() {
//        if (monitorManagementContentTable.getSelectedRow() == -1) {
//            Tool.showMessage("请先选择布控信息", "提示", 0);
//            return;
//        }
//        if (!Tool.showConfirm("确认删除？", "提示")) {
//            return;
//        }
//        String instruction = "/ISAPI/Intelligent/FDLib/executeControl?format=json&relateID=" + relateInfoEntityList.get(monitorManagementContentTable.getSelectedRow()).getRelateID();
//        org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
//        try {
//            if (resultData.getInt("statusCode") == 1) {
//                Tool.showMessage("删除成功", "提示", 0);
//                getMonitorList();
//            } else {
//                Tool.showMessage("删除设备失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 显示报警信息
//     * status  0：抓拍图；1：名单报警；
//     * */
//    public void showAlarmInfo(int status, CaptureLibResultEntity captureLibResultEntity, AlarmResultEntity alarmResultEntity) {
//        switch (status) {
//            case 0:
//                Vector vectorOne = new Vector();
//                vectorOne.add(0, Base64.encodeBytes(Tool.getURLStream(captureLibResultEntity.getImage())));
//                vectorOne.add(1, captureLibResultEntity.getTargetAttrs().getFaceTime() + "\n" + captureLibResultEntity.getTargetAttrs().getDeviceName());
//                snapAlarmContentTableModel.addRow(vectorOne);
//                if (snapAlarmRollingStatus == 1) {
//                    moveScrollBarToBottom(snapAlarmScrollBar);
//                }
//                snapAlarmBottomStatus = 0;
//                break;
//            case 1:
//                Vector vectorTwo = new Vector();
//                vectorTwo.add(0, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getImage())));
//                vectorTwo.add(1, Base64.encodeBytes(Tool.getURLStream(alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0).getHuman_data().get(0).getFace_picurl())));
//                vectorTwo.add(2, Tool.displayAlarmResult(alarmResultEntity.getTargetAttrs().getFaceTime(), alarmResultEntity.getTargetAttrs().getDeviceName(), alarmResultEntity.getFaces().get(0).getIdentify().get(0).getCandidate().get(0)));
//                blackAlarmContentTableModel.addRow(vectorTwo);
//                if (blackAlarmRollingStatus == 1) {
//                    moveScrollBarToBottom(blackAlarmScrollBar);
//                }
//                blackAlarmBottomStatus = 0;
//                break;
//            default:
//                break;
//        }
//    }
//
//    /*
//     * 将滚动条移到底部
//     * */
//    private void moveScrollBarToBottom(JScrollBar jScrollBar) {
//        if (jScrollBar != null) {
//            jScrollBar.setValue(jScrollBar.getMaximum());
//        }
//    }
//
//    public void init() {
//        MonitorRealTimeForm monitorRealTimeForm = new MonitorRealTimeForm();
//        tabbedPane1.add("实时监控", monitorRealTimeForm.getMonitorRealTimePanel());
//
//        MonitorHistoryForm monitorHistoryForm = new MonitorHistoryForm();
//        tabbedPane1.add("实时监控", monitorHistoryForm.getMonitorHistroyForm());
//
//        RegisterForm registerForm = new RegisterForm();
//        registerForm.init();
//        tabbedPane1.add("办证端", registerForm.getMain());
//
//        EquipmentManagementForm equipmentManagementForm = new EquipmentManagementForm();
//        tabbedPane1.add("设备管理", equipmentManagementForm.getEquipmentManagementForm());
//
//        DataAnalysisForm dataAnalysisForm = new DataAnalysisForm();
//        tabbedPane1.add("数据分析", dataAnalysisForm.getDataAnalysisForm());
//
//        FaceCollectionManagementForm faceCollectionManagementForm = new FaceCollectionManagementForm();
//        tabbedPane1.add("采集设备管理", faceCollectionManagementForm.getFaceCollectionManagementForm());
//
//        JFrame frame = new JFrame("ProtectionForm");
//        frame.setContentPane(this.protection);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setVisible(true);
//        //加载vlc播放器相关库
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "vlc"); // vlc : libvlc.dll,libvlccore.dll和plugins目录的路径,这里我直接放到了项目根目录下
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//        for (int i = 0; i < monitorPointEntityList.size(); i++) {
//            String streamURL = monitorPointEntityList.get(i).getStreamURL();
//            if (streamURL == null) {
//                return;
//            }
//            EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
//            GridLayout gridBagLayout = new GridLayout(1, 1, 2, 2);
//            livePreviewContentPanelList.get(i).setLayout(gridBagLayout);
//            livePreviewContentPanelList.get(i).add(mediaPlayerComponent);
//            livePreviewContentPanelList.get(i).updateUI();
//            //设置参数并播放
//            EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
//            String[] options = {"rtsp-tcp", "network-caching=300"}; //配置参数 rtsp-tcp作用: 使用 RTP over RTSP (TCP) (默认关闭),network-caching=300:网络缓存300ms,设置越大延迟越大,太小视频卡顿,300较为合适
//            mediaPlayer.playMedia(Tool.getRTSPAddress(streamURL), options); //播放rtsp流
//            mediaPlayer.start();//停止了哈
////            livePreviewContentPanelList.get(i).setEnabled(false);
//        }
//    }
//}
