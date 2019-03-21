package UI.FDlib.FaceAlarmSearch;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionEvent;

public class FaceAlarmSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_StartTime;
	private JTextField textField_StopTime;
	private JTextField textField_searchPostion;
	private JTextField textField_MaxResultNum;
	private JTextField textField_maxResult;
	private JTextField textField_SimilarityB;
	private JTextField textField_SimilarityM;
    private String strDeviceIP="";
    private String strDevicePort="";

	/**
	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			FaceAlarmSearch dialog = new FaceAlarmSearch();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	
	public FaceAlarmSearch(String strIP,String strPort)
	{
		strDeviceIP = strIP;
		strDevicePort = strPort;
		
		InitDialog();
	}
	
	public void InitDialog() {
		
		 String strPrefix = "http://" + strDeviceIP + ":" + strDevicePort;
	        final String strUrl = strPrefix + "/ISAPI/Intelligent/FDLib/FCSearch?format=json";
	        
		setBounds(100, 100, 679, 467);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBounds(10, 10, 660, 419);
		contentPanel.add(panel);
		
		JLabel label = new JLabel("StartTime");
		label.setBounds(20, 13, 77, 15);
		panel.add(label);
		
		textField_StartTime = new JTextField();
		textField_StartTime.setText("2017-09-14 00:00:00");
		textField_StartTime.setColumns(10);
		textField_StartTime.setBounds(133, 10, 124, 21);
		panel.add(textField_StartTime);
		
		JLabel label_1 = new JLabel("StopTime");
		label_1.setBounds(20, 50, 77, 15);
		panel.add(label_1);
		
		textField_StopTime = new JTextField();
		textField_StopTime.setText("2017-09-14 23:59:59");
		textField_StopTime.setColumns(10);
		textField_StopTime.setBounds(133, 47, 124, 21);
		panel.add(textField_StopTime);
		
		JLabel label_2 = new JLabel("Search Result Postion");
		label_2.setBounds(20, 94, 136, 15);
		panel.add(label_2);
		
		textField_searchPostion = new JTextField();
		textField_searchPostion.setText("0");
		textField_searchPostion.setColumns(10);
		textField_searchPostion.setBounds(191, 91, 66, 21);
		panel.add(textField_searchPostion);
		
		JLabel label_3 = new JLabel("Max Result Num");
		label_3.setBounds(20, 132, 136, 15);
		panel.add(label_3);
		
		textField_MaxResultNum = new JTextField();
		textField_MaxResultNum.setText("500");
		textField_MaxResultNum.setColumns(10);
		textField_MaxResultNum.setBounds(191, 129, 66, 21);
		panel.add(textField_MaxResultNum);
		
		JLabel label_4 = new JLabel("Max Result");
		label_4.setBounds(20, 174, 136, 15);
		panel.add(label_4);
		
		textField_maxResult = new JTextField();
		textField_maxResult.setText("10");
		textField_maxResult.setColumns(10);
		textField_maxResult.setBounds(191, 171, 66, 21);
		panel.add(textField_maxResult);
		
		JLabel lbl_pic1 = new JLabel("");
		lbl_pic1.setBorder(new LineBorder(new Color(0, 0, 0)));
		lbl_pic1.setBounds(314, 13, 316, 166);
		panel.add(lbl_pic1);
		
		JLabel lbl_pic2 = new JLabel("");
		lbl_pic2.setBorder(new LineBorder(new Color(0, 0, 0)));
		lbl_pic2.setBounds(314, 195, 316, 166);
		panel.add(lbl_pic2);
		final JLabel arrlabel[]=new JLabel[2];
		
		arrlabel[0]=lbl_pic1;
		
		arrlabel[1]=lbl_pic2;
		JButton button = new JButton("Search");
		//Face Alarm search
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
	                    JSONObject jsonTask = new JSONObject();
	                    
	                    jsonTask.put("searchResultPosition",Integer.parseInt(textField_searchPostion.getText()));
	                    jsonTask.put("maxResultNumber",Integer.parseInt(textField_MaxResultNum.getText()));
	                    jsonTask.put("maxResults",Integer.parseInt(textField_maxResult.getText()));
	                    
	                    String strStart = textField_StartTime.getText();
	                    String strStop = textField_StopTime.getText();
	                    jsonTask.put("snapStartTime", strStart.replace(" ", "T")+"Z");
	                    jsonTask.put("snapEndTime",strStop.replace(" ", "T")+"Z");
	                    jsonTask.put("minSimilarity",Double.parseDouble(textField_SimilarityB.getText()));
	                    jsonTask.put("maxSimilarity",Double.parseDouble(textField_SimilarityM.getText()));
	                    jsonTask.put("FDID","-1");
	                    jsonTask.put("cameraID","-1");
	                    jsonTask.put("sortType","similarity");
	                   

	                    JSONObject retData = new JSONObject(HTTPClientUtil.doPost(strUrl,jsonTask.toString(),null));	                   

	                    System.out.println(retData);
	                    String strPicUrl="";
	                    	                    
	                    JSONArray jsonArrayTargets = retData.getJSONArray("MatchList");
	                    for(int i = 0;i < jsonArrayTargets.length();i++){
	                        JSONObject singleTargets = jsonArrayTargets.getJSONObject(i);
	                        
	                        strPicUrl= singleTargets.getString("face_picurl");
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
		button.setBounds(82, 351, 83, 23);
		panel.add(button);
		
		JLabel label_5 = new JLabel("Similarity");
		label_5.setBounds(20, 227, 66, 15);
		panel.add(label_5);
		
		textField_SimilarityB = new JTextField();
		textField_SimilarityB.setText("0.30");
		textField_SimilarityB.setColumns(10);
		textField_SimilarityB.setBounds(103, 224, 66, 21);
		panel.add(textField_SimilarityB);
		
		JLabel label_6 = new JLabel("~");
		label_6.setBounds(174, 227, 27, 18);
		panel.add(label_6);
		
		textField_SimilarityM = new JTextField();
		textField_SimilarityM.setText("1.00");
		textField_SimilarityM.setColumns(10);
		textField_SimilarityM.setBounds(191, 224, 66, 21);
		panel.add(textField_SimilarityM);
	}
}
