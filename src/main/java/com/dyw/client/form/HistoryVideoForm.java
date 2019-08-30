package com.dyw.client.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyw.client.HCNetSDK;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.StaffEntity;
import com.dyw.client.entity.protection.FDLibEntity;
import com.dyw.client.entity.protection.TargetsEntity;
import com.dyw.client.service.BaseFormService;
import com.dyw.client.service.DateSelectorButtonService;
import com.dyw.client.service.PlaybackService;
import com.dyw.client.service.SnapAlarmTableCellRenderer;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class HistoryVideoForm extends BaseFormService {
    private Logger logger = LoggerFactory.getLogger(HistoryVideoForm.class);
    private DefaultTableModel historyVideoModel = new DefaultTableModel();
    private List<FDLibEntity> fdLibEntityList = new ArrayList<>();//人脸库列表

    @Override
    public JPanel getPanel() {
        return historyVideoForm;
    }

    private JFrame frame;
    private JPanel historyVideoForm;
    private JPanel historyVideoToolbarPanel;
    private JPanel historyVideoContentPanel;
    private JComboBox modelChooseCombo;
    private JTextField cardNumberText;
    private JButton imageChooseButton;
    private JButton searchButton;
    private JLabel cardNumberLabel;
    private JLabel imagePath;
    private JScrollPane contentScroll;
    private JTable contentTable;
    private JLabel startTimeLabel;
    private DateSelectorButtonService startTimeButton;
    private JLabel endTimeLabel;
    private DateSelectorButtonService endTimeButton;
    private JLabel similarityMinLabel;
    private JTextField similarityMinText;
    private JLabel similarityMaxLabel;
    private JTextField similarityMaxText;
    private JTextField amountText;
    private JLabel amountLabel;

    private JPopupMenu searchPopupMenu;
    private int menuStatus = 0;
    private JMenuItem videoMenuItem = new JMenuItem("查看录像");


    public HistoryVideoForm() {
        /*
         * 获取人脸库列表
         * */
        getFDLib();
        /*
         * 初始化选项框
         * */
        searchButton.setEnabled(false);
        modelChooseCombo.addItem("请选择查询方式");
        modelChooseCombo.addItem("根据卡号查询");
        modelChooseCombo.addItem("根据图片查询");
        modelChooseCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (modelChooseCombo.getSelectedIndex()) {
                        case 0:
                            searchButton.setEnabled(false);
                            break;
                        case 1:
                            imagePath.setText("");
                            searchButton.setEnabled(true);
                            cardNumberLabel.setVisible(true);
                            cardNumberText.setVisible(true);
                            imageChooseButton.setVisible(false);
                            imagePath.setVisible(false);
                            break;
                        case 2:
                            searchButton.setEnabled(true);
                            cardNumberLabel.setVisible(false);
                            cardNumberText.setVisible(false);
                            imageChooseButton.setVisible(true);
                            imagePath.setVisible(true);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        /*
         * 初始化表格
         * */
        String[] columnHistoryVideoInfo = {"图片", "时间", "设备", "分值"};
        contentTable.setModel(historyVideoModel);
        historyVideoModel.setColumnIdentifiers(columnHistoryVideoInfo);
        TableCellRenderer historyVideoRenderer = new SnapAlarmTableCellRenderer();
        contentTable.setDefaultRenderer(Object.class, historyVideoRenderer);

        /*
         * 照片选择
         * */
        imageChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choseLocalPicture();
            }
        });
        /*
         * 初始化时间
         * */
        startTimeButton.setText(Tool.getCurrentDate() + " 00:00:00");
        endTimeButton.setText(Tool.getCurrentDate() + " 23:59:59");
        /*
         * 点击查询
         * */
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modelChooseCombo.getSelectedIndex() == 1 && cardNumberText.getText().equals("")) {
                    Tool.showMessage("请完善查询条件", "提示", 1);
                    return;
                }
                if (modelChooseCombo.getSelectedIndex() == 2 && imagePath.getText().equals("")) {
                    Tool.showMessage("请完善查询条件", "提示", 1);
                    return;
                }
                if (similarityMinText.getText().equals("") || similarityMaxText.getText().equals("") || amountText.getText().equals("")) {
                    Tool.showMessage("请完善查询条件", "提示", 1);
                    return;
                }
                search();
            }
        });
        /*
         * 弹出右键菜单
         * */
        contentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //在table显示
                    searchPopupMenu = new JPopupMenu();
                    //表格 的rowAtPoint方法返回坐标所在的行号，参数为坐标类型，
                    menuStatus = contentTable.rowAtPoint(e.getPoint());
                    searchPopupMenu.add(videoMenuItem);
                    searchPopupMenu.show(contentTable, e.getX(), e.getY());
                }
            }
        });
        /*
         * 查看录像
         * */
        videoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String timeInfo = contentTable.getValueAt(menuStatus, 1).toString();
                    logger.info("抓拍时间:" + timeInfo);
                    HCNetSDK.NET_DVR_TIME struStartTime = Tool.segmentationTime(0, timeInfo);//start
                    HCNetSDK.NET_DVR_TIME struStopTime = Tool.segmentationTime(1, timeInfo);//end
                    PlaybackService playbackService = new PlaybackService();
                    playbackService.playOrPause(struStartTime, struStopTime, Egci.cameraMap.get(contentTable.getValueAt(menuStatus, 2).toString()));
                } catch (Exception e1) {
                    Tool.showMessage("查看录像出错", "提示", 0);
                    logger.error("查看录像出错", e1);
                }
            }
        });
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
            imagePath.setText(s);
        } catch (Exception e1) {
            Tool.showMessage("未成功选取本地照片", "提示", 0);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        startTimeButton = new DateSelectorButtonService();
        endTimeButton = new DateSelectorButtonService();
    }

    /*
     * 查询
     * */
    private void search() {
        /*
         * 第一步：上传图片到脸谱，建立图片模型，返回faceUrl
         * */
        try {
            byte[] image;
            if (modelChooseCombo.getSelectedIndex() == 1) {//输入卡号搜索
                List<StaffEntity> staffEntityList = Egci.session.selectList("mapping.staffMapper.getStaffWithCard", cardNumberText.getText());
                if (staffEntityList.size() > 0) {
                    image = staffEntityList.get(0).getStaffImage();
                } else {
                    Tool.showMessage("卡号不存在", "提示", 1);
                    return;
                }
            } else {//本地图片搜索
                image = Tool.getPictureStream(imagePath.getText());
            }
            org.json.JSONObject resultFaceUrlData = Tool.faceInfoOperation(1, Egci.fdLibIDForVideo, image, null);
            if (resultFaceUrlData.getInt("statusCode") == 1) {
                /*
                 * 第二步：查询抓拍结果
                 * */
                org.json.JSONObject inboundData = new org.json.JSONObject();
                String instruction = "/ISAPI/SDT/Face/searchByPic?supportSync=true";
                inboundData.put("searchResultPosition", 0);//起始位置
                inboundData.put("maxResults", Integer.parseInt(amountText.getText()));//最大记录数
                inboundData.put("startTime", Tool.changeTimeToISO8601(startTimeButton.getText()));//起始时间
                inboundData.put("endTime", Tool.changeTimeToISO8601(endTimeButton.getText()));//结束时间
                inboundData.put("similarityMin", Float.parseFloat(similarityMinText.getText()) / 100);//最小阈值
                inboundData.put("similarityMax", Float.parseFloat(similarityMaxText.getText()) / 100);//最大阈值
                inboundData.put("dataType", "URL");
                inboundData.put("pictureMerge", false);//attention:一人多图是否合并
                inboundData.put("modelMaxNum", Integer.parseInt(amountText.getText()));
                inboundData.put("targetModelData", "");
                inboundData.put("faceURL", resultFaceUrlData.getString("URL"));//图片url
                org.json.JSONObject resultData = Tool.sendInstructionAndReceiveStatusAndData(3, instruction, inboundData);
                if (resultData.getInt("statusCode") == 1) {
                    List<TargetsEntity> targetsEntityList = JSON.parseArray(resultData.getString("targets"), TargetsEntity.class);
                    showResult(targetsEntityList);
                } else {
                    Tool.showMessage("查询结果出错", resultData.getString("errorMsg"), 0);
                }
            } else {
                Tool.showMessage("建模失败，请更换图片重试", resultFaceUrlData.getString("errorMsg"), 0);
            }
        } catch (Exception e) {
            Tool.showMessage("查询失败，请稍后重试", "提示", 0);
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
     * 显示搜索结果
     * */
    private void showResult(List<TargetsEntity> targetsEntityList) {
        try {
            historyVideoModel.setRowCount(0);
            for (TargetsEntity targetsEntity : targetsEntityList) {
                Vector vector = new Vector();
                vector.add(0, Base64.encodeBytes(Tool.getURLStream(targetsEntity.getSubpicUrl())));
                vector.add(1, changeISO8601ToTime(targetsEntity.getCaptureTime()));
                vector.add(2, targetsEntity.getCaptureSite());
                vector.add(3, targetsEntity.getSimilarity());
                historyVideoModel.addRow(vector);
            }
        } catch (Exception e) {
            logger.error("显示结果出错", e);
        }
    }

    /*
     *
     * */
    private String changeISO8601ToTime(String ISO8601) {
        String timeInfo = "";
        try {
            timeInfo = ISO8601.replace("T", " ").replace("Z", "");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateBefore = simpleDateFormat.parse(timeInfo);
            long timestamp = dateBefore.getTime() + 28800000;
            Date dateAfter = new Date(timestamp);
            timeInfo = simpleDateFormat.format(dateAfter);
        } catch (Exception e) {
            logger.error("时间转换出错", e);
            timeInfo = "2030-04-10 12:24:17";
        }
        return timeInfo;
    }

    public void init() {
        frame = new JFrame("通行视频记录");
        frame.setContentPane(this.historyVideoForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
