package com.dyw.client.form;

import com.alibaba.fastjson.JSONObject;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.entity.FaceCollectionEntity;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.service.*;
import com.dyw.client.timer.PingTimer;
import com.dyw.client.tool.Tool;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterForm {
    private Logger logger = LoggerFactory.getLogger(RegisterForm.class);
    private List<String> cardNumbers;
    private Map<String, StaffEntity> waitStaffMap;
    private DefaultTableModel model;
    private StaffEntity oldStaff = new StaffEntity();
    private byte[] staffPhoto;
    private byte[] takePhoto;
    private StaffOperationService staffOperationService;
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表
    private DefaultTableModel waitStaffModel;
    //    private List<StaffEntity> waitStaffList = new ArrayList<>();
    private List<StaffEntity> resultStaffList = new ArrayList<>();
    private List<StaffEntity> resultWaitStaffList = new ArrayList<>();
    private static String cardNumberPattern = "^[1-9]\\d*$";//卡号正则表达式
    private int operationCode;//操作码：1：新增；2：修改

    public JPanel getRegisterForm() {
        return registerForm;
    }

    private JPanel registerForm;
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
    private JComboBox sexSelectionCombo;
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
    private JTable waitStaffTable;//待拍照人员列表
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
    private JButton choseLocalPictureButton;
    private JButton takePhotoButton;
    private StaffEntity staffEntity;

    public RegisterForm() {
        //获取人脸库
        if (Egci.faceServerStatus == 1) {
            getFDLib();
        }
        //初始化性别选择框
        sexSelectionCombo.addItem("未知");
        sexSelectionCombo.addItem("男");
        sexSelectionCombo.addItem("女");
        //获取采集设备的ip
        try {
            EquipmentEntity equipmentEntity = Egci.session.selectOne("mapping.equipmentMapper.getFaceCollectionWithHostIp", InetAddress.getLocalHost().getHostAddress());
            if (equipmentEntity == null) {
                JOptionPane.showMessageDialog(null, "该主机未绑定采集设备", "错误", 0);
                Egci.configEntity.setFaceCollectionIp("0.0.0.0");
            } else {
                Egci.configEntity.setFaceCollectionIp(equipmentEntity.getEquipmentIp());
            }
        } catch (UnknownHostException e) {
            logger.error("获取采集设备ip出错", e);
        } catch (TooManyResultsException e) {
            logger.error(e.getMessage(), e);
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
        waitStaffTable.setEnabled(false);
        changePhotoButton.setEnabled(false);
        inputDisabled();
        //初始化查询结果表
        String[] columnNames = {"通行卡号", "中文名称", "证件号码"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        resultTable.setModel(model);
        //获取待拍照人员列表
        String[] columnWaits = {"姓名", "卡号"};
        waitStaffModel = new DefaultTableModel();
        waitStaffModel.setColumnIdentifiers(columnWaits);
        waitStaffTable.setModel(waitStaffModel);
        getWaitStaff();
        //点击待拍照人员后自动填充人员信息
        waitStaffTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                try {
                    if (resultWaitStaffList.get(waitStaffTable.getSelectedRow()) != null) {
                        fillStaffInfo(resultWaitStaffList.get(waitStaffTable.getSelectedRow()));
                        oldStaff = resultWaitStaffList.get(waitStaffTable.getSelectedRow());
                        operationCode = 2;//将从待拍照人员表中添加的人定为修改
                    }
                } catch (Exception e1) {
                }
            }
        });
        //重新加载待拍照人员列表
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWaitStaff();
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
                    addButton.setEnabled(false);
                    if (resultStaffList.get(resultTable.getSelectedRow()) != null) {
                        staffPhoto = resultStaffList.get(resultTable.getSelectedRow()).getStaffImage();
                        fillStaffInfo(resultStaffList.get(resultTable.getSelectedRow()));
                        oldStaff = resultStaffList.get(resultTable.getSelectedRow());
                    }
                } catch (Exception e1) {
//                    logger.error("将搜索结果加载到人员信息出错，不用提示这个错误");
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
                    changeModeService = new ChangeModeService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketRegisterPort());
                    changeModeService.sendInfo("6#" + Egci.configEntity.getFaceCollectionIp() + "#0\n");
                    changeModeService.receiveInfoOnce();
                } else {
                    ChangeModeService changeModeService = null;
                    changeModeService = new ChangeModeService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketRegisterPort());
                    changeModeService.sendInfo("6#" + Egci.configEntity.getFaceCollectionIp() + "#1\n");
                    changeModeService.receiveInfoOnce();
                }
            }
        });
        //选取本地照片
        choseLocalPictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choseLocalPicture();
            }
        });
        //打开摄像头
        takePhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takePhoto();
            }
        });
    }

    /*
     * 打开摄像头
     * */
    private void takePhoto() {
        Thread thread = new Thread(new TakePhotoService(this));
        thread.start();
    }

    /*
     * 显示摄像头抓拍图片
     * */
    public void displayPhoto() {
        try {
            byte[] pictureBytes = Tool.getPictureStream(System.getProperty("user.dir") + "/snap.jpg");
            takePhoto = pictureBytes;
            ImageIcon imageIcon = new ImageIcon(pictureBytes);
            takePhotoLabel.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), photoPanel.getWidth(), 1));
        } catch (Exception e) {
            IdPhoto.setIcon(null);
            logger.error("显示摄像头抓拍图片出错", e);
        }
    }

    /*
     * 重新连接到服务程序
     * */
    public void reconnectToServer() {
//        if (JOptionPane.showConfirmDialog(null, "确定重新连接到服务程序吗？", "重连提示", 0) == 0) {
        try {
            RegisterReceiveInfoSocketService registerReceiveInfoSocketService = new RegisterReceiveInfoSocketService(this);
            registerReceiveInfoSocketService.sendInfo("7#" + Egci.configEntity.getFaceCollectionIp());
            registerReceiveInfoSocketService.start();
            useIdCardCheckBox.setSelected(true);
        } catch (Exception e) {
//            Tool.showMessage("重连失败，请确保服务程序运行正常", "提示", 0);
        }
//        }
    }

    /*
     * 取消操作
     * */
    private void cancel() {
        oldStaff.setStaffId(0);
        cleanStaffInfo();
        inputDisabled();
        saveButton.setEnabled(false);
        searchButton.setEnabled(true);
        addButton.setEnabled(true);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        chineseNameText.setEnabled(true);
        passCardText.setEnabled(true);
        changePhotoButton.setEnabled(false);
        getWaitStaff();
        waitStaffTable.getSelectionModel().clearSelection();
        waitStaffTable.setEnabled(false);
        chineseNameText.requestFocus();
        operationCode = 0;
    }

    /*
     * 删除人员
     * */
    private void delete() {
        if (JOptionPane.showConfirmDialog(null, "确定要删除吗？", "删除提示", 0) == 0) {
            staffOperationService.delete(oldStaff);
            cancel();
        }
    }

    /*
     * 修改人员信息
     * */
    private void update() {
        inputEnable();
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        updateButton.setEnabled(false);
        chineseNameText.setEnabled(true);
        passCardText.setEnabled(true);
        changePhotoButton.setEnabled(true);
        operationCode = 2;
    }

    //初始化页面
    public void init() {
        //启用ping功能,判断连接状态
        PingTimer pingTimer = new PingTimer(this);
        pingTimer.open();
        JFrame frame = new JFrame("办证客户端");
        frame.setContentPane(this.registerForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    /*
     * 将人员信息填入人员表单中
     * */
    private void fillStaffInfo(StaffEntity staffEntity) {
        try {
            chineseNameText.setText(staffEntity.getStaffName());
            passCardText.setText(staffEntity.getStaffCardNumber());
            IdNumberText.setText(staffEntity.getStaffCardId());
            birthdayText.setText(staffEntity.getStaffBirthday());
            sexSelectionCombo.setSelectedIndex(staffEntity.getStaffGender());
            companyText.setText(staffEntity.getStaffCompany());
            try {
                staffEntity.setStaffImage(staffEntity.getStaffImage());
            } catch (Exception e) {
//                logger.error("获取表格人员信息出错", e);
                staffEntity.setStaffImage(null);
            }
            try {
                ImageIcon imageIcon = new ImageIcon(staffEntity.getStaffImage());
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
    public void fillCollectionInfo(FaceCollectionEntity collectionEntity) {
        try {
            idSimilarityLabel.setText(String.valueOf(collectionEntity.getFaceCollectionSimilarity()));
            idNameLabel.setText(collectionEntity.getFaceCollectionName());
            idCardNumberLabel.setText(collectionEntity.getFaceCollectionCardId());
            idNationLabel.setText(collectionEntity.getFaceCollectionNation());
            idSexLabel.setText(String.valueOf(collectionEntity.getFaceCollectionGender()));
            idBirthdayLabel.setText(collectionEntity.getFaceCollectionBirthday());
            idValidityPeriodLabel.setText(collectionEntity.getFaceCollectionExpirationDate());
            idOrganizationLabel.setText(collectionEntity.getFaceCollectionOrganization());
            takePhoto = collectionEntity.getFaceCollectionStaffImage();
            try {
                ImageIcon imageIcon = new ImageIcon(collectionEntity.getFaceCollectionStaffImage());
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
    private void cleanStaffInfo() {
        try {
            chineseNameText.setText("");
            englishNameText.setText("");
            passCardText.setText("");
            IdNumberText.setText("");
            birthdayText.setText("");
            sexSelectionCombo.setSelectedIndex(0);
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
    private StaffEntity getStaffEntity() {
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.setStaffName(chineseNameText.getText());
        staffEntity.setStaffCardNumber(passCardText.getText());
        staffEntity.setStaffCardId(IdNumberText.getText());
        staffEntity.setStaffBirthday(birthdayText.getText());
        staffEntity.setStaffGender(sexSelectionCombo.getSelectedIndex());
        staffEntity.setStaffCompany(companyText.getText());
        try {
            staffEntity.setStaffImage(staffPhoto);
        } catch (Exception e) {
            logger.error("获取表格人员信息出错", e);
            staffEntity.setStaffImage(null);
        }
        return staffEntity;
    }

    /*
     * 新增人员
     * */
    public void add() {
        oldStaff.setStaffId(0);
        waitStaffTable.setEnabled(true);
        inputEnable();
        searchButton.setEnabled(false);
        saveButton.setEnabled(true);
        addButton.setEnabled(false);
        changePhotoButton.setEnabled(true);
        model.setRowCount(0);
        operationCode = 1;
    }

    /*
     * 保存人员信息
     * */
    private void save() {
        if (JOptionPane.showConfirmDialog(null, "确定要保存吗？", "保存提示", 0) == 0) {
            StaffEntity staffEntity = getStaffEntity();
            staffEntity.setStaffImage(staffPhoto);
            if (staffEntity.getStaffName().equals("") || staffEntity.getStaffCardNumber().equals("") || staffEntity.getStaffImage() == null || !Pattern.matches(cardNumberPattern, staffEntity.getStaffCardNumber())) {
                JOptionPane.showMessageDialog(null, "中文名、卡号或照片格式错误！", "错误 ", 0);
            } else {
                if (staffEntity.getStaffCardNumber().equals(oldStaff.getStaffCardNumber())) {
                    if (operationCode == 2) {
                        staffOperationService.save(staffEntity, oldStaff);
                        cancel();
                    } else if (operationCode == 1) {
                        if (Egci.session.selectList("mapping.staffMapper.getStaffWithCard", staffEntity.getStaffCardNumber()).size() > 0) {
                            Tool.showMessage("卡号已存在", "提示", 0);
                        } else {
                            staffOperationService.save(staffEntity, oldStaff);
                            cancel();
                        }
                    }
                } else {
                    if (Egci.session.selectList("mapping.staffMapper.getStaffWithCard", staffEntity.getStaffCardNumber()).size() > 0) {
                        Tool.showMessage("卡号已存在", "提示", 0);
                    } else {
                        staffOperationService.save(staffEntity, oldStaff);
                        cancel();
                    }
                }
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
        resultStaffList.clear();
        resultWaitStaffList.clear();
        model.setRowCount(0);
        waitStaffModel.setRowCount(0);
        searchButton.setEnabled(false);
        addButton.setEnabled(true);
        saveButton.setEnabled(false);
        resultStaffList = staffOperationService.search(chineseNameText.getText(), passCardText.getText());
        for (StaffEntity staffEntity : resultStaffList) {
            Vector vector = new Vector();
            vector.add(0, staffEntity.getStaffCardNumber());
            vector.add(1, staffEntity.getStaffName());
            vector.add(2, staffEntity.getStaffCardId());
            model.addRow(vector);
        }
        //查询待拍照人员表结果
        resultWaitStaffList = staffOperationService.searchWaitStaff(chineseNameText.getText(), passCardText.getText());
        for (StaffEntity staffEntity : resultWaitStaffList) {
            Vector vector = new Vector();
            vector.add(0, staffEntity.getStaffName());
            vector.add(1, staffEntity.getStaffCardNumber());
            waitStaffModel.addRow(vector);
        }
        waitStaffTable.setEnabled(true);
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
    private void inputDisabled() {
        englishNameText.setEnabled(false);
        IdNumberText.setEnabled(false);
        birthdayText.setEnabled(false);
        sexSelectionCombo.setEnabled(false);
        companyText.setEnabled(false);
        IdPhoto.setEnabled(false);
    }

    /*
     * 启用表格输入
     * */
    private void inputEnable() {
        englishNameText.setEnabled(true);
        IdNumberText.setEnabled(true);
        birthdayText.setEnabled(true);
        sexSelectionCombo.setEnabled(true);
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
         * 4：全部网络已断开
         * */
        switch (status) {
            case 0:
                communicationStatusButton.setBackground(Color.green);
                communicationStatusButton.setText("通信正常");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(true);
                break;
            case 1:
                communicationStatusButton.setBackground(Color.lightGray);
                communicationStatusButton.setText("服务程序正在重连...");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(false);
                break;
            case 2:
                communicationStatusButton.setBackground(Color.lightGray);
                communicationStatusButton.setText("服务器已断开");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(false);
                break;
            case 3:
                communicationStatusButton.setBackground(Color.lightGray);
                communicationStatusButton.setText("采集设备已断开");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(false);
                break;
            case 4:
                communicationStatusButton.setBackground(Color.lightGray);
                communicationStatusButton.setText("网络已断开");
                communicationStatusButton.setEnabled(false);
                useIdCardCheckBox.setEnabled(false);
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
                    case "video":
                        Egci.fdLibIDForVideo = fdLibEntity.getFDID();
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e1) {
            logger.error("获取人脸库出错", e1);
        }
    }

    /*
     * 获取本地图片
     * */
    private void choseLocalPicture() {
        try {
            JFileChooser jf = new JFileChooser();
            jf.showOpenDialog(null);//显示打开的文件对话框
            File f = jf.getSelectedFile();//使用文件类获取选择器选择的文件
            String s = f.getAbsolutePath();//返回路径名
            //JOptionPane弹出对话框类，显示绝对路径名
            byte[] pictureBytes = Tool.getPictureStream(s);
            IdPhoto.setIcon(null);
            ImageIcon imageIcon = new ImageIcon(pictureBytes);
            IdPhoto.setIcon(Tool.getImageScale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight(), photoPanel.getHeight(), 2));
            staffPhoto = pictureBytes;
        } catch (Exception e1) {
            Tool.showMessage("未成功选取本地照片", "提示", 0);
            IdPhoto.setIcon(null);
            logger.error("选取本地照片出错", e1);
        }
    }

    /*
     * 获取待拍照人员列表
     * */
    private void getWaitStaff() {
        try {
            resultWaitStaffList.clear();
            waitStaffModel.setRowCount(0);
            resultWaitStaffList = staffOperationService.getWaitStaffList();
            for (StaffEntity staffEntity : resultWaitStaffList) {
                Vector vector = new Vector();
                vector.add(0, staffEntity.getStaffName());
                vector.add(1, staffEntity.getStaffCardNumber());
                waitStaffModel.addRow(vector);
            }
        } catch (Exception e) {
            logger.error("获取待拍照人员列表出错", e);
        }
    }

    /*
     * 获取查询按钮，用来监听enter
     * */
    public JButton getSearchButton() {
        return searchButton;
    }

    /*
     * 获取输入框输入焦点问题
     * */
    public JTextField getChineseNameText() {
        return chineseNameText;
    }
}
