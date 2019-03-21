package ISAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import UI.FDlib.BlackFD.BlackFDlibManagement.FDlibListStruct;

import javax.swing.SwingConstants;
import java.awt.Panel;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;


public class VideoAnalysisTaskForm extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField TaskNametextField;
    private JTextField streamUrltextField;
    private JTextField userNametextField;
    private JTextField passWordtextField;
    private JTextField maxSplitCounttextField;
    private JTextField splitTimetextField;
    private JTextField startTimetextField;
    private JTextField endTimetextField_1;
    private JTextField tempInfostartTimetextField;
    private JTextField tempInfoendTimetextField;
    private JTextField destinationUrltextField;
    private JTextField duserNametextField;
    private JTextField dpasswordtextField;
    private JTextField detectRegionregionNametextField;
    private JTextField maskRegionregionNametextField;
    private JTextField targetSpeedtextField;
    private JTextField captureTimestextField;
    private JTextField sensitivitytextField;
    private JTextField captureTimeIntervaltextField;
    private JTextField captureThresholdtextField;
    private JTextField deviceIdtextField;
    private JTextField deviceChanneltextField;
    private JTextField deviceNametextField;

    public static class StruVideoAnalysis {
        public taskInfo taskInfo;
    }

    public static class taskInfo {
        public String taskName;
        public int algorithmType;
        public String streamType;
        public String taskPriority;
        public stream stream;
        public time time;
        public List<destination> destination;
        public rule rule;
        public targetAttrs targetAttrs;
    }

    public static class stream {
        public String url;
        public String userName;
        public String passWord;
        public int maxSplitCount;
        public int splitTime;
    }


    public static class time {
        public String taskType;
        public planInfo planInfo;
        public tempInfo tempInfo;
    }


    public static class planInfo {
        public List<exeTimeList> exeTimeList;
    }

    public static class exeTimeList {
        public String day;
        public List<timeRange> timeRange;
    }

    public static class timeRange {
        public String startTime;
        public String endTime;
        public int algorithmType;
    }

    public static class tempInfo {
        public String startTime;
        public String endTime;
    }

    public static class destination {
        public String destinationType;
        public String destinationUrl;
        public String userName;
        public String passWord;
        public int subscribeEvent;
    }

    public static class rule {
        public boolean bigTargetModel;
        public detectRule detectRule;
        public maskRegion maskRegion;
        public int targetSpeed;
        public int captureTimes;
        public int sensitivity;
        public int captureTimeInterval;
        public int captureThreshold;
        public boolean misinformation;
    }

    public static class detectRule {
        public List<detectRegion> detectRegion;
    }

    public static class detectRegion {
        public String regionName;
        public List<point> point;
    }

    public static class point {
        public float x;
        public float y;
    }

    public static class maskRegion {
        public String regionName;
        public List<point> point;
    }

    public static class targetAttrs {
        public String deviceId;
        public int deviceChannel;
        public String deviceName;
    }

    public static StruVideoAnalysis struVideoAnalysis = new StruVideoAnalysis();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            VideoAnalysisTaskForm dialog = new VideoAnalysisTaskForm();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public VideoAnalysisTaskForm() {
        setTitle("\u89C6\u9891\u5206\u6790\u4EFB\u52A1-\u730E\u9E70");
        setBounds(100, 100, 1020, 769);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setLocationRelativeTo(null);
        final planInfo struplanInfo = new planInfo();

        struplanInfo.exeTimeList = new ArrayList<exeTimeList>();

        final List<destination> destinationlist = new ArrayList<destination>();

        final detectRule strdetectRule = new detectRule();

        strdetectRule.detectRegion = new ArrayList<detectRegion>();

        final List<maskRegion> maskRegionlist = new ArrayList<maskRegion>();

        JLabel label = new JLabel("\u4EFB\u52A1\u540D\u79F0");
        label.setBounds(30, 13, 54, 15);
        contentPanel.add(label);

        JLabel label_1 = new JLabel("\u7B97\u6CD5\u7C7B\u578B");
        label_1.setBounds(30, 44, 54, 15);
        contentPanel.add(label_1);

        TaskNametextField = new JTextField();
        TaskNametextField.setBounds(109, 10, 180, 21);
        contentPanel.add(TaskNametextField);
        TaskNametextField.setColumns(10);

        final JComboBox algorithmTypecomboBox = new JComboBox();
        algorithmTypecomboBox.setBackground(Color.WHITE);
        algorithmTypecomboBox.setModel(new DefaultComboBoxModel(new String[]{"\u9AD8\u8D28\u91CF\u8F66", "\u9AD8\u6027\u80FD\u8F66", "\u4EBA\u5458\u7ED3\u6784\u5316\u5EFA\u6A21", "\u4EBA\u548C\u8F66\u7ED3\u6784\u5316\u5EFA\u6A21", "\u89C6\u9891\u4EBA\u8138\u68C0\u6D4B", "\u89C6\u9891\u4EBA\u8138\u68C0\u6D4B\u548C\u5EFA\u6A21"}));
        algorithmTypecomboBox.setBounds(109, 41, 180, 21);
        contentPanel.add(algorithmTypecomboBox);

        JLabel label_2 = new JLabel("\u53D6\u6D41\u7C7B\u578B");
        label_2.setBounds(30, 75, 54, 15);
        contentPanel.add(label_2);

        final JComboBox streamTypecomboBox = new JComboBox();
        streamTypecomboBox.setBackground(Color.WHITE);
        streamTypecomboBox.setModel(new DefaultComboBoxModel(new String[]{"realtime", "historyvideo", "localvideo"}));
        streamTypecomboBox.setBounds(109, 72, 180, 21);
        contentPanel.add(streamTypecomboBox);

        JLabel label_3 = new JLabel("\u4EFB\u52A1\u4F18\u5148\u7EA7");
        label_3.setBounds(30, 106, 69, 15);
        contentPanel.add(label_3);

        final JComboBox taskPrioritycomboBox = new JComboBox();
        taskPrioritycomboBox.setBackground(Color.WHITE);
        taskPrioritycomboBox.setModel(new DefaultComboBoxModel(new String[]{"high", "medium", "low"}));
        taskPrioritycomboBox.setBounds(109, 103, 180, 21);
        contentPanel.add(taskPrioritycomboBox);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 156, 351, 160);
        contentPanel.add(panel);
        panel.setLayout(null);

        JLabel lblurl = new JLabel("\u53D6\u6D41url");
        lblurl.setBounds(26, 10, 54, 15);
        panel.add(lblurl);

        streamUrltextField = new JTextField();
        streamUrltextField.setColumns(10);
        streamUrltextField.setBounds(105, 7, 180, 21);
        panel.add(streamUrltextField);

        JLabel label_4 = new JLabel("\u53D6\u6D41\u7528\u6237\u540D");
        label_4.setBounds(26, 41, 69, 15);
        panel.add(label_4);

        userNametextField = new JTextField();
        userNametextField.setColumns(10);
        userNametextField.setBounds(105, 38, 180, 21);
        panel.add(userNametextField);

        JLabel label_5 = new JLabel("\u53D6\u6D41\u5BC6\u7801");
        label_5.setBounds(26, 72, 69, 15);
        panel.add(label_5);

        passWordtextField = new JTextField();
        passWordtextField.setColumns(10);
        passWordtextField.setBounds(105, 69, 180, 21);
        panel.add(passWordtextField);

        JLabel label_6 = new JLabel("\u4EFB\u52A1\u5E76\u53D1\r\n\u6267\u884C\u4E2A\u6570");
        label_6.setBounds(26, 101, 116, 18);
        panel.add(label_6);

        maxSplitCounttextField = new JTextField();
        maxSplitCounttextField.setColumns(10);
        maxSplitCounttextField.setBounds(134, 100, 151, 21);
        panel.add(maxSplitCounttextField);

        JLabel label_7 = new JLabel("\u6700\u5C0F\u5207\u8FB9\u65F6\u95F4");
        label_7.setBounds(26, 129, 116, 18);
        panel.add(label_7);

        splitTimetextField = new JTextField();
        splitTimetextField.setColumns(10);
        splitTimetextField.setBounds(106, 129, 179, 21);
        panel.add(splitTimetextField);

        JLabel label_8 = new JLabel("1-40");
        label_8.setBounds(295, 101, 35, 18);
        panel.add(label_8);

        JLabel lbls = new JLabel("1-3599s");
        lbls.setBounds(295, 129, 47, 18);
        panel.add(lbls);

        JLabel lblStream = new JLabel("\u53D6\u6D41");
        lblStream.setBounds(10, 131, 54, 15);
        contentPanel.add(lblStream);

        JLabel label_9 = new JLabel("\u65F6\u95F4");
        label_9.setBounds(10, 326, 54, 15);
        contentPanel.add(label_9);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(Color.WHITE);
        panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_2.setBounds(10, 351, 351, 370);
        contentPanel.add(panel_2);
        panel_2.setLayout(null);

        JLabel label_10 = new JLabel("\u4EFB\u52A1\u65F6\u95F4\u7C7B\u578B");
        label_10.setBounds(10, 13, 94, 15);
        panel_2.add(label_10);

        final JComboBox taskTypecomboBox = new JComboBox();
        taskTypecomboBox.setBackground(Color.WHITE);
        taskTypecomboBox.setBounds(109, 10, 180, 21);
        panel_2.add(taskTypecomboBox);
        taskTypecomboBox.setModel(new DefaultComboBoxModel(new String[]{"plan", "temp"}));

        JLabel label_11 = new JLabel("\u8BA1\u5212\u4EFB\u52A1\u4FE1\u606F");
        label_11.setBounds(10, 38, 80, 15);
        panel_2.add(label_11);

        JLabel label_12 = new JLabel("\u8BA1\u5212\u65F6\u95F4\u5217\u8868");
        label_12.setBounds(108, 38, 80, 15);
        panel_2.add(label_12);

        JLabel lblDay = new JLabel("\u8BA1\u5212\u6267\u884C\u65E5");
        lblDay.setBounds(10, 66, 80, 15);
        panel_2.add(lblDay);

        final JComboBox exeTimecomboBox = new JComboBox();
        exeTimecomboBox.setBackground(Color.WHITE);
        exeTimecomboBox.setBounds(109, 63, 180, 21);
        panel_2.add(exeTimecomboBox);
        exeTimecomboBox.setModel(new DefaultComboBoxModel(new String[]{"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"}));

        JButton button = new JButton("\u6DFB\u52A0\u6267\u884C\u65E5");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                exeTimeList singleexeTimeList = new exeTimeList();
                singleexeTimeList.day = exeTimecomboBox.getSelectedItem().toString();
                struplanInfo.exeTimeList.add(singleexeTimeList);

                struplanInfo.exeTimeList.get(struplanInfo.exeTimeList.size() - 1).timeRange = new ArrayList<timeRange>();

                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        button.setBounds(109, 94, 111, 23);
        panel_2.add(button);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 118, 323, 133);
        panel_2.add(panel_1);
        panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_1.setBackground(Color.WHITE);
        panel_1.setLayout(null);

        JLabel label_13 = new JLabel("\u7B97\u6CD5\u7C7B\u578B");
        label_13.setBounds(10, 82, 54, 15);
        panel_1.add(label_13);

        final JComboBox streamalgorithmTypecomboBox_1 = new JComboBox();
        streamalgorithmTypecomboBox_1.setBackground(Color.WHITE);
        streamalgorithmTypecomboBox_1.setModel(new DefaultComboBoxModel(new String[]{"\u9AD8\u8D28\u91CF\u8F66", "\u9AD8\u6027\u80FD\u8F66", "\u4EBA\u5458\u7ED3\u6784\u5316\u5EFA\u6A21", "\u4EBA\u548C\u8F66\u7ED3\u6784\u5316\u5EFA\u6A21", "\u89C6\u9891\u4EBA\u8138\u68C0\u6D4B", "\u89C6\u9891\u4EBA\u8138\u68C0\u6D4B\u548C\u5EFA\u6A21"}));
        streamalgorithmTypecomboBox_1.setBounds(114, 79, 180, 21);
        panel_1.add(streamalgorithmTypecomboBox_1);

        JLabel label_14 = new JLabel("\u53D6\u6D41\u5F00\u59CB\u65F6\u95F4");
        label_14.setBounds(10, 13, 80, 15);
        panel_1.add(label_14);

        startTimetextField = new JTextField();
        startTimetextField.setText("00:00:00");
        startTimetextField.setColumns(10);
        startTimetextField.setBounds(114, 10, 180, 21);
        panel_1.add(startTimetextField);

        JLabel label_15 = new JLabel("\u53D6\u6D41\u7ED3\u675F\u65F6\u95F4");
        label_15.setBounds(10, 44, 80, 15);
        panel_1.add(label_15);

        endTimetextField_1 = new JTextField();
        endTimetextField_1.setText("00:00:00");
        endTimetextField_1.setColumns(10);
        endTimetextField_1.setBounds(114, 41, 180, 21);
        panel_1.add(endTimetextField_1);

        JButton button_1 = new JButton("\u6DFB\u52A0\u6267\u884C\u65E5\u65F6\u95F4\u5217\u8868");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                for (int i = 0; i < struplanInfo.exeTimeList.size(); i++) {
                    if (struplanInfo.exeTimeList.get(i).day == exeTimecomboBox.getSelectedItem().toString()) {
                        timeRange singletimeRange = new timeRange();
                        singletimeRange.startTime = startTimetextField.getText();
                        singletimeRange.endTime = endTimetextField_1.getText();
                        singletimeRange.algorithmType = streamalgorithmTypecomboBox_1.getSelectedIndex() + 1;

                        struplanInfo.exeTimeList.get(i).timeRange.add(singletimeRange);

                        break;
                    }
                }

                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        button_1.setBounds(114, 110, 159, 23);
        panel_1.add(button_1);

        JLabel label_16 = new JLabel("\u4E34\u65F6\u4EFB\u52A1\u4FE1\u606F");
        label_16.setBounds(11, 261, 80, 15);
        panel_2.add(label_16);

        JLabel label_17 = new JLabel("\u53D6\u6D41\u5F00\u59CB\u65F6\u95F4");
        label_17.setBounds(10, 289, 80, 15);
        panel_2.add(label_17);

        JLabel label_18 = new JLabel("\u53D6\u6D41\u5F00\u59CB\u65F6\u95F4");
        label_18.setBounds(10, 320, 80, 15);
        panel_2.add(label_18);

        tempInfostartTimetextField = new JTextField();
        tempInfostartTimetextField.setBounds(114, 286, 180, 21);
        panel_2.add(tempInfostartTimetextField);
        tempInfostartTimetextField.setText("00:00:00");
        tempInfostartTimetextField.setColumns(10);

        tempInfoendTimetextField = new JTextField();
        tempInfoendTimetextField.setBounds(114, 317, 180, 21);
        panel_2.add(tempInfoendTimetextField);
        tempInfoendTimetextField.setText("00:00:00");
        tempInfoendTimetextField.setColumns(10);

        JLabel label_19 = new JLabel("\u63A5\u6536\u65B9");
        label_19.setBounds(371, 10, 54, 15);
        contentPanel.add(label_19);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_3.setBackground(Color.WHITE);
        panel_3.setBounds(371, 27, 322, 185);
        contentPanel.add(panel_3);
        panel_3.setLayout(null);

        JLabel label_20 = new JLabel("\u63A5\u6536\u65B9\u7C7B\u578B");
        label_20.setBounds(24, 13, 69, 15);
        panel_3.add(label_20);

        final JComboBox destinationTypecomboBox = new JComboBox();
        destinationTypecomboBox.setBackground(Color.WHITE);
        destinationTypecomboBox.setModel(new DefaultComboBoxModel(new String[]{"ServerKafka", "ServerCloud", "ServerCVR-CVR", "ServerS08", "ServerSDKServerWS", "ServerK08-K08", "Server86SDK", "ServerHCS", "ServerClient"}));
        destinationTypecomboBox.setBounds(103, 10, 180, 21);
        panel_3.add(destinationTypecomboBox);

        JLabel lblurl_1 = new JLabel("\u7ED3\u679C\u63A5\u6536URL");
        lblurl_1.setBounds(24, 38, 80, 15);
        panel_3.add(lblurl_1);

        destinationUrltextField = new JTextField();
        destinationUrltextField.setColumns(10);
        destinationUrltextField.setBounds(103, 35, 180, 21);
        panel_3.add(destinationUrltextField);

        JLabel label_21 = new JLabel("\u63A5\u6536\u65B9\u7528\u6237\u540D");
        label_21.setBounds(24, 63, 80, 15);
        panel_3.add(label_21);

        duserNametextField = new JTextField();
        duserNametextField.setColumns(10);
        duserNametextField.setBounds(103, 59, 180, 21);
        panel_3.add(duserNametextField);

        JLabel label_22 = new JLabel("\u63A5\u6536\u65B9\u5BC6\u7801");
        label_22.setBounds(24, 88, 69, 15);
        panel_3.add(label_22);

        dpasswordtextField = new JTextField();
        dpasswordtextField.setColumns(10);
        dpasswordtextField.setBounds(103, 86, 180, 21);
        panel_3.add(dpasswordtextField);

        JLabel label_23 = new JLabel("\u8BA2\u9605\u68C0\u6D4B\u4E0A\u4F20\u4E8B\u4EF6\u7C7B\u578B");
        label_23.setBounds(24, 120, 136, 15);
        panel_3.add(label_23);

        final JComboBox dsubscribeEventcomboBox = new JComboBox();
        dsubscribeEventcomboBox.setBackground(Color.WHITE);
        dsubscribeEventcomboBox.setModel(new DefaultComboBoxModel(new String[]{"\u4EBA\u8138\u6293\u62CD", "\u4EBA\u8138\u6293\u62CD\u5206\u6790", "\u4EBA\u8138\u6293\u62CD\u5206\u6790\u6BD4\u5BF9"}));
        dsubscribeEventcomboBox.setBounds(159, 117, 126, 21);
        panel_3.add(dsubscribeEventcomboBox);

        JButton adddestinationBtn = new JButton("\u6DFB\u52A0\u63A5\u6536\u65B9");
        adddestinationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                destination singledestination = new destination();
                singledestination.destinationType = destinationTypecomboBox.getSelectedItem().toString();
                singledestination.destinationUrl = destinationUrltextField.getText();
                singledestination.userName = duserNametextField.getText();
                singledestination.passWord = dpasswordtextField.getText();
                singledestination.subscribeEvent = dsubscribeEventcomboBox.getSelectedIndex() + 1;

                destinationlist.add(singledestination);

                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        adddestinationBtn.setBounds(106, 152, 118, 23);
        panel_3.add(adddestinationBtn);

        JLabel label_24 = new JLabel("\u89C4\u5219");
        label_24.setBounds(371, 222, 54, 15);
        contentPanel.add(label_24);

        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_4.setBackground(Color.WHITE);
        panel_4.setBounds(371, 247, 322, 474);
        contentPanel.add(panel_4);
        panel_4.setLayout(null);

        final JCheckBox bigTargetModelcheckBox = new JCheckBox("\u5F00\u542F\u5927\u76EE\u6807\u6A21\u5F0F");
        bigTargetModelcheckBox.setBackground(Color.WHITE);
        bigTargetModelcheckBox.setBounds(20, 6, 132, 23);
        panel_4.add(bigTargetModelcheckBox);

        JLabel label_26 = new JLabel("\u68C0\u6D4B\u89C4\u5219");
        label_26.setBounds(20, 34, 54, 15);
        panel_4.add(label_26);

        JLabel label_25 = new JLabel("\u89C4\u5219\u533A\u57DF");
        label_25.setBounds(20, 59, 54, 15);
        panel_4.add(label_25);

        JPanel panel_5 = new JPanel();
        panel_5.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_5.setBackground(Color.WHITE);
        panel_5.setBounds(20, 84, 280, 97);
        panel_4.add(panel_5);
        panel_5.setLayout(null);

        JLabel label_27 = new JLabel("\u533A\u57DF\u540D\u79F0");
        label_27.setBounds(10, 13, 69, 15);
        panel_5.add(label_27);

        detectRegionregionNametextField = new JTextField();
        detectRegionregionNametextField.setColumns(10);
        detectRegionregionNametextField.setBounds(90, 10, 180, 21);
        panel_5.add(detectRegionregionNametextField);

        JLabel label_28 = new JLabel("\u533A\u57DF\u5750\u6807");
        label_28.setBounds(10, 42, 69, 15);
        panel_5.add(label_28);

        JButton button_3 = new JButton("\u6DFB\u52A0\u89C4\u5219\u533A\u57DF");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                RegionPointList getRegion = new RegionPointList();
                getRegion.show();

                detectRegion singledetectRegion = new detectRegion();
                singledetectRegion.point = new ArrayList<point>();
                singledetectRegion.regionName = detectRegionregionNametextField.getText();

                for (int i = 0; i < getRegion.pointlist.size(); i++) {
                    point singlepoint = new point();
                    singlepoint.x = getRegion.pointlist.get(i).x;
                    singlepoint.y = getRegion.pointlist.get(i).y;
                    singledetectRegion.point.add(singlepoint);
                }
                strdetectRule.detectRegion.add(singledetectRegion);

                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        button_3.setBounds(90, 41, 127, 23);
        panel_5.add(button_3);

        JLabel label_29 = new JLabel("\u5C4F\u853D\u533A\u57DF");
        label_29.setBounds(20, 184, 54, 15);
        panel_4.add(label_29);

        JPanel panel_6 = new JPanel();
        panel_6.setLayout(null);
        panel_6.setBorder(new LineBorder(Color.LIGHT_GRAY));
        panel_6.setBackground(Color.WHITE);
        panel_6.setBounds(20, 209, 280, 89);
        panel_4.add(panel_6);

        JLabel label_30 = new JLabel("\u533A\u57DF\u540D\u79F0");
        label_30.setBounds(10, 13, 69, 15);
        panel_6.add(label_30);

        maskRegionregionNametextField = new JTextField();
        maskRegionregionNametextField.setColumns(10);
        maskRegionregionNametextField.setBounds(90, 10, 180, 21);
        panel_6.add(maskRegionregionNametextField);

        JLabel label_31 = new JLabel("\u533A\u57DF\u5750\u6807");
        label_31.setBounds(10, 42, 69, 15);
        panel_6.add(label_31);

        JButton button_4 = new JButton("\u6DFB\u52A0\u5C4F\u853D\u533A\u57DF");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                RegionPointList getRegion = new RegionPointList();
                getRegion.show();

                maskRegion singlemaskRegion = new maskRegion();
                singlemaskRegion.point = new ArrayList<point>();
                singlemaskRegion.regionName = maskRegionregionNametextField.getText();

                for (int i = 0; i < getRegion.pointlist.size(); i++) {
                    point singlepoint = new point();
                    singlepoint.x = getRegion.pointlist.get(i).x;
                    singlepoint.y = getRegion.pointlist.get(i).y;
                    singlemaskRegion.point.add(singlepoint);
                }
                maskRegionlist.add(singlemaskRegion);

                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        button_4.setBounds(90, 41, 127, 23);
        panel_6.add(button_4);

        JLabel label_32 = new JLabel("\u76EE\u6807\u751F\u6210\u901F\u5EA6");
        label_32.setBounds(20, 308, 87, 15);
        panel_4.add(label_32);

        targetSpeedtextField = new JTextField();
        targetSpeedtextField.setColumns(10);
        targetSpeedtextField.setBounds(99, 305, 180, 21);
        panel_4.add(targetSpeedtextField);

        JLabel label_33 = new JLabel("\u6293\u62CD\u6B21\u6570");
        label_33.setBounds(20, 336, 69, 15);
        panel_4.add(label_33);

        captureTimestextField = new JTextField();
        captureTimestextField.setColumns(10);
        captureTimestextField.setBounds(99, 333, 180, 21);
        panel_4.add(captureTimestextField);

        JLabel label_34 = new JLabel("\u7075\u654F\u5EA6");
        label_34.setBounds(20, 364, 69, 15);
        panel_4.add(label_34);

        sensitivitytextField = new JTextField();
        sensitivitytextField.setColumns(10);
        sensitivitytextField.setBounds(99, 361, 180, 21);
        panel_4.add(sensitivitytextField);

        JLabel label_35 = new JLabel("\u6293\u62CD\u95F4\u9694");
        label_35.setBounds(20, 392, 69, 15);
        panel_4.add(label_35);

        captureTimeIntervaltextField = new JTextField();
        captureTimeIntervaltextField.setColumns(10);
        captureTimeIntervaltextField.setBounds(99, 389, 180, 21);
        panel_4.add(captureTimeIntervaltextField);

        JLabel label_36 = new JLabel("\u6293\u62CD\u9608\u503C");
        label_36.setBounds(20, 423, 69, 15);
        panel_4.add(label_36);

        captureThresholdtextField = new JTextField();
        captureThresholdtextField.setColumns(10);
        captureThresholdtextField.setBounds(99, 420, 180, 21);
        panel_4.add(captureThresholdtextField);

        final JCheckBox misinformationcheckBox = new JCheckBox("\u53BB\u8BEF\u62A5");
        misinformationcheckBox.setBackground(Color.WHITE);
        misinformationcheckBox.setBounds(20, 444, 132, 23);
        panel_4.add(misinformationcheckBox);

        JLabel label_37 = new JLabel("\u900F\u4F20\u5B57\u6BB5");
        label_37.setBounds(720, 10, 54, 15);
        contentPanel.add(label_37);

        JLabel lblid = new JLabel("\u8BBE\u5907ID");
        lblid.setBounds(720, 41, 54, 15);
        contentPanel.add(lblid);

        deviceIdtextField = new JTextField();
        deviceIdtextField.setColumns(10);
        deviceIdtextField.setBounds(799, 38, 180, 21);
        contentPanel.add(deviceIdtextField);

        JLabel label_39 = new JLabel("\u8BBE\u5907\u901A\u9053\u53F7");
        label_39.setBounds(720, 75, 69, 15);
        contentPanel.add(label_39);

        deviceChanneltextField = new JTextField();
        deviceChanneltextField.setColumns(10);
        deviceChanneltextField.setBounds(799, 72, 180, 21);
        contentPanel.add(deviceChanneltextField);

        JLabel label_40 = new JLabel("\u8BBE\u5907\u540D\u79F0");
        label_40.setBounds(720, 109, 54, 15);
        contentPanel.add(label_40);

        deviceNametextField = new JTextField();
        deviceNametextField.setColumns(10);
        deviceNametextField.setBounds(799, 106, 180, 21);
        contentPanel.add(deviceNametextField);


        JButton TaskCommitBtn = new JButton("\u4EFB\u52A1\u63D0\u4EA4");
        TaskCommitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String strurl = "/ISAPI/SDT/Management/Task/Video?format=json";

                JSONObject jsonVideoAnalysisTaskInfo = new JSONObject();

                JSONObject jsontaskInfo = new JSONObject();

                JSONObject jsonstream = new JSONObject();

                JSONObject jsontime = new JSONObject();

                try {

                    jsontaskInfo.put("taskName", TaskNametextField.getText());
                    jsontaskInfo.put("algorithmType", algorithmTypecomboBox.getSelectedIndex() + 1);
                    jsontaskInfo.put("streamType", streamTypecomboBox.getSelectedItem().toString());
                    jsontaskInfo.put("taskPriority", taskPrioritycomboBox.getSelectedItem().toString());

                    //stream
                    jsonstream.put("streamUrl", streamUrltextField.getText());
                    jsonstream.put("userName", userNametextField.getText());
                    jsonstream.put("passWord", passWordtextField.getText());
                    jsonstream.put("maxSplitCount", Integer.parseInt(maxSplitCounttextField.getText()));
                    jsonstream.put("splitTime", Integer.parseInt(splitTimetextField.getText()));

                    jsontaskInfo.put("stream", jsonstream);

                    //time
                    String strtaskType = taskTypecomboBox.getSelectedItem().toString();
                    jsontime.put("taskType", strtaskType);
                    if (strtaskType == "plan") {
                        JSONObject jsonplanInfo = new JSONObject();

                        JSONArray jsonexeTimeList = new JSONArray();
                        for (int i = 0; i < struplanInfo.exeTimeList.size(); i++) {
                            JSONObject jsonexeTime = new JSONObject();
                            jsonexeTime.put("day", struplanInfo.exeTimeList.get(i).day);

                            JSONArray jsontimeRangeList = new JSONArray();
                            for (int j = 0; j < struplanInfo.exeTimeList.get(i).timeRange.size(); j++) {
                                JSONObject jsontimeRange = new JSONObject();

                                jsontimeRange.put("startTime", struplanInfo.exeTimeList.get(i).timeRange.get(j).startTime);
                                jsontimeRange.put("endTime", struplanInfo.exeTimeList.get(i).timeRange.get(j).endTime);
                                jsontimeRange.put("algorithmType", struplanInfo.exeTimeList.get(i).timeRange.get(j).algorithmType);

                                jsontimeRangeList.put(j, jsontimeRange);
                            }
                            jsonexeTime.put("timeRange", jsontimeRangeList);

                            jsonexeTimeList.put(i, jsonexeTime);
                        }
                        jsonplanInfo.put("exeTimeList", jsonexeTimeList);

                        jsontime.put("planInfo", jsonplanInfo);
                    } else if (strtaskType == "temp") {
                        JSONObject jsontempInfo = new JSONObject();
                        jsontempInfo.put("startTime", tempInfostartTimetextField.getText());
                        jsontempInfo.put("startTime", tempInfoendTimetextField.getText());

                        jsontime.put("tempInfo", jsontempInfo);
                    }
                    jsontaskInfo.put("time", jsontime);

                    //destination
                    JSONArray jsondestinationList = new JSONArray();
                    for (int i = 0; i < destinationlist.size(); i++) {
                        JSONObject jsondestination = new JSONObject();
                        jsondestination.put("destinationType", destinationlist.get(i).destinationType);
                        jsondestination.put("destinationUrl", destinationlist.get(i).destinationUrl);
                        jsondestination.put("userName", destinationlist.get(i).userName);
                        jsondestination.put("password", destinationlist.get(i).passWord);
                        jsondestination.put("subscribeEvent", destinationlist.get(i).subscribeEvent);

                        jsondestinationList.put(i, jsondestination);
                    }
                    jsontaskInfo.put("destination", jsondestinationList);

                    //rule
                    JSONObject jsonrule = new JSONObject();
                    jsonrule.put("bigTargetModel", bigTargetModelcheckBox.isSelected());

                    JSONObject jsondetectRule = new JSONObject();

                    JSONArray jsondetectRegionlist = new JSONArray();
                    for (int i = 0; i < strdetectRule.detectRegion.size(); i++) {
                        JSONObject jsondetectRegion = new JSONObject();
                        jsondetectRegion.put("regionName", strdetectRule.detectRegion.get(i).regionName);

                        JSONArray jsonpointlist = new JSONArray();
                        for (int j = 0; j < strdetectRule.detectRegion.get(i).point.size(); j++) {
                            JSONObject jsonsinglepoint = new JSONObject();
                            jsonsinglepoint.put("x", strdetectRule.detectRegion.get(i).point.get(j).x);
                            jsonsinglepoint.put("y", strdetectRule.detectRegion.get(i).point.get(j).y);

                            jsonpointlist.put(j, jsonsinglepoint);
                        }
                        jsondetectRegion.put("point", jsonpointlist);

                        jsondetectRegionlist.put(i, jsondetectRegion);
                    }
                    jsondetectRule.put("detectRegion", jsondetectRegionlist);

                    jsonrule.put("detectRule", jsondetectRule);

                    JSONArray jsonmaskRegionlist = new JSONArray();
                    for (int i = 0; i < maskRegionlist.size(); i++) {
                        JSONObject jsonmaskRegion = new JSONObject();
                        jsonmaskRegion.put("regionName", maskRegionlist.get(i).regionName);

                        JSONArray jsonpointlist = new JSONArray();
                        for (int j = 0; j < maskRegionlist.get(i).point.size(); j++) {
                            JSONObject jsonsinglepoint = new JSONObject();
                            jsonsinglepoint.put("x", maskRegionlist.get(i).point.get(j).x);
                            jsonsinglepoint.put("y", maskRegionlist.get(i).point.get(j).y);

                            jsonpointlist.put(j, jsonsinglepoint);
                        }
                        jsonmaskRegion.put("point", jsonpointlist);

                        jsonmaskRegionlist.put(i, jsonmaskRegion);
                    }
                    jsonrule.put("maskRegion", jsonmaskRegionlist);

                    jsonrule.put("targetSpeed", Integer.parseInt(targetSpeedtextField.getText()));
                    jsonrule.put("captureTimes", Integer.parseInt(captureTimestextField.getText()));
                    jsonrule.put("sensitivity", Integer.parseInt(sensitivitytextField.getText()));
                    jsonrule.put("captureTimeInterval", Integer.parseInt(captureTimeIntervaltextField.getText()));
                    jsonrule.put("captureThreshold", Integer.parseInt(captureThresholdtextField.getText()));
                    jsonrule.put("misinformation", misinformationcheckBox.isSelected());

                    jsontaskInfo.put("rule", jsonrule);

                    //targetAttrs
                    JSONObject jsontargetAttrs = new JSONObject();
                    jsontargetAttrs.put("deviceId", deviceIdtextField.getText());
                    jsontargetAttrs.put("deviceChannel", Integer.parseInt(deviceChanneltextField.getText()));
                    jsontargetAttrs.put("deviceName", deviceNametextField.getText());

                    jsontaskInfo.put("targetAttrs", jsontargetAttrs);

                    jsonVideoAnalysisTaskInfo.put("taskInfo", jsontaskInfo);

                    JSONObject jsonRet = null;
                    try {
                        if (HttpsClientUtil.bHttpsEnabled) {
                            jsonRet = new JSONObject(HttpsClientUtil.httpsPost(strurl, jsonVideoAnalysisTaskInfo.toString()));
                        } else {
                            jsonRet = new JSONObject(HTTPClientUtil.doPost(strurl, jsonVideoAnalysisTaskInfo.toString(), null));
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // parse url
                    if (jsonRet.getString("errorCode").compareTo("1") != 0) {
                        //error info
                        JOptionPane.showMessageDialog(null, jsonRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    struplanInfo.exeTimeList.clear();
                    destinationlist.clear();
                    strdetectRule.detectRegion.clear();
                    maskRegionlist.clear();
                }
            }
        });
        TaskCommitBtn.setBounds(799, 156, 93, 23);
        contentPanel.add(TaskCommitBtn);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
        }
    }
}
