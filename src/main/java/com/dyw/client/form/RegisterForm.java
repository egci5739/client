package com.dyw.client.form;

import ISAPI.HttpsClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.CollectionEntity;
import com.dyw.client.entity.FaceCollectionEntity;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.FaceInfoEntity;
import com.dyw.client.service.*;
import com.dyw.client.timer.PingTimer;
import com.dyw.client.tool.Tool;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;

public class RegisterForm {
    private Logger logger = LoggerFactory.getLogger(RegisterForm.class);
    private List<String> cardNumbers;
    private Map<String, StaffEntity> waitStaffMap;
    private DefaultTableModel model;
    private StaffEntity oldStaff;
    private byte[] staffPhoto;
    private byte[] takePhoto;
    private StaffOperationService staffOperationService;
    private int addWaitStaffStatus = 0;

    private JPanel main;
    private JPanel waitStaffPanel;
    private JPanel staffInfoPanel;
    private JPanel identityInfoPanel;
    private JPanel staffInfoTitlePanel;
    private JPanel staffInfoContentPanel;
    private JPanel staffInfoButtonPanel;
    private JPanel staffInfoResultPanel;
    private JLabel chineseNameLabel;
    private JTextField chineseNameText;
    private JTextField englishNameText;
    private JTextField passCardText;
    private JTextField IdNumberText;
    private JTextField birthdayText;
    private JLabel englishNameLabel;
    private JLabel passCardLabel;
    private JLabel IdNumberLabel;
    private JLabel birthdayLabel;
    private JLabel IdPhotoLabel;
    private JLabel sexLabel;
    private JLabel companyLabel;
    private JTextField sexText;
    private JTextField companyText;
    private JButton searchButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JCheckBox useIdCardCheckBox;
    private JTable resultTable;
    private JScrollPane resultScrollPane;
    private JPanel photoPanel;
    private JLabel IdPhoto;
    private JPanel waitStaffTitlePanel;
    private JPanel waitStaffListPanel;
    private JList waitStaffList;
    private JPanel waitStaffButtonPanel;
    private JButton refreshButton;
    private JPanel identityTitlePanel;
    private JPanel takePhotoPanel;
    private JPanel changePhotoButtonPanel;
    private JButton changePhotoButton;
    private JPanel identityPhotoPanel;
    private JPanel identityDetailPanel;
    private JLabel takePhotoLabel;
    private JLabel idSimilarityLabel;
    private JLabel idNameLabel;
    private JLabel idCardNumberLabel;
    private JLabel idNationLabel;
    private JLabel idSexLabel;
    private JLabel idBirthdayLabel;
    private JLabel idValidityPeriodLabel;
    private JLabel idOrganizationLabel;
    private JLabel identityPhotoLabel;
    private JButton communicationStatusButton;
    private JScrollPane waitStaffScroll;
    private JButton addWaitStaffButton;
    private StaffEntity staffEntity;

    public RegisterForm() {
        //获取采集设备的ip
        try {
            FaceCollectionEntity faceCollectionEntity = Egci.session.selectOne("mapping.faceCollectionMapper.getFaceCollectionWithHostIp", InetAddress.getLocalHost().getHostAddress());
            if (faceCollectionEntity == null) {
                JOptionPane.showMessageDialog(null, "该主机未绑定采集设备", "错误", 0);
                Egci.configEntity.setFaceCollectionIp("0.0.0.0");
            } else {
                Egci.configEntity.setFaceCollectionIp(faceCollectionEntity.getFaceCollectionIp());
            }
        } catch (UnknownHostException e) {
            logger.error("获取采集设备ip出错", e);
        }
        //创建接收采集信息的socket对象
        RegisterReceiveInfoSocketService registerReceiveInfoSocketService = new RegisterReceiveInfoSocketService(this);
        registerReceiveInfoSocketService.sendInfo("7#" + Egci.configEntity.getFaceCollectionIp());
        registerReceiveInfoSocketService.start();
        //初始化人员操作对象
        staffOperationService = new StaffOperationService();
        //初始化拍照模式切换对象
        //初始化按钮状态
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(false);
        waitStaffList.setEnabled(false);
        changePhotoButton.setEnabled(false);
        inputDisabled();
        //初始化结果表
        String[] columnNames = {"通行卡号", "中文名称", "证件号码"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        resultTable.setModel(model);
        //获取待拍照人员列表
        staffOperationService.getWaitStaffList();
        waitStaffList.setListData(staffOperationService.getCardNumbers().toArray());
        //getWaitStaffList();
        //点击待拍照人员后自动填充人员信息
        waitStaffList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (staffOperationService.getWaitStaffMap().get(waitStaffList.getSelectedValue()) != null) {
                    fillStaffInfo(staffOperationService.getWaitStaffMap().get(waitStaffList.getSelectedValue()));
                }
            }
        });
        //重新加载待拍照人员列表
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //获取待拍照人员列表
                staffOperationService.getWaitStaffList();
                waitStaffList.setListData(staffOperationService.getCardNumbers().toArray());
            }
        });
        //取消
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        //删除
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        //新增
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        //保存
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        //修改
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        //搜索
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        //将搜索结果加载到人员信息中
        resultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    chineseNameText.setEnabled(false);
                    passCardText.setEnabled(false);
                    IdPhoto.setEnabled(true);
                    if (staffOperationService.getResultStaffMap().get(resultTable.getSelectedRow() + "") != null) {
                        staffPhoto = staffOperationService.getResultStaffMap().get(resultTable.getSelectedRow() + "").getPhoto();
                        fillStaffInfo(staffOperationService.getResultStaffMap().get(resultTable.getSelectedRow() + ""));
                        oldStaff = staffOperationService.getResultStaffMap().get(resultTable.getSelectedRow() + "");
                    }
                } catch (Exception e1) {
                    logger.error("将搜索结果加载到人员信息出错", e1);
//                    model.setRowCount(0);
                }
            }
        });
        //替换旧照片
        changePhotoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePhoto();
            }
        });
        //切换拍照模式
        useIdCardCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JCheckBox jcb = (JCheckBox) e.getItem();
                // 判断是否被选择
                if (jcb.isSelected()) {
                    ChangeModeService changeModeService = null;
                    changeModeService = new ChangeModeService();
                    changeModeService.sendInfo("6#" + Egci.configEntity.getFaceCollectionIp() + "#0\n");
                    changeModeService.receiveInfoOnce();
                } else {
                    ChangeModeService changeModeService = null;
                    changeModeService = new ChangeModeService();
                    changeModeService.sendInfo("6#" + Egci.configEntity.getFaceCollectionIp() + "#1\n");
                    changeModeService.receiveInfoOnce();
                }
            }
        });
        //重新连接服务程序
        communicationStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reconnectToServer();
            }
        });
        //点击新增待拍照人员
        addWaitStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWaitStaff();
            }
        });
    }

    //新增待拍照人员
    private void addWaitStaff() {
        if (addWaitStaffStatus == 0) {
            inputEnable();
            searchButton.setEnabled(false);
            addButton.setEnabled(false);
            addWaitStaffButton.setText("确认");
            addWaitStaffStatus = 1;
        } else {
            if (JOptionPane.showConfirmDialog(null, "确定要保存吗？", "保存提示", 0) == 0) {
                StaffEntity staffEntity = getStaffEntity();
                if (staffEntity.getName().equals("") || staffEntity.getCardNumber().equals("")) {
                    JOptionPane.showMessageDialog(null, "中文名或卡号缺失！", "错误 ", 0);
                    return;
                } else {
                    if (staffOperationService.addWaitStaff(staffEntity)) {
                        cancel();
                        addWaitStaffButton.setText("新增人员");
                        staffOperationService.getWaitStaffList();
                        waitStaffList.setListData(staffOperationService.getCardNumbers().toArray());
                        addWaitStaffStatus = 0;
                    } else {
                        JOptionPane.showMessageDialog(null, "卡号已经存在！", "错误 ", 0);
                    }
                }
            }
        }
    }

    /*
     * 重新连接到服务程序
     * */
    private void reconnectToServer() {
        if (JOptionPane.showConfirmDialog(null, "确定重新连接到服务程序吗？", "重连提示", 0) == 0) {
            RegisterReceiveInfoSocketService registerReceiveInfoSocketService = new RegisterReceiveInfoSocketService(this);
            registerReceiveInfoSocketService.sendInfo("7#" + Egci.configEntity.getFaceCollectionIp());
            registerReceiveInfoSocketService.start();
            useIdCardCheckBox.setSelected(true);
        }
    }

    /*
     * 取消操作
     * */
    private void cancel() {
        cleanStaffInfo();
        waitStaffList.setEnabled(false);
        inputDisabled();
        saveButton.setEnabled(false);
        searchButton.setEnabled(true);
        addButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        chineseNameText.setEnabled(true);
        passCardText.setEnabled(true);
        changePhotoButton.setEnabled(false);
        addWaitStaffButton.setText("新增人员");
        addWaitStaffStatus = 0;
        addWaitStaffButton.setEnabled(true);
    }

    /*
     * 删除人员
     * */
    private void delete() {
        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
            StaffEntity staffEntity = new StaffEntity();
            staffEntity.setCardNumber(passCardText.getText());
            staffOperationService.delete(staffEntity);
            cancel();
        }
    }

    /*
     * 修改人员信息
     * */
    private void update() {
        System.out.println("旧卡号为：" + oldStaff.getCardNumber());
        inputEnable();
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        updateButton.setEnabled(false);
        chineseNameText.setEnabled(true);
        passCardText.setEnabled(true);
        changePhotoButton.setEnabled(true);
    }

    //初始化页面
    public void init() {
        //启用ping功能
        PingTimer pingTimer = new PingTimer(this);
        pingTimer.open();
        JFrame frame = new JFrame("RegisterForm");
        frame.setContentPane(this.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //点击回车键搜索
        frame.getRootPane().setDefaultButton(searchButton);
        frame.setVisible(true);
        //登陆脸谱服务器
        HttpsClientUtil.httpsClientInit(Egci.configEntity.getFaceServerIp(), Egci.configEntity.getFaceServerPort(), "admin", "hik12345");
        //登录校验代码
        String strUrl = "/ISAPI/Security/userCheck";
        String strOut = "";
        strOut = HttpsClientUtil.httpsGet("https://" + Egci.configEntity.getFaceServerIp() + ":" + Egci.configEntity.getFaceServerPort() + strUrl);
        logger.info(strOut);
        //解析返回的xml文件
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new ByteArrayInputStream(strOut.getBytes("UTF-8")));
            Element employees = document.getRootElement();
            for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                if (employee.getName() == "statusValue" && 0 == employee.getText().compareTo("200")) {
                    JOptionPane.showMessageDialog(null, "连接脸谱服务器成功", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            //登陆失败
            JOptionPane.showMessageDialog(null, "连接脸谱服务器失败", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException e) {
            JOptionPane.showMessageDialog(null, "连接脸谱服务器失败", "Error", JOptionPane.ERROR_MESSAGE);
            logger.error("登陆脸谱服务器失败", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("登陆脸谱服务器失败", e);
        }
    }

    /*
     * 将人员信息填入人员表单中
     * */
    public void fillStaffInfo(StaffEntity staffEntity) {
        try {
            chineseNameText.setText(staffEntity.getName());
            englishNameText.setText(staffEntity.getNameEn());
            passCardText.setText(staffEntity.getCardNumber());
            IdNumberText.setText(staffEntity.getCardId());
            birthdayText.setText(staffEntity.getBirthday());
            sexText.setText(staffEntity.getSex());
            companyText.setText(staffEntity.getCompany());
            try {
                staffEntity.setPhoto(staffEntity.getPhoto());
            } catch (Exception e) {
//                logger.error("获取表格人员信息出错", e);
                staffEntity.setPhoto(null);
            }
            try {
                ImageIcon imageIcon = new ImageIcon(staffEntity.getPhoto());
                IdPhoto.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), photoPanel.getHeight(), 2));
            } catch (Exception e) {
                IdPhoto.setIcon(null);
            }
        } catch (Exception e) {
            logger.error("将人员信息填入人员表单中出错", e);
            IdPhoto.setIcon(null);
//            model.setRowCount(0);
        }
    }

    /*
     * 将采集的信息填入身份信息表中
     * */
    public void fillCollectionInfo(CollectionEntity collectionEntity) {
        try {
            idSimilarityLabel.setText(collectionEntity.getSimilation());
            idNameLabel.setText(collectionEntity.getName());
            idCardNumberLabel.setText(collectionEntity.getCardId());
            idNationLabel.setText(collectionEntity.getNation());
            idSexLabel.setText(collectionEntity.getSex());
            idBirthdayLabel.setText(collectionEntity.getBirthday());
            idValidityPeriodLabel.setText(collectionEntity.getExpirationDate());
            idOrganizationLabel.setText(collectionEntity.getOrganization());
            takePhoto = collectionEntity.getStaffPhoto();
            try {
                ImageIcon imageIcon = new ImageIcon(collectionEntity.getStaffPhoto());
                takePhotoLabel.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), photoPanel.getWidth(), 1));
            } catch (Exception e) {
                IdPhoto.setIcon(null);
            }
        } catch (Exception e) {
            logger.error("将采集的信息填入身份信息表出错", e);
        }
    }

    /*
     * 清空人员信息表单
     * */
    public void cleanStaffInfo() {
        try {
            chineseNameText.setText("");
            englishNameText.setText("");
            passCardText.setText("");
            IdNumberText.setText("");
            birthdayText.setText("");
            sexText.setText("");
            companyText.setText("");
            idSimilarityLabel.setText("");
            idNameLabel.setText("");
            idCardNumberLabel.setText("");
            idNationLabel.setText("");
            idSexLabel.setText("");
            idBirthdayLabel.setText("");
            idValidityPeriodLabel.setText("");
            idOrganizationLabel.setText("");
            IdPhoto.setIcon(null);
            takePhotoLabel.setIcon(null);
            model.setRowCount(0);
            staffPhoto = null;//清空缓存的图片
            takePhoto = null;//清空缓存的图片
        } catch (Exception e) {
            logger.error("清空表格出错", e);
        }
    }

    /*
     * 获取表格人员信息
     * */
    public StaffEntity getStaffEntity() {
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setName(chineseNameText.getText());
        staffEntity.setNameEn(englishNameText.getText());
        staffEntity.setCardNumber(passCardText.getText());
        staffEntity.setCardId(IdNumberText.getText());
        staffEntity.setBirthday(birthdayText.getText());
        staffEntity.setSex(sexText.getText());
        staffEntity.setCompany(companyText.getText());
        try {
            staffEntity.setPhoto(staffPhoto);
        } catch (Exception e) {
            logger.error("获取表格人员信息出错", e);
            staffEntity.setPhoto(null);
        }
        return staffEntity;
    }

    /*
     * 新增人员
     * */
    public void add() {
        waitStaffList.setEnabled(true);
        inputEnable();
        searchButton.setEnabled(false);
        saveButton.setEnabled(true);
        addButton.setEnabled(false);
        changePhotoButton.setEnabled(true);
        addWaitStaffButton.setEnabled(false);
    }

    /*
     * 保存人员信息
     * */
    public void save() {
        if (JOptionPane.showConfirmDialog(null, "确定要保存吗？", "保存提示", 0) == 0) {
            StaffEntity staffEntity = getStaffEntity();
            staffEntity.setPhoto(staffPhoto);
            if (staffEntity.getName().equals("") || staffEntity.getCardNumber().equals("") || staffEntity.getPhoto() == null) {
                JOptionPane.showMessageDialog(null, "中文名、卡号或照片缺失！", "错误 ", 0);
                return;
            } else {
                staffOperationService.save(staffEntity, oldStaff);
                cancel();
            }
        }
    }

    /*
     * 查询人员
     * */
    public void search() {
        if (chineseNameText.getText().equals("") && passCardText.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入搜索条件！", "错误 ", 0);
            return;
        }
        searchButton.setEnabled(false);
        addButton.setEnabled(false);
        saveButton.setEnabled(false);
        model.setRowCount(0);
        staffOperationService.search(chineseNameText.getText(), passCardText.getText());
        for (Vector v : staffOperationService.getVectorList()) {
            model.addRow(v);
        }
        addWaitStaffButton.setEnabled(false);
    }

    /*
     * 替换旧照片
     * */
    private void changePhoto() {
        IdPhoto.setIcon(null);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ImageIcon imageIcon = new ImageIcon(takePhoto);
            IdPhoto.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), photoPanel.getHeight(), 2));
            staffPhoto = takePhoto;
        } catch (Exception e) {
            IdPhoto.setIcon(null);
        }
    }

    /*
     * 禁用表格输入
     * */
    public void inputDisabled() {
        englishNameText.setEnabled(false);
        IdNumberText.setEnabled(false);
        birthdayText.setEnabled(false);
        sexText.setEnabled(false);
        companyText.setEnabled(false);
        IdPhoto.setEnabled(false);
    }

    /*
     * 启用表格输入
     * */
    public void inputEnable() {
        englishNameText.setEnabled(true);
        IdNumberText.setEnabled(true);
        birthdayText.setEnabled(true);
        sexText.setEnabled(true);
        companyText.setEnabled(true);
        IdPhoto.setEnabled(true);
    }

    /*
     * 更改通信状态
     * */
    public void changeCommunicationStatus(int status) {
        /*
         *状态说明：
         * 0：通信正常
         * 1：服务程序断开
         * 2：服务器网络异常
         * 3：采集设备网络异常
         * */
        switch (status) {
            case 0:
                communicationStatusButton.setBackground(Color.green);
                communicationStatusButton.setText("通信正常");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(true);
                break;
            case 1:
                communicationStatusButton.setBackground(Color.red);
                communicationStatusButton.setText("服务程序断开");
                communicationStatusButton.setEnabled(true);
                useIdCardCheckBox.setEnabled(false);
                break;
            case 2:
                communicationStatusButton.setBackground(Color.red);
                communicationStatusButton.setText("网络异常");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(false);
                break;
            default:
                break;
        }
    }
}
