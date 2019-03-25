package com.dyw.client.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.*;
import com.dyw.client.functionForm.EquipmentFunction;
import com.dyw.client.functionForm.FaceBaseFunction;
import com.dyw.client.tool.Tool;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ProtectionForm {
    private Logger logger = LoggerFactory.getLogger(ProtectionForm.class);
    private DefaultTableModel deviceManagementContentTableModel;
    private DefaultTableModel personManagementContentResultTableModel;
    //    private List<FDLibEntity> libEntityList;//人脸库列表
    private List<String> FDLibNames;//人脸库名称列表
    private List<FaceInfoEntity> faceInfoEntityList;//人员列表
    private List<FDLibEntity> fdLibEntityList;//人脸库列表
    private CtrlCenterEntity ctrlCenterEntity;//根控制中心
    private List<RegionEntity> regionEntityList;//区域列表
    private String FDID;//人脸库ID
    private List<DeviceEntity> deviceEntityList;//设备列表
    private int equipmentStatus;//资源管理状态：主要用来执行删除操作时做判断
    private List<MonitorPointEntity> monitorPointEntityList;//监控点列表

    private JPanel protection;
    private JTabbedPane tabbedPane1;
    private JPanel intelligentApplicationPanel;
    private JPanel personManagementPanel;
    private JPanel monitorManagementPanel;
    private JPanel resourceManagementPanel;
    private JPanel resourceManagementTitlePanel;
    private JPanel resourceManagementContentPanel;
    private JScrollPane resourceManagementContentScroll;
    private JTable resourceManagementContentTable;
    private JButton allEquipmentButton;
    private JButton oneMonitoryPointButton;
    private JButton twoMonitoryPointButton;
    private JButton threeMonitoryPointButton;
    private JButton addEquipmentButton;
    private JButton addMonitoryPointButton;
    private JButton deleteButton;
    private JPanel personManagementContentPanel;
    private JPanel personManagementBasePanel;
    private JPanel personManagementBaseToolBarPanel;
    private JPanel personManagementBaseListPanel;
    private JPanel personManagementBaseConditionPanel;
    private JPanel personManagementBaseConditionSearchButtonPanel;
    private JButton personManagementBaseAddButton;
    private JButton personManagementBaseEditButton;
    private JButton personManagementBaseDeleteButton;
    private JScrollPane personManagementBaseListScroll;
    private JList personManagementBaseList;
    private JPanel personManagementContentToolBarPanel;
    private JPanel personManagementContentResultPanel;
    private JPanel personManagementContentSelectPagePanel;
    private JButton personManagementContentToolBarAddButton;
    private JButton personManagementContentToolBarDeleteButton;
    private JTable personManagementContentResultTable;
    private JScrollPane personManagementContentResultScroll;
    private JButton personManagementContentToolBarImportButton;

    /*
     * 构造函数
     * */
    public ProtectionForm() {
        try {
            //获取根控制中心
            ctrlCenterEntity = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter?source=device", null).getString("ctrlCenter"), CtrlCenterEntity.class).get(0);
            //获取区域列表
            regionEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID(), null).getString("region"), RegionEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        equipmentStatus = 0;
        fdLibEntityList = new ArrayList<>();
        faceInfoEntityList = new ArrayList<>();
        FDLibNames = new ArrayList<>();
        deviceEntityList = new ArrayList<>();
//        libEntityList = new ArrayList<>();
        monitorPointEntityList = new ArrayList<>();
        FDID = null;
        /*
         * 资源管理
         * */
        //初始化全部设备表格
        deviceManagementContentTableModel = new DefaultTableModel();
        //获取全部设备
        allEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDevice();
            }
        });
        //添加设备
        addEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEquipment();
            }
        });
        //删除设备或监控点
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEquipment();
            }
        });
        //获取一核监控点
        oneMonitoryPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMonitor(regionEntityList.get(0));
            }
        });
        //添加监控点
        addMonitoryPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMonitor();
            }
        });
        /*
         * 名单管理
         * */
        //初始化名单库人脸显示列表
        String[] columnPersonManagementContentResultInfo = {"姓名", "性别", "出生日期", "卡号"};
        personManagementContentResultTableModel = new DefaultTableModel();
        personManagementContentResultTableModel.setColumnIdentifiers(columnPersonManagementContentResultInfo);
        personManagementContentResultTable.setModel(personManagementContentResultTableModel);
        //获取人脸库列表
        getFDLib();
        //添加人脸库
        personManagementBaseAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFDLib();
            }
        });
        //修改人脸库
        personManagementBaseEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFDLib();
            }
        });
        //选择某一个人脸库
        personManagementBaseList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                //显示某一个人脸库中的人员信息
                showSelectBase();
            }
        });
        //添加人员
        personManagementContentToolBarAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFace();
            }
        });
        //删除人脸库
        personManagementBaseDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFDLib();
            }
        });
        //删除人员信息
        personManagementContentToolBarDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFace();
            }
        });
        //导入全部人员
        personManagementContentToolBarImportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importAllFace();
            }
        });
    }

    //=====================================================================================
    /*
     *名单管理
     * */
    //获取人脸库列表
    public void getFDLib() {
        try {
            fdLibEntityList.clear();
            FDLibNames.clear();
            fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                FDLibNames.add(fdLibEntity.getName());
            }
            System.out.println(faceInfoEntityList);
            personManagementBaseList.setListData(FDLibNames.toArray());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    //创建人脸库
    public void addFDLib() {
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, null);
        faceBaseFunction.init();
    }

    //修改人脸库
    private void editFDLib() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, fdLibEntityList.get(personManagementBaseList.getSelectedIndex()));
        faceBaseFunction.init();
    }

    //添加人脸
    private void addFace() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        String cardNumber = JOptionPane.showInputDialog(null, "请输入卡号", "添加新用户", 1);
        org.json.JSONObject inboundData = new org.json.JSONObject();
        //从数据库查询人员信息
        try {
            if (cardNumber != null) {
                StaffEntity staffEntity = Egci.session.selectOne("mapping.staffMapper.getResultStaffWithCard", cardNumber);
                if (staffEntity == null) {
                    Tool.showMessage("人员不存在", "提示", 0);
                } else {
                    //第一步：先将人员图片发送到脸谱服务器，获取faceURL
                    org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, FDID, staffEntity.getPhoto(), null);
                    if (resultFaceUrlData == null) {
                        Tool.showMessage("添加失败", "提示", 0);
                        return;
                    } else {
                        inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
                    }
                    String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
                    inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
                    inboundData.put("faceLibType", "blackFD");
                    inboundData.put("FDID", FDID);
                    inboundData.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
                    inboundData.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
                    inboundData.put("bornTime", staffEntity.getBirthday());
                    org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
                    if (resultData.getInt("statusCode") == 1) {
                        Tool.showMessage("添加成功", "提示", 0);
                        showSelectBase();
                    } else {
                        Tool.showMessage("添加失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
                    }
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    //删除人脸库
    private void deleteFDLib() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        try {
            if (!Tool.showConfirm("确认删除", "删除提示")) {
                return;
            }
            String instruction = "/ISAPI/Intelligent/FDLib?format=json&FDID=" + fdLibEntityList.get(personManagementBaseList.getSelectedIndex()).getFDID() + "&faceLibType=blackFD";
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(4, instruction, null);
            if (resultData.getInt("statusCode") == 1) {
                Tool.showMessage("删除成功", "提示", 0);
                getFDLib();
            } else {
                Tool.showMessage("删除失败", "提示", 0);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    /*
     * 显示选定人脸库中的人员信息
     * */
    private void showSelectBase() {
        personManagementContentResultTableModel.setRowCount(0);
        FDLibEntity fdLibEntity = fdLibEntityList.get(personManagementBaseList.getSelectedIndex());
        FDID = fdLibEntity.getFDID();
        try {
            if (fdLibEntityList.get(personManagementBaseList.getSelectedIndex()) != null) {
                //获取人脸库中的人脸数据
//                String inboundData = "{\"searchResultPosition\":0,\"maxResults\":100,\"faceLibType\":\"blackFD\",\"FDID\":\"" + FDID + "\"}";
                String instruction = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
                org.json.JSONObject inboundData = new org.json.JSONObject();
                inboundData.put("searchResultPosition", 0);
                inboundData.put("maxResults", 100);
                inboundData.put("faceLibType", "blackFD");
                inboundData.put("FDID", FDID);
                faceInfoEntityList.clear();
                faceInfoEntityList = JSON.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData).getString("MatchList"), FaceInfoEntity.class);
                for (FaceInfoEntity faceInfoEntity : faceInfoEntityList) {
                    StaffEntity staffEntity = Tool.splitNameAndGetStaff(faceInfoEntity.getName());
                    Vector v = new Vector();
                    v.add(0, staffEntity.getName());
                    v.add(1, faceInfoEntity.getGender());
                    v.add(2, faceInfoEntity.getBornTime());
                    v.add(3, staffEntity.getCardNumber());
                    personManagementContentResultTableModel.addRow(v);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | JSONException ignored) {

        }
    }

    /*
     * 删除人员信息
     * */
    private void deleteFace() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        if (Tool.showConfirm("是否删除人员", "提示")) {
            org.json.JSONObject deleteInboundData = new org.json.JSONObject();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("value", faceInfoEntityList.get(personManagementContentResultTable.getSelectedRow()).getFPID());
            JSONArray jsonarry = new JSONArray();
            try {
                jsonarry.put(0, map);
                deleteInboundData.put("FPID", jsonarry);
                org.json.JSONObject resultData = Tool.faceInfoOperation(2, FDID, null, deleteInboundData);
                if (resultData.getInt("statusCode") == 1) {
                    Tool.showMessage("删除成功", "提示", 0);
                    showSelectBase();
                } else {
                    Tool.showMessage("删除失败", "提示", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 导入全部人脸信息
     * */
    private void importAllFace() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        List<StaffEntity> staffEntityList = Egci.session.selectList("mapping.staffMapper.getAllStaff");
        for (StaffEntity staffEntity : staffEntityList) {
            String instruction = "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, FDID, staffEntity.getPhoto(), null);
            org.json.JSONObject inboundData = new org.json.JSONObject();
            try {
                inboundData.put("faceURL", resultFaceUrlData.getString("URL"));
                inboundData.put("faceLibType", "blackFD");
                inboundData.put("FDID", FDID);
                inboundData.put("name", staffEntity.getName() + "_" + staffEntity.getCardNumber() + "_" + staffEntity.getStaffId());//名字_卡号_id
                inboundData.put("gender", Tool.changeGenderToMaleAndFemale(staffEntity.getSex()));
                inboundData.put("bornTime", staffEntity.getBirthday());
                org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(3, instruction, inboundData);
                showSelectBase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //==================================================================================
    /*
     * 资源管理
     * */
    /*
     * 获取全部设备列表
     * */
    public void getDevice() {
        deviceEntityList.clear();
        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device/search";
        org.json.JSONObject inboundData = new org.json.JSONObject();
        try {
            String[] columnResourceManagementInfo = {"设备名称", "类型", "IP地址", "端口"};
            deviceManagementContentTableModel.setColumnIdentifiers(columnResourceManagementInfo);
            inboundData.put("searchResultPosition", 0);
            inboundData.put("maxResults", 100);
            String resultDevice = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData).getString("ctrlCenter");
            deviceEntityList = JSONObject.parseArray(new org.json.JSONObject(resultDevice).getString("device"), DeviceEntity.class);
            resourceManagementContentTable.setModel(deviceManagementContentTableModel);
            deviceManagementContentTableModel.setRowCount(0);
            for (DeviceEntity deviceEntity : deviceEntityList) {
                Vector vector = new Vector();
                vector.add(0, deviceEntity.getDeviceName());
                vector.add(1, deviceEntity.getDeviceType());
                vector.add(2, deviceEntity.getDeviceIP());
                vector.add(3, deviceEntity.getDevicePort());
                deviceManagementContentTableModel.addRow(vector);
            }
            equipmentStatus = 1;
        } catch (JSONException e) {
            Tool.showMessage("获取设备失败或没有添加设备", "提示", 0);
        }
    }

    /*
     * 添加设备
     * */
    private void addEquipment() {
        EquipmentFunction equipmentFunction = new EquipmentFunction(ctrlCenterEntity, this);
        equipmentFunction.init();
    }

    /*
     * 删除设备或监控点
     * */
    private void deleteEquipment() {
        if (resourceManagementContentTable.getSelectedRow() == -1) {
            Tool.showMessage("请先选择一个设备或监控点", "提示", 0);
            return;
        }
        if (!Tool.showConfirm("确认删除", "提示")) {
            return;
        }
        if (equipmentStatus == 1) {
            String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/Device/delete";
            org.json.JSONObject inboundData = new org.json.JSONObject();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("DeviceID", deviceEntityList.get(resourceManagementContentTable.getSelectedRow()).getDeviceID());
            JSONArray jsonarry = new JSONArray();
            try {
                jsonarry.put(0, map);
                inboundData.put("Device", jsonarry);
                org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatus(2, instruction, inboundData);
                if (resultData.getInt("statusCode") == 1) {
                    Tool.showMessage("删除成功", "提示", 0);
                    getDevice();
                } else {
                    Tool.showMessage("删除失败，错误码：" + resultData.getInt("statusCode"), "提示", 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //获取监控点信息
    private void getMonitor(RegionEntity regionEntity) {
        String instruction = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/region/" + regionEntity.getRegionID() + "/monitorPoint/search";
        org.json.JSONObject inboundData = new org.json.JSONObject();
        try {
            String[] columnResourceMonitorInfo = {"监控点名称", "相机类型", "IP地址", "端口", "布防状态"};
            deviceManagementContentTableModel.setColumnIdentifiers(columnResourceMonitorInfo);
            inboundData.put("searchResultPosition", 0);
            inboundData.put("maxResults", 100);
            inboundData.put("isRegion", "yes");
            org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
            monitorPointEntityList = JSONObject.parseArray(new org.json.JSONObject(resultData.getString("region")).getString("monitorPoint"), MonitorPointEntity.class);
            System.out.println("一核监控点信息" + monitorPointEntityList.get(0).getMonitorPointName());
        } catch (JSONException e) {
            Tool.showMessage("获取监控点失败或没有添加监控点", "提示", 0);
        }
    }

    /*
     * 添加监控点
     * */
    private void addMonitor() {
        //获取监控点列表
        String instructionGet = "/ISAPI/SDT/Management/CtrlCenter/" + ctrlCenterEntity.getCtrlCenterID() + "/monitorPoint/search";
        org.json.JSONObject inboundDataGet = new org.json.JSONObject();
        try {
            inboundDataGet.put("searchResultPosition", 0);
            inboundDataGet.put("maxResults", 100);
//            inboundDataGet.put("isRegion", "no");
            org.json.JSONObject resultDataGet = Tool.sendInstructionAndReceiveStatusAndData(3, instructionGet, inboundDataGet);
            Thread.sleep(2000);
            List<MonitorPointEntity> monitorPointEntities = JSONObject.parseArray(new org.json.JSONObject(resultDataGet.getString("ctrlCenter")).getString("monitorPoint"), MonitorPointEntity.class);
//            List<MonitorPointEntity> monitorPointEntities = JSONObject.parseArray(resultDataGet.getString("monitorPoint"), MonitorPointEntity.class);
            System.out.println("监控点数量：" + monitorPointEntities.size());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void init() {
        JFrame frame = new JFrame("ProtectionForm");
        frame.setContentPane(this.protection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
