package UI.FDlib.BlackFD;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;
import ISAPI.HttpsClientUtil;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.imageio.ImageIO;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class BlackFDlibManagement extends JDialog {

    /**
     * Launch the application.
     */
/*   public static void main(String[] args) {
		try {
			BlackFDlibManagement dialog = new BlackFDlibManagement();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			 GetFDlibList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

    public static class FDlibListStruct {
        public String FDID;
        public String faceLibType;
        public String name;
        public String customInfo;

    }

    public static class FacePicDataStruct {
        public String FDID;
        public String FPID;
        public String faceURL;
        public String name;
        public String gender;
        public String bornTime;
        public String city;
        public String certificateType;
        public String certificateNumber;
        public String caseInfo;
        public String tag;
        public String address;
        public String customInfo;
    }


    static List<FDlibListStruct> FDlibListData = new ArrayList<FDlibListStruct>();

    static List<FacePicDataStruct> FacePicDataList = new ArrayList<FacePicDataStruct>();


    public static void GetFDlibList(String strIP, String strPort) {
        String strUrl = "";
        if (HttpsClientUtil.bHttpsEnabled) {
            strUrl = "https://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity?format=json";
        } else {
            strUrl = "http://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity?format=json";
        }
        try {
            FDlibListData.clear();
            String strResult = "";
            if (HttpsClientUtil.bHttpsEnabled) {
                strResult = HttpsClientUtil.httpsGet(strUrl);
            } else {
                strResult = HTTPClientUtil.doGet(strUrl, null);
            }

            JSONObject retData = new JSONObject(strResult);
            JSONArray jsonArrayTargets = retData.getJSONArray("FDLibEntity");

            for (int i = 0; i < jsonArrayTargets.length(); i++) {
                JSONObject singleTargets = jsonArrayTargets.getJSONObject(i);
                FDlibListStruct single = new FDlibListStruct();

                single.FDID = singleTargets.getString("FDID");
                single.faceLibType = singleTargets.getString("faceLibType");
                single.name = singleTargets.getString("name");
                single.customInfo = singleTargets.getString("customInfo");

                FDlibListData.add(single);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void GetFacePicDataList(String strIP, String strPort, FDlibListStruct singleFD) {
        String strUrl = "";
        if (HttpsClientUtil.bHttpsEnabled) {
            strUrl = "https://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity/FDSearch?format=json";
        } else {
            strUrl = "http://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity/FDSearch?format=json";
        }

        try {
            FacePicDataList.clear();
            JSONObject jsonFacePicSearch = new JSONObject();
            jsonFacePicSearch.put("searchResultPosition", 0);
            jsonFacePicSearch.put("maxResults", 100);
            jsonFacePicSearch.put("faceLibType", "blackFD");
            jsonFacePicSearch.put("FDID", singleFD.FDID);
            String strResult = "";
            if (HttpsClientUtil.bHttpsEnabled) {
                strResult = HttpsClientUtil.httpsPost(strUrl, jsonFacePicSearch.toString());
            } else {
                strResult = HTTPClientUtil.doPost(strUrl, jsonFacePicSearch.toString(), null);
            }


            JSONObject retData = new JSONObject(strResult);
            JSONArray jsonArrayTargets = retData.getJSONArray("MatchList");

            for (int i = 0; i < jsonArrayTargets.length(); i++) {
                JSONObject singleTargets = jsonArrayTargets.getJSONObject(i);
                FacePicDataStruct singleTemp = new FacePicDataStruct();

                singleTemp.FDID = singleFD.FDID;
                singleTemp.FPID = singleTargets.getString("FPID");
                if (singleTemp.FPID == null) {
                    break;
                }
                singleTemp.faceURL = singleTargets.getString("faceURL");
                singleTemp.name = singleTargets.getString("name");
                singleTemp.gender = singleTargets.getString("gender");
                singleTemp.bornTime = singleTargets.getString("bornTime");
                singleTemp.city = singleTargets.getString("city");
                singleTemp.certificateType = singleTargets.getString("certificateType");
                singleTemp.certificateNumber = singleTargets.getString("certificateNumber");
                singleTemp.tag = singleTargets.getString("tag");
                singleTemp.address = singleTargets.getString("address");
                singleTemp.customInfo = singleTargets.getString("customInfo");

                FacePicDataList.add(singleTemp);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int m_selectedPicIndex = 0;

    public static void showPic(final JPanel m_panel) {
        int nSize = 8;
        m_panel.removeAll();

        JLabel[] arrlabel = new JLabel[nSize];

        for (int t = 0; t < nSize; t++) {
            arrlabel[t] = new JLabel();
            arrlabel[t].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {

                    for (int i = 0; i < m_panel.getComponentCount(); i++) {
                        if (m_panel.getComponent(i) == arg0.getComponent()) {
                            m_selectedPicIndex = i;
                            ((JLabel) arg0.getComponent()).setBorder(new LineBorder(Color.red));
                            //break;
                        } else {
                            ((JLabel) m_panel.getComponent(i)).setBorder(new LineBorder(Color.white));
                        }
                    }
                }
            });
            Image img = null;
            try {
                if (t >= FacePicDataList.size()) {
                    break;
                }
                URL picurl = new URL(FacePicDataList.get(t).faceURL);
                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection) picurl.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    img = ImageIO.read(is);
                    is.close();
                    img = img.getScaledInstance(180, 230, 0);
                    arrlabel[t].setIcon(new ImageIcon(img));

                    m_panel.add(arrlabel[t]);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    /**
     * Create the dialog.
     */
    public BlackFDlibManagement(final String strIP, final String strPort) {
        final JPanel FacePicDataPanel = new JPanel();
        getContentPane().setBackground(Color.WHITE);
        setBounds(100, 100, 1122, 753);
        getContentPane().setLayout(null);
        final JList FDliblist = new JList();
        FDliblist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                GetFacePicDataList(strIP, strPort, FDlibListData.get(FDliblist.getSelectedIndex()));
                showPic(FacePicDataPanel);

                FacePicDataPanel.updateUI();
                FacePicDataPanel.repaint();
            }
        });

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.GRAY));
        panel.setBounds(10, 10, 297, 56);
        getContentPane().add(panel);
        panel.setLayout(null);

        JButton btnFDlibEdit = new JButton("Edit");
        btnFDlibEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                FDlibListStruct single = new FDlibListStruct();

                single = FDlibListData.get(FDliblist.getAnchorSelectionIndex());
                FDlibEditForm fDlibEditForm = new FDlibEditForm(strIP, strPort, single);

                fDlibEditForm.setLocationRelativeTo(null);
                fDlibEditForm.setVisible(true);


                FDliblist.removeAll();
                GetFDlibList(strIP, strPort);
                FDliblist.setModel(new AbstractListModel() {
                    final String[] values = new String[FDlibListData.size()];

                    public int getSize() {
                        return values.length;
                    }

                    public Object getElementAt(int index) {
                        values[index] = FDlibListData.get(index).name;
                        return values[index];
                    }
                });

            }
        });
        btnFDlibEdit.setForeground(Color.BLACK);
        btnFDlibEdit.setBackground(new Color(135, 206, 250));
        btnFDlibEdit.setBounds(93, 10, 57, 36);
        panel.add(btnFDlibEdit);

        JButton btnFDlibDel = new JButton("Del");
        btnFDlibDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                String m_strFDID = FDlibListData.get(FDliblist.getSelectedIndex()).FDID;
                String url = "";
                if (HttpsClientUtil.bHttpsEnabled) {
                    url = "http://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity?format=json&FDID=" + m_strFDID + "&faceLibType=blackFD";
                } else {
                    url = "http://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity?format=json&FDID=" + m_strFDID + "&faceLibType=blackFD";
                }

                try {
                    String out = "";
                    if (HttpsClientUtil.bHttpsEnabled) {
                        out = HttpsClientUtil.httpsDelete(url);
                    } else {
                        out = HTTPClientUtil.doDelete(url, null);
                    }


                    FDliblist.removeAll();
                    GetFDlibList(strIP, strPort);
                    FDliblist.setModel(new AbstractListModel() {
                        final String[] values = new String[FDlibListData.size()];

                        public int getSize() {
                            return values.length;
                        }

                        public Object getElementAt(int index) {
                            values[index] = FDlibListData.get(index).name;
                            return values[index];
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btnFDlibDel.setForeground(Color.BLACK);
        btnFDlibDel.setBackground(new Color(135, 206, 250));
        btnFDlibDel.setBounds(174, 10, 57, 36);
        panel.add(btnFDlibDel);

        JButton btnFDlibAdd = new JButton("Add");
        btnFDlibAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FDlibAddForm fDlibAddForm = new FDlibAddForm(strIP, strPort);
                fDlibAddForm.setLocationRelativeTo(null);
                fDlibAddForm.setVisible(true);


                FDliblist.removeAll();
                GetFDlibList(strIP, strPort);
                FDliblist.setModel(new AbstractListModel() {
                    final String[] values = new String[FDlibListData.size()];

                    public int getSize() {
                        return values.length;
                    }

                    public Object getElementAt(int index) {
                        values[index] = FDlibListData.get(index).name;
                        return values[index];
                    }
                });
            }
        });
        btnFDlibAdd.setForeground(Color.BLACK);
        btnFDlibAdd.setBackground(new Color(135, 206, 250));
        btnFDlibAdd.setBounds(10, 10, 57, 36);
        panel.add(btnFDlibAdd);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(Color.GRAY));
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(317, 10, 779, 56);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JButton btnFacePicAdd = new JButton("Add");
        btnFacePicAdd.setForeground(Color.BLACK);
        btnFacePicAdd.setBackground(new Color(135, 206, 250));
        btnFacePicAdd.setBounds(555, 10, 57, 36);
        panel_1.add(btnFacePicAdd);

        JButton btnFacePicEdit = new JButton("Edit");

        btnFacePicEdit.setForeground(Color.BLACK);
        btnFacePicEdit.setBackground(new Color(135, 206, 250));
        btnFacePicEdit.setBounds(633, 10, 57, 36);
        panel_1.add(btnFacePicEdit);

        JButton btnFacePicDel = new JButton("Del");
        //Del Face Info
        btnFacePicDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String m_strFDID = FDlibListData.get(FDliblist.getSelectedIndex()).FDID;
                String strUrl = "";
                if (HttpsClientUtil.bHttpsEnabled) {
                    strUrl = "https://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity/FDSearch/Delete?format=json&FDID=" + m_strFDID + "&faceLibType=blackFD";
                } else {
                    strUrl = "http://" + strIP + ":" + strPort + "/ISAPI/Intelligent/FDLibEntity/FDSearch/Delete?format=json&FDID=" + m_strFDID + "&faceLibType=blackFD";
                }

                try {
                    JSONObject jsonSingleFDlib = new JSONObject();

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("value", FacePicDataList.get(m_selectedPicIndex).FPID);

                    JSONArray jsonarry = new JSONArray();
                    jsonarry.put(0, map);

                    jsonSingleFDlib.put("FPID", jsonarry);

                    String type = "application/x-www-form-urlencoded; charset=UTF-8";
                    String strResult = "";
                    if (HttpsClientUtil.bHttpsEnabled) {
                        strResult = HttpsClientUtil.doPutWithType(strUrl, jsonSingleFDlib.toString(), null, type);
                    } else {
                        strResult = HTTPClientUtil.doPutWithType(strUrl, jsonSingleFDlib.toString(), null, type);
                    }


                    GetFacePicDataList(strIP, strPort, FDlibListData.get(FDliblist.getSelectedIndex()));
                    showPic(FacePicDataPanel);

                    FacePicDataPanel.updateUI();
                    FacePicDataPanel.repaint();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btnFacePicDel.setForeground(Color.BLACK);
        btnFacePicDel.setBackground(new Color(135, 206, 250));
        btnFacePicDel.setBounds(712, 10, 57, 36);
        panel_1.add(btnFacePicDel);

        JPanel panel_2 = new JPanel();
        //add Face Info
        panel_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {


            }
        });
        panel_2.setBorder(new LineBorder(Color.GRAY));
        panel_2.setBackground(Color.WHITE);
        panel_2.setBounds(10, 76, 297, 629);
        getContentPane().add(panel_2);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));


        FDliblist.setBackground(Color.WHITE);
        GetFDlibList(strIP, strPort);
        FDliblist.removeAll();
        FDliblist.setModel(new AbstractListModel() {
            final String[] values = new String[FDlibListData.size()];

            public int getSize() {
                return values.length;
            }

            public Object getElementAt(int index) {
                values[index] = FDlibListData.get(index).name;
                return values[index];
            }
        });

        panel_2.add(FDliblist);


        FacePicDataPanel.setBorder(new LineBorder(Color.GRAY));
        FacePicDataPanel.setBackground(Color.WHITE);
        FacePicDataPanel.setBounds(317, 76, 779, 629);
        FacePicDataPanel.setAutoscrolls(true);
        getContentPane().add(FacePicDataPanel);
        FacePicDataPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        GetFacePicDataList(strIP, strPort, FDlibListData.get(0));

        showPic(FacePicDataPanel);
        FDliblist.setSelectedIndex(0);

        //Modify Face info UI
        btnFacePicEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                FacePicDataStruct singletemp = FacePicDataList.get(m_selectedPicIndex);
                FacePicEditForm facePicEditForm = new FacePicEditForm(strIP, strPort, singletemp);
                facePicEditForm.setLocationRelativeTo(null);
                facePicEditForm.setVisible(true);

                GetFacePicDataList(strIP, strPort, FDlibListData.get(FDliblist.getSelectedIndex()));
                showPic(FacePicDataPanel);

                FacePicDataPanel.updateUI();
                FacePicDataPanel.repaint();
            }
        });


        //Add Face Info Dialog
        btnFacePicAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FaceAddForm faceAddForm = new FaceAddForm(strIP, strPort, FDlibListData.get(FDliblist.getSelectedIndex()).FDID);
                faceAddForm.setVisible(true);


//				FacePicDataStruct singletemp=FacePicDataList.get(m_selectedPicIndex);			
//				FacePicEditForm facePicEditForm=new FacePicEditForm(strIP,strPort,singletemp);
//				facePicEditForm.setLocationRelativeTo(null);
//				facePicEditForm.setVisible(true);
//
                GetFacePicDataList(strIP, strPort, FDlibListData.get(FDliblist.getSelectedIndex()));
                showPic(FacePicDataPanel);

                FacePicDataPanel.updateUI();
                FacePicDataPanel.repaint();
            }
        });
    }


}
