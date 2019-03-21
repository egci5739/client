package ISAPI;

import UI.FDlib.AlarmHost.AlarmHost;
import UI.FDlib.BlackFD.BlackFDlibManagement;
import UI.FDlib.Compare1V1;
import UI.FDlib.FaceAlarmSearch.FaceAlarmSearch;
import UI.FDlib.FaceCaptureSearch.FaceCaptureSearch;
import UI.FDlib.FaceSearchByPic.FaceSearchByPic;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.util.Iterator;


public class MainForm extends JDialog {

    private final JPanel contentPanel = new JPanel();
    public JTextField textFieldUrl;
    private JTextField IPtextField;
    private JTextField UsertextField;
    private JTextField PasstextField;
    private JTextField PorttextField;


    private String strDeviceIP;
    private String strDevicePort;


    //启动服务，监听来自客户端的请求

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            MainForm dialog = new MainForm();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public MainForm() {
        setBounds(100, 100, 1071, 611);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(175, 238, 238));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        final JCheckBox chckbxHttps = new JCheckBox("Https");
        JLabel lblUrl = new JLabel("URL");
        lblUrl.setBounds(23, 52, 24, 15);
        contentPanel.add(lblUrl);

        JLabel lblMethod = new JLabel("Method");
        lblMethod.setBounds(447, 52, 54, 15);
        contentPanel.add(lblMethod);

        textFieldUrl = new JTextField();
        textFieldUrl.setBounds(46, 49, 323, 21);
        contentPanel.add(textFieldUrl);
        textFieldUrl.setColumns(10);

        final JComboBox comboBox = new JComboBox();
        comboBox.setBackground(Color.WHITE);
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"GET", "PUT", "POST", "DELETE"}));
        comboBox.setToolTipText("");
        comboBox.setBounds(511, 49, 125, 21);
        contentPanel.add(comboBox);

        JButton btnExecute = new JButton("Execute");
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });

        btnExecute.setBackground(Color.WHITE);
        btnExecute.setBounds(818, 48, 93, 23);
        contentPanel.add(btnExecute);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(23, 103, 439, 418);
        contentPanel.add(scrollPane_1);

        final JTextPane inboundtextPane = new JTextPane();
        scrollPane_1.setViewportView(inboundtextPane);
        inboundtextPane.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(511, 103, 439, 418);
        contentPanel.add(scrollPane);

        final JTextPane textPanereturn = new JTextPane();
        scrollPane.setViewportView(textPanereturn);
        textPanereturn.setBackground(Color.WHITE);

        JLabel lblIn = new JLabel("IN");
        lblIn.setBounds(23, 75, 54, 15);
        contentPanel.add(lblIn);

        JLabel lblOut = new JLabel("OUT");
        lblOut.setBounds(511, 75, 54, 15);
        contentPanel.add(lblOut);

        JLabel lblIp = new JLabel("IP");
        lblIp.setBounds(23, 20, 54, 15);
        contentPanel.add(lblIp);

        JLabel lblUser = new JLabel("User");
        lblUser.setBounds(217, 20, 54, 15);
        contentPanel.add(lblUser);
        JLabel lblPassword = new JLabel("PassWord");
        lblPassword.setBounds(450, 20, 54, 15);
        contentPanel.add(lblPassword);
        IPtextField = new JTextField();
        IPtextField.setText("10.66.78.141");
        IPtextField.setColumns(10);
        IPtextField.setBounds(46, 17, 161, 21);
        contentPanel.add(IPtextField);
        UsertextField = new JTextField();
        UsertextField.setText("admin");
        UsertextField.setColumns(10);
        UsertextField.setBounds(249, 18, 161, 21);
        contentPanel.add(UsertextField);
        PasstextField = new JTextField();
        PasstextField.setText("Sdk2016+");
        PasstextField.setColumns(10);
        PasstextField.setBounds(511, 17, 161, 21);
        contentPanel.add(PasstextField);

        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(659, 52, 80, 23);
        contentPanel.add(lblPort);

        PorttextField = new JTextField();
        PorttextField.setText("443");
        PorttextField.setColumns(10);
        PorttextField.setBounds(699, 52, 103, 23);
        contentPanel.add(PorttextField);

        JButton btnLogin = new JButton("Login");
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                //添加凭证
                HttpsClientUtil.bHttpsEnabled = chckbxHttps.isSelected();
                if (HttpsClientUtil.bHttpsEnabled) {
                    strDevicePort = PorttextField.getText();
                    HttpsClientUtil.httpsClientInit(IPtextField.getText(), Integer.parseInt(strDevicePort), UsertextField.getText(), PasstextField.getText());
                } else {
                    strDevicePort = "80";
                    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(UsertextField.getText(), PasstextField.getText());
                    HTTPClientUtil.client.getState().setCredentials(AuthScope.ANY, creds);
                }

                //登录校验代码待补充
                String strUrl = "/ISAPI/Security/userCheck";
                try {
                    String strOut = "";

                    if (HttpsClientUtil.bHttpsEnabled) {
                        strOut = HttpsClientUtil.httpsGet("https://" + IPtextField.getText() + ":" + PorttextField.getText() + strUrl);
                    } else {
                        strOut = HTTPClientUtil.doGet("http://" + IPtextField.getText() + ":" + PorttextField.getText() + strUrl, null);
                    }

                    System.out.println(strOut);
                    SAXReader saxReader = new SAXReader();
                    try {

                        Document document = saxReader.read(new ByteArrayInputStream(strOut.getBytes("UTF-8")));
                        Element employees = document.getRootElement();

                        for (Iterator i = employees.elementIterator(); i.hasNext(); ) {

                            Element employee = (Element) i.next();

                            if (employee.getName() == "statusValue" && 0 == employee.getText().compareTo("200")) {

                                JOptionPane.showMessageDialog(null, "login success", "Information", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                        //login fail
                        JOptionPane.showMessageDialog(null, "login fail", "Error", JOptionPane.ERROR_MESSAGE);

                    } catch (DocumentException e) {
                        JOptionPane.showMessageDialog(null, "login fail", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println(e.getMessage());
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // JOptionPane.showMessageDialog(null, "login success", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        btnLogin.setBounds(808, 16, 93, 23);
        contentPanel.add(btnLogin);

        JScrollBar scrollBar = new JScrollBar();
        scrollBar.setBounds(933, 103, 17, 418);
        contentPanel.add(scrollBar);


        chckbxHttps.setBackground(new Color(153, 255, 255));
        chckbxHttps.setBounds(699, 16, 103, 23);
        contentPanel.add(chckbxHttps);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("FDLib");
        menuBar.add(mnNewMenu);

        JMenuItem mntmNewMenuItem = new JMenuItem("1V1");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {


                strDeviceIP = IPtextField.getText();

                //strDevicePort = "80";

                Compare1V1 dlgComp = new Compare1V1(strDeviceIP, strDevicePort);

                dlgComp.setBounds(100, 100, 850, 600);
                dlgComp.setVisible(true);

            }
        });
        mnNewMenu.add(mntmNewMenuItem);
        //Black FD
        JMenuItem mntmNewMenuItem_3 = new JMenuItem("BlackFD");
        mntmNewMenuItem_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                strDeviceIP = IPtextField.getText();

                //strDevicePort = "80";

                BlackFDlibManagement dlgComp = new BlackFDlibManagement(strDeviceIP, strDevicePort);
                dlgComp.setLocationRelativeTo(null);
                //dlgComp.setBounds(100,100 , 850, 600);
                dlgComp.setVisible(true);
            }
        });
        mnNewMenu.add(mntmNewMenuItem_3);
        JMenuItem FaceSearchByPic = new JMenuItem("FaceSearchByPic");
        FaceSearchByPic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                strDeviceIP = IPtextField.getText();
                // strDevicePort = "80";
                UI.FDlib.FaceSearchByPic.FaceSearchByPic dlgFace = new FaceSearchByPic(strDeviceIP, strDevicePort);
                dlgFace.setBounds(100, 100, 850, 700);
                dlgFace.setVisible(true);
            }
        });
        mnNewMenu.add(FaceSearchByPic);
        JMenuItem mntmNewMenuItem_1 = new JMenuItem("FaceCaptureSearch");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                strDeviceIP = IPtextField.getText();

                //strDevicePort = "80";

                FaceCaptureSearch dlgFace = new FaceCaptureSearch(strDeviceIP, strDevicePort);

                dlgFace.setBounds(100, 100, 700, 500);
                dlgFace.setVisible(true);
            }
        });
        mnNewMenu.add(mntmNewMenuItem_1);

        //Alarm Host
        JMenuItem mntmAlarmhost = new JMenuItem("AlarmHost");
        mntmAlarmhost.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                strDeviceIP = IPtextField.getText();

                //strDevicePort = "80";

                AlarmHost dlgAlarmHost = new AlarmHost(strDeviceIP, strDevicePort);

                dlgAlarmHost.setBounds(100, 100, 700, 500);
                dlgAlarmHost.setVisible(true);
            }
        });
        mnNewMenu.add(mntmAlarmhost);

        //Face Alarm Search
        JMenuItem mntmFacealarmsearch = new JMenuItem("FaceAlarmSearch");
        mntmFacealarmsearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                strDeviceIP = IPtextField.getText();

                //strDevicePort = "80";

                FaceAlarmSearch dlgFaceAlarmSearch = new FaceAlarmSearch(strDeviceIP, strDevicePort);

                dlgFaceAlarmSearch.setBounds(100, 100, 700, 500);
                dlgFaceAlarmSearch.setVisible(true);
            }
        });
        mnNewMenu.add(mntmFacealarmsearch);

        JMenu mnLis = new JMenu("ListenServer");
        menuBar.add(mnLis);

        JMenuItem mntmHost = new JMenuItem("Host");
        mntmHost.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

            }
        });
        mnLis.add(mntmHost);

        JMenuItem mntmRecv = new JMenuItem("Recv");
        mntmRecv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                ListenServer_Recv dlgComp = new ListenServer_Recv();

                dlgComp.setVisible(true);
            }
        });
        mnLis.add(mntmRecv);

        JMenu mnAnalysisTask = new JMenu("Analysis Task");
        menuBar.add(mnAnalysisTask);

        JMenuItem mntmTaskInfo = new JMenuItem("Task Info");
        mntmTaskInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //增加Analysis Tasks
            }
        });
        mnAnalysisTask.add(mntmTaskInfo);

        JMenu mnVideoAnalysis = new JMenu("Video Analysis");
        menuBar.add(mnVideoAnalysis);

        JMenuItem mntmVideoAnalysisTask = new JMenuItem("Video Analysis Task Commit");
        mntmVideoAnalysisTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                VideoAnalysisTaskForm videoAnalysisTaskForm = new VideoAnalysisTaskForm();
                videoAnalysisTaskForm.show();
            }
        });
        mnVideoAnalysis.add(mntmVideoAnalysisTask);

        btnExecute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                String surl = textFieldUrl.getText();
                String smethord = comboBox.getSelectedItem().toString();
                String sinbound = inboundtextPane.getText();
                String out = "";
                JsonFormatTool JsonFormatTool = new JsonFormatTool();
                //JOptionPane.showMessageDialog(null, sinbound, "Information", JOptionPane.INFORMATION_MESSAGE);

                if (smethord == "GET") {
                    try {
                        if (HttpsClientUtil.bHttpsEnabled) {
                            out = HttpsClientUtil.httpsGet("https://" + IPtextField.getText() + ":" + surl);
                        } else {
                            out = HTTPClientUtil.doGet("http://" + IPtextField.getText() + ":" + surl, null);
                        }

                        textPanereturn.setText(JsonFormatTool.formatJson(out));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (smethord == "PUT") {
                    try {
                        if (HttpsClientUtil.bHttpsEnabled) {
                            out = HttpsClientUtil.httpsPut("https://" + IPtextField.getText() + ":" + surl, sinbound);
                        } else {
                            out = HTTPClientUtil.doPut("http://" + IPtextField.getText() + ":" + surl, sinbound, null);
                        }

                        textPanereturn.setText(JsonFormatTool.formatJson(out));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (smethord == "POST") {
                    try {
                        if (HttpsClientUtil.bHttpsEnabled) {
                            out = HttpsClientUtil.httpsPost("https://" + IPtextField.getText() + ":" + surl, sinbound);
                        } else {
                            out = HTTPClientUtil.doPost("http://" + IPtextField.getText() + ":" + surl, sinbound, null);
                        }
                        textPanereturn.setText(JsonFormatTool.formatJson(out));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (smethord == "DELETE") {
                    try {
                        if (HttpsClientUtil.bHttpsEnabled) {
                            out = HttpsClientUtil.httpsDelete("https://" + IPtextField.getText() + ":" + surl);
                        } else {
                            out = HTTPClientUtil.doDelete("http://" + IPtextField.getText() + ":" + surl, null);
                        }

                        textPanereturn.setText(JsonFormatTool.formatJson(out));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                System.out.println(sinbound);
            }
        });

    }
}
