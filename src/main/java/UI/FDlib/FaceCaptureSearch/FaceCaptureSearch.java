package UI.FDlib.FaceCaptureSearch;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;


import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Color;


public class FaceCaptureSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_StartTime;
	private JTextField textField_StopTime;
	private JTextField textField_searchPostion;
	private JTextField textField_MaxResultNum;
	private JTextField textField_maxResult;
	
    private String strDeviceIP="";
    private String strDevicePort="";
	
    private String routePic="";
    private String modelData="";
    private String taskID="";

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			FaceCaptureSearch dialog = new FaceCaptureSearch("10.66.109.3","80");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public FaceCaptureSearch(String strIP,String strPort)
	{
		strDeviceIP = strIP;
		strDevicePort = strPort;
		
		InitDialog();
	}
	
	public void InitDialog()
	{
		
		
		 String strPrefix = "http://" + strDeviceIP + ":" + strDevicePort;
	        final String strUrl = strPrefix + "/ISAPI/SDT/Face/captureSearch";

	        
		setTitle("FaceCaptureSearch");
		setBounds(100, 100, 676, 496);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("StartTime");
		label.setBounds(20, 13, 77, 15);
		contentPanel.add(label);
		
		textField_StartTime = new JTextField();
		textField_StartTime.setText("2017-09-14 00:00:00");
		textField_StartTime.setColumns(10);
		textField_StartTime.setBounds(133, 10, 124, 21);
		contentPanel.add(textField_StartTime);
		
		JLabel label_1 = new JLabel("StopTime");
		label_1.setBounds(20, 50, 77, 15);
		contentPanel.add(label_1);
		
		textField_StopTime = new JTextField();
		textField_StopTime.setText("2017-09-14 23:59:59");
		textField_StopTime.setColumns(10);
		textField_StopTime.setBounds(133, 47, 124, 21);
		contentPanel.add(textField_StopTime);
		
		JLabel label_2 = new JLabel("Search Result Postion");
		label_2.setBounds(20, 94, 136, 15);
		contentPanel.add(label_2);
		
		textField_searchPostion = new JTextField();
		textField_searchPostion.setText("0");
		textField_searchPostion.setColumns(10);
		textField_searchPostion.setBounds(191, 91, 66, 21);
		contentPanel.add(textField_searchPostion);
		
		JLabel label_3 = new JLabel("Max Result Num");
		label_3.setBounds(20, 132, 136, 15);
		contentPanel.add(label_3);
		
		textField_MaxResultNum = new JTextField();
		textField_MaxResultNum.setText("500");
		textField_MaxResultNum.setColumns(10);
		textField_MaxResultNum.setBounds(191, 129, 66, 21);
		contentPanel.add(textField_MaxResultNum);
		
		JLabel label_4 = new JLabel("Max Result");
		label_4.setBounds(20, 174, 136, 15);
		contentPanel.add(label_4);
		
		textField_maxResult = new JTextField();
		textField_maxResult.setText("10");
		textField_maxResult.setColumns(10);
		textField_maxResult.setBounds(191, 171, 66, 21);
		contentPanel.add(textField_maxResult);
		
		JLabel label_8 = new JLabel("AgeGroup");
		label_8.setBounds(20, 205, 136, 15);
		contentPanel.add(label_8);
		
		final JComboBox comboBox_AgeGroup = new JComboBox();
		comboBox_AgeGroup.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "child", "young", "middle", "old"}));
		comboBox_AgeGroup.setSelectedIndex(0);
		comboBox_AgeGroup.setBounds(174, 202, 83, 21);
		contentPanel.add(comboBox_AgeGroup);
		
		JLabel label_9 = new JLabel("Gender");
		label_9.setBounds(20, 236, 136, 15);
		contentPanel.add(label_9);
		
		final JComboBox comboBox_Gender = new JComboBox();
		comboBox_Gender.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "male", "female"}));
		comboBox_Gender.setSelectedIndex(0);
		comboBox_Gender.setBounds(174, 233, 83, 21);
		contentPanel.add(comboBox_Gender);
		
		JLabel label_10 = new JLabel("Glass");
		label_10.setBounds(20, 267, 136, 15);
		contentPanel.add(label_10);
		
		final JComboBox comboBox_Class = new JComboBox();
		comboBox_Class.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Class.setSelectedIndex(0);
		comboBox_Class.setBounds(174, 264, 83, 21);
		contentPanel.add(comboBox_Class);
		
		JLabel label_11 = new JLabel("Smile");
		label_11.setBounds(20, 295, 136, 15);
		contentPanel.add(label_11);
		
		final JComboBox comboBox_Smile = new JComboBox();
		comboBox_Smile.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Smile.setSelectedIndex(0);
		comboBox_Smile.setBounds(174, 292, 83, 21);
		contentPanel.add(comboBox_Smile);
		
		JLabel label_12 = new JLabel("High Risk Population");
		label_12.setBounds(20, 323, 136, 15);
		contentPanel.add(label_12);
		
		final JComboBox comboBox_Population = new JComboBox();
		comboBox_Population.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Population.setSelectedIndex(0);
		comboBox_Population.setBounds(174, 320, 83, 21);
		contentPanel.add(comboBox_Population);
		
		JLabel lbl_InLibrary = new JLabel("In Library");
		lbl_InLibrary.setBounds(20, 354, 136, 15);
		contentPanel.add(lbl_InLibrary);
		
		final JComboBox comboBox_Inlibrary = new JComboBox();
		comboBox_Inlibrary.setModel(new DefaultComboBoxModel(new String[] {"unknown", "yes", "no"}));
		comboBox_Inlibrary.setSelectedIndex(0);
		comboBox_Inlibrary.setBounds(174, 351, 83, 21);
		contentPanel.add(comboBox_Inlibrary);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
		
		JLabel lbl_pic1 = new JLabel("");
		lbl_pic1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lbl_pic1.setBounds(314, 23, 316, 166);
		contentPanel.add(lbl_pic1);
		
		JLabel label_pic2 = new JLabel("");
		label_pic2.setBorder(new LineBorder(new Color(0, 0, 0)));
		label_pic2.setBounds(314, 205, 316, 166);
		contentPanel.add(label_pic2);
		
		final JLabel arrlabel[]=new JLabel[2];
		
		arrlabel[0]=lbl_pic1;
		
		arrlabel[1]=label_pic2;
		
         
		
		JButton button = new JButton("Search");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				 try {
	                    JSONObject jsonTask = new JSONObject();
	                    
	                    jsonTask.put("searchResultPosition",Integer.parseInt(textField_searchPostion.getText()));
	                    jsonTask.put("maxResults",Integer.parseInt(textField_maxResult.getText()));
	                    
	                    String strStart = textField_StartTime.getText();
	                    String strStop = textField_StopTime.getText();
	                    jsonTask.put("startTime", strStart.replace(" ", "T")+"Z");
	                    jsonTask.put("endTime",strStop.replace(" ", "T")+"Z");
	                    JSONArray channel = new JSONArray();
	                    JSONObject jsonChannel = new JSONObject();
	                    jsonChannel.put("channelID","");
	                    channel.put(0,jsonChannel);
	                    jsonTask.put("choiceChannel",channel);
	                   
	                    //unlmited 
	                    if(comboBox_AgeGroup.getSelectedItem() == "unlimited")
	                    {
	                    	jsonTask.put("ageGroup","");
	                    }
	                    else
	                    {
	                    	 jsonTask.put("ageGroup",comboBox_AgeGroup.getSelectedItem());
	                    }
	                    
	                    if(comboBox_Gender.getSelectedItem() == "unlimited")
	                    {
	                    	jsonTask.put("gender","");
	                    }
	                    else
	                    {
	                    	jsonTask.put("gender",comboBox_Gender.getSelectedItem());
	                    }
	                    if(comboBox_Class.getSelectedItem() == "unlimited")
	                    {
	                    	jsonTask.put("glasses","");
	                    }
	                    else
	                    {
	                    	 jsonTask.put("glasses",comboBox_Class.getSelectedItem());
	                    }
	                    if(comboBox_Smile.getSelectedItem() == "unlimited")
	                    {
	                    	jsonTask.put("smile","");
	                    }
	                    else
	                    {
	                    	 jsonTask.put("smile",comboBox_Smile.getSelectedItem());
	                    }
	                    if(comboBox_Population.getSelectedItem() == "unlimited")
	                    {
	                    	jsonTask.put("ethnic","");
	                    }
	                    else
	                    {
	                    	jsonTask.put("ethnic",comboBox_Population.getSelectedItem());
	                    }
	                    
	                    jsonTask.put("isInLibrary",comboBox_Inlibrary.getSelectedItem());
	                    
	                   

	                    JSONObject retData = new JSONObject(HTTPClientUtil.doPost(strUrl,jsonTask.toString(),null));	                   

	                    System.out.println(retData);
	                    String strPicUrl="";
	                    	                    
	                    JSONArray jsonArrayTargets = retData.getJSONArray("targets");
	                    for(int i = 0;i < jsonArrayTargets.length();i++){
	                        JSONObject singleTargets = jsonArrayTargets.getJSONObject(i);
	                        
	                        strPicUrl= singleTargets.getString("picUrl");
	                        System.out.println(strPicUrl);
	                        
	                        
	                        if(i<2)
	                        {
								int width = arrlabel[i].getWidth();
								int height = arrlabel[i].getHeight();
	
								Image img = null;
								try {
									URL picurl = new URL(strPicUrl);
									HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
									conn.setConnectTimeout(6000);
									conn.setDoInput(true);
									conn.setUseCaches(false);
									conn.connect();
									InputStream is = conn.getInputStream();
									img = ImageIO.read(is);
									is.close();
								} catch (Exception e) {
	
								}
								img = img.getScaledInstance(width, height, 0);
								arrlabel[i].setIcon(new ImageIcon(img));
								arrlabel[i].setVisible(true);
	                        }
	                    }
	                    
	                    
	                    
	                }catch (Exception e){
	                    e.printStackTrace();
	                }
				
			}
		});
		button.setBounds(86, 420, 83, 23);
		contentPanel.add(button);
		

		

		
		
	}
}
