package com.dyw.client.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.FaceInfoEntity;
import com.dyw.client.functionForm.FaceBaseFunction;
import com.dyw.client.functionForm.FaceInfoFunction;
import com.dyw.client.service.ImportPersonProgressService;
import com.dyw.client.service.PersonPageSelectionService;
import com.dyw.client.tool.Tool;
import org.json.JSONArray;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PersonManagementForm {
    public JPanel getPersonManagementForm() {
        return personManagementForm;
    }

    private JPanel personManagementForm;
    private JPanel personManagementPanel;
    private JPanel personManagementBasePanel;
    private JPanel personManagementBaseToolBarPanel;
    private JButton personManagementBaseAddButton;
    private JButton personManagementBaseEditButton;
    private JButton personManagementBaseDeleteButton;
    private JPanel personManagementBaseListPanel;
    private JScrollPane personManagementBaseListScroll;
    private JList personManagementBaseList;
    private JPanel personManagementBaseConditionPanel;
    private JPanel personManagementBaseConditionSearchButtonPanel;
    private JPanel personManagementContentPanel;
    private JPanel personManagementContentToolBarPanel;
    private JButton personManagementContentToolBarAddByCardButton;
    private JButton personManagementContentToolBarDeleteButton;
    private JButton personManagementContentToolBarImportButton;
    private JButton personManagementContentToolBarAddButton;
    private JPanel personManagementContentResultPanel;
    private JScrollPane personManagementContentResultScroll;
    private JTable personManagementContentResultTable;
    private JPanel personManagementContentSelectPagePanel;
    private JProgressBar importPersonProgressBar;//进度条
    private JButton firstPageButton;
    private JButton previousPageButton;
    private JButton nextPageButton;

    private DefaultTableModel personManagementContentResultTableModel;
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表
    private List<String> FDLibNames = new ArrayList<>();//人脸库名称列表
    private String FDID;//人脸库ID
    private List<FaceInfoEntity> faceInfoEntityList = new ArrayList<>();//人员列表
    private PersonPageSelectionService personPageSelectionService = new PersonPageSelectionService();
    private int pageNum = 0;

    public PersonManagementForm() {
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
                personManagementContentResultTableModel.setRowCount(0);
                pageNum = 0;//页码归0
                //显示某一个人脸库中的人员信息
                showSelectBase();
            }
        });
        //按卡号添加人员
        personManagementContentToolBarAddByCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFaceByCard();
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
        //首页
        firstPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FDID == null) {
                    Tool.showMessage("请选择一个人脸库", "提示", 0);
                    return;
                }
//                displaySelectResult(personPageSelectionService.firstPage(faceInfoEntityList));
                pageNum = 0;
                showSelectBase();
            }
        });
        //上一页
        previousPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FDID == null) {
                    Tool.showMessage("请选择一个人脸库", "提示", 0);
                    return;
                }
//                displaySelectResult(personPageSelectionService.previousPage(faceInfoEntityList));
                if (pageNum < 1) {
                    Tool.showMessage("已经是第一页", "提示", 0);
                    return;
                }
                pageNum--;
                showSelectBase();
            }
        });
        //下一页
        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FDID == null) {
                    Tool.showMessage("请选择一个人脸库", "提示", 0);
                    return;
                }
//                displaySelectResult(personPageSelectionService.nextPage(faceInfoEntityList));
                pageNum++;
                showSelectBase();
            }
        });
    }

    /*
     * 获取人脸库列表
     * */
    public void getFDLib() {
        try {
            Egci.fdLibMaps.clear();
            fdLibEntityList.clear();
            FDLibNames.clear();
            fdLibEntityList = JSONObject.parseArray(Tool.sendInstructionAndReceiveStatusAndData(1, "/ISAPI/Intelligent/FDLib?format=json", null).getString("FDLib"), FDLibEntity.class);
            for (FDLibEntity fdLibEntity : fdLibEntityList) {
                FDLibNames.add(fdLibEntity.getName());
                Egci.fdLibMaps.put(fdLibEntity.getFDID(), fdLibEntity.getName());
            }
            personManagementBaseList.setListData(FDLibNames.toArray());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    /*
     * 创建人脸库
     * */
    public void addFDLib() {
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, null);
        faceBaseFunction.init();
    }

    /*
     * 修改人脸库
     * */
    private void editFDLib() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        if (personManagementBaseList.getSelectedValue().toString().equals("电厂人员库")) {
            Tool.showMessage("禁止修改此库", "提示", 0);
            return;
        }
        FaceBaseFunction faceBaseFunction = new FaceBaseFunction(this, fdLibEntityList.get(personManagementBaseList.getSelectedIndex()));
        faceBaseFunction.init();
    }

    /*
     * 按卡号添加人脸
     * */
    private void addFaceByCard() {
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
                    if (staffEntity.getBirthday() == null) {
                        staffEntity.setBirthday("1900-01-01");
                    }
                    inboundData.put("bornTime", Tool.judgeBirthdayFormat(staffEntity.getBirthday()));
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

    /*
     * 添加人脸信息
     * */
    private void addFace() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        FaceInfoFunction faceInfoFunction = new FaceInfoFunction(FDID, this);
        faceInfoFunction.init();
    }

    /*
     * 删除人脸库
     * */
    private void deleteFDLib() {
        if (FDID == null) {
            Tool.showMessage("请选择一个人脸库", "提示", 0);
            return;
        }
        System.out.println("选择的库是:" + personManagementBaseList.getSelectedValue());
        if (personManagementBaseList.getSelectedValue().toString().equals("电厂人员库")) {
            Tool.showMessage("禁止删除此库", "提示", 0);
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
    public void showSelectBase() {
        try {
            FDLibEntity fdLibEntity = fdLibEntityList.get(personManagementBaseList.getSelectedIndex());
            FDID = fdLibEntity.getFDID();
            System.out.println("FDID是：" + FDID);
            if (fdLibEntityList.get(personManagementBaseList.getSelectedIndex()) != null) {
                //获取人脸库中的人脸数据
//                String inboundData = "{\"searchResultPosition\":0,\"maxResults\":100,\"faceLibType\":\"blackFD\",\"FDID\":\"" + FDID + "\"}";
                String instruction = "/ISAPI/Intelligent/FDLib/FDSearch?format=json";
                org.json.JSONObject inboundData = new org.json.JSONObject();
                inboundData.put("searchResultPosition", pageNum);
                inboundData.put("maxResults", 50);
                inboundData.put("faceLibType", "blackFD");
                inboundData.put("FDID", FDID);
                faceInfoEntityList.clear();
                faceInfoEntityList = JSON.parseArray(Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData).getString("MatchList"), FaceInfoEntity.class);
                displaySelectResult(faceInfoEntityList);
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | JSONException ignored) {

        }
    }

    /*
     * 分页显示结果
     * */
    public void displaySelectResult(List<FaceInfoEntity> faceInfoEntityList) {
        personManagementContentResultTableModel.setRowCount(0);
        for (FaceInfoEntity faceInfoEntity : faceInfoEntityList) {
            if (faceInfoEntity.getName().contains("_")) {//显示卡号
                StaffEntity staffEntity = Tool.splitNameAndGetStaff(faceInfoEntity.getName());
                Vector v = new Vector();
                v.add(0, staffEntity.getName());
                v.add(1, faceInfoEntity.getGender());
                v.add(2, faceInfoEntity.getBornTime());
                v.add(3, staffEntity.getCardNumber());
                personManagementContentResultTableModel.addRow(v);
            } else {//没有卡号
                Vector v = new Vector();
                v.add(0, faceInfoEntity.getName());
                v.add(1, faceInfoEntity.getGender());
                v.add(2, faceInfoEntity.getBornTime());
                v.add(3, "");
                personManagementContentResultTableModel.addRow(v);
            }
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
        Thread thread = new ImportPersonProgressService(importPersonProgressBar, this, FDID, personManagementContentToolBarImportButton);
        thread.start();
        personManagementContentToolBarImportButton.setEnabled(false);
    }

}
