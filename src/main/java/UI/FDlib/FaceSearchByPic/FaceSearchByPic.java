package UI.FDlib.FaceSearchByPic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;

import ISAPI.HTTPClientUtil;

import java.awt.Color;
import javax.swing.JTextField;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
public class FaceSearchByPic extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField_StartTime;
	private JTextField textField_StopTime;
	private JTextField textField_searchPostion;
	private JTextField textField_MaxResultNum;
	private JTextField textField_maxResult;
	private JTextField textField_SimilarityB;
	private JTextField textField_SimilarityE;
	
	
    private String strDeviceIP="";
    private String strDevicePort="";
    
    private String routePic="";
    private String modelData="";
    private String taskID="";

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			FaceSearchByPic dialog = new FaceSearchByPic();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	
	class PicFileFilter extends FileFilter {    
	    public String getDescription() {    
	        return "*.jpg;*.gif;*.bmp;*.png";    
	    }    
	    
	    public boolean accept(File file) {    
	        String name = file.getName();    
	        return file.isDirectory() || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".gif")||name.toLowerCase().endsWith(".bmp")||name.toLowerCase().endsWith(".png");  // ����ʾĿ¼��bmp��png�ļ�  
	    }    
	}    
	
	/**
	 * Create the dialog.
	 */
	
	public FaceSearchByPic(String strIp,String strPort)
	{
		strDeviceIP = strIp;
		strDevicePort = strPort;
		InitDlg();
	}
	
	public void InitDlg() {
		
		
		 String strPrefix = "http://" + strDeviceIP + ":" + strDevicePort;
	        final String strUrl = strPrefix + "/ISAPI/Intelligent/uploadStorageCloud?format=json";
	        final String strTaskUrl = strPrefix + "/ISAPI/SDT/Face/searchByPic";
	        final String strFaceAnalysisUrl = strPrefix + "/ISAPI/SDT/Face/pictureAnalysis";
	        final String strTaskProgressUrl = strPrefix + "/ISAPI/SDT/Face/searchByPic/progress?taskID=";
	        final String strSearchResUrl = strPrefix + "/ISAPI/SDT/Face/searchByPic/result?taskID=";

	        
	        
		setTitle("FaceSearchByPic");
		setBounds(100, 100, 595, 701);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			final JLabel lbl_pic = new JLabel("");
			lbl_pic.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					
					JFileChooser fc = new JFileChooser();
					PicFileFilter picFilter = new PicFileFilter(); //ͼƬ������    
					fc.addChoosableFileFilter(picFilter);  
					fc.setFileFilter(picFilter);  
					String filepath="";
					  
					if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
						 filepath = fc.getSelectedFile().getPath();  
						 String filename = fc.getSelectedFile().getName(); 
		                    
		                    filepath=filepath.replace("\\", "\\\\");
		                    
		                    int width = lbl_pic.getWidth(); 
		                    int height = lbl_pic.getHeight(); 
		                    
		                    
		                    Image image = new ImageIcon(filepath).getImage();
		                    image = image.getScaledInstance(width, height, 0);
		                    lbl_pic.setIcon(new ImageIcon(image));
		                    	            		
		                    lbl_pic.setVisible(true);
		                    	                	                                       
		                    
		                    System.out.println("path:" + filepath);  
		                    System.out.println("name:" + filename); 	                    
					     
					}  
					else
					{
						return;
					}
	                
	                
	                File file = new File(filepath);
	                
	                if(file == null)  return;

	                JSONObject jsonData = new JSONObject();
	                JSONObject jsonpictureAnalysis = new JSONObject();
	                String strPic = "";
	                try{
	                	 //upload pic
	                	 FileInputStream uploadPic = new FileInputStream(file);
	     	            byte[] bytePic = new byte[uploadPic.available()];
	     	            uploadPic.read(bytePic);
	     	            strPic = new String(bytePic, "ISO-8859-1");
	                    jsonData.put("FDID","1");
	                    jsonData.put("storageType","dynamic");	                  
	                    JSONObject jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
	                    routePic = jsonRet.getString("URL");
	                    System.out.println(routePic);
	                    jsonpictureAnalysis.put("imagesType","URL");
	                    jsonpictureAnalysis.put("imagesData",routePic);	
	                    jsonpictureAnalysis.put("algorithmType","faceModel");
	                   
	                    JSONObject jsonpictureAnalysisRet = new JSONObject(HTTPClientUtil.doPost(strFaceAnalysisUrl, jsonpictureAnalysis.toString(),null));
	                   
	                    JSONArray arrTargets = new JSONArray();
	                    arrTargets = jsonpictureAnalysisRet.getJSONArray("targets");
	                    JSONObject singleTarget = new JSONObject();
	                    singleTarget = arrTargets.getJSONObject(0);	                   
	                    modelData = singleTarget.getString("targetModelData");
	                    
	                }catch (Exception e){
	                    e.printStackTrace();
	                }
	               
	                
	               // routePic = HTTPClientUtil.doPostPicture(strUrl,jsonData,"---------------------------35071184679168",file);
	              
	               // modelData = HTTPClientUtil.doFacePicAnalysis_simple(strFaceAnalysisUrl,routePic);
	                	               
				}
			});
			lbl_pic.setBorder(new LineBorder(new Color(0, 0, 0)));
			lbl_pic.setBounds(10, 10, 279, 180);
			contentPanel.add(lbl_pic);
		}
		
		JLabel lblNewLabel = new JLabel("StartTime");
		lblNewLabel.setBounds(20, 208, 77, 15);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("StopTime");
		lblNewLabel_1.setBounds(20, 245, 77, 15);
		contentPanel.add(lblNewLabel_1);
		
		textField_StartTime = new JTextField();
		textField_StartTime.setBounds(133, 205, 124, 21);
		contentPanel.add(textField_StartTime);
		textField_StartTime.setColumns(10);
		
        Date d = new Date();  
        System.out.println(d);  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateNowStr = sdf.format(d);  
        System.out.println("��ʽ��������ڣ�" + dateNowStr);
        
        
        String strDateStart=dateNowStr.substring(0, 10)+" 00:00:00";
       // strDate.format("%d-%d-%d", d.getYear(),d.getMonth(),d.getDate());
        
        System.out.println("strDate��" + strDateStart);
        
        textField_StartTime.setText(strDateStart);
		
		textField_StopTime = new JTextField();
		textField_StopTime.setColumns(10);
		textField_StopTime.setBounds(133, 242, 124, 21);
		contentPanel.add(textField_StopTime);
		
        String strDateStop=dateNowStr.substring(0, 10)+" 23:59:59";
		
		textField_StopTime.setText(strDateStop);
		
		JLabel lblSearchresultpostion = new JLabel("Search Result Postion");
		lblSearchresultpostion.setBounds(20, 289, 136, 15);
		contentPanel.add(lblSearchresultpostion);
		
		textField_searchPostion = new JTextField();
		textField_searchPostion.setText("0");
		textField_searchPostion.setColumns(10);
		textField_searchPostion.setBounds(191, 286, 66, 21);
		contentPanel.add(textField_searchPostion);
		
		JLabel lblMaxresultnum = new JLabel("Max Result Num");
		lblMaxresultnum.setBounds(20, 327, 136, 15);
		contentPanel.add(lblMaxresultnum);
		
		textField_MaxResultNum = new JTextField();
		textField_MaxResultNum.setText("500");
		textField_MaxResultNum.setColumns(10);
		textField_MaxResultNum.setBounds(191, 324, 66, 21);
		contentPanel.add(textField_MaxResultNum);
		
		JLabel lblMaxResult = new JLabel("Max Result");
		lblMaxResult.setBounds(20, 369, 136, 15);
		contentPanel.add(lblMaxResult);
		
		textField_maxResult = new JTextField();
		textField_maxResult.setText("10");
		textField_maxResult.setColumns(10);
		textField_maxResult.setBounds(191, 366, 66, 21);
		contentPanel.add(textField_maxResult);
		
		JLabel lblNewLabel_2 = new JLabel("Similarity");
		lblNewLabel_2.setBounds(20, 405, 66, 15);
		contentPanel.add(lblNewLabel_2);
		
		textField_SimilarityB = new JTextField();
		textField_SimilarityB.setText("0.30");
		textField_SimilarityB.setBounds(103, 402, 66, 21);
		contentPanel.add(textField_SimilarityB);
		textField_SimilarityB.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("~");
		lblNewLabel_3.setBounds(174, 405, 27, 18);
		contentPanel.add(lblNewLabel_3);
		
		textField_SimilarityE = new JTextField();
		textField_SimilarityE.setText("1.00");
		textField_SimilarityE.setBounds(191, 402, 66, 21);
		contentPanel.add(textField_SimilarityE);
		textField_SimilarityE.setColumns(10);
		
		JLabel lblAgegroup = new JLabel("AgeGroup");
		lblAgegroup.setBounds(20, 451, 136, 15);
		contentPanel.add(lblAgegroup);
		
		final JComboBox comboBox_AgeGroup = new JComboBox();
		comboBox_AgeGroup.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "child", "young", "middle", "old"}));
		comboBox_AgeGroup.setSelectedIndex(0);
		comboBox_AgeGroup.setBounds(174, 448, 83, 21);
		contentPanel.add(comboBox_AgeGroup);
		
		JLabel lblGendle = new JLabel("Gender");
		lblGendle.setBounds(20, 482, 136, 15);
		contentPanel.add(lblGendle);
		
		final JComboBox comboBox_Gender = new JComboBox();
		comboBox_Gender.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "male", "female"}));
		comboBox_Gender.setSelectedIndex(0);
		comboBox_Gender.setBounds(174, 479, 83, 21);
		contentPanel.add(comboBox_Gender);
		
		JLabel lblClass = new JLabel("Glass");
		lblClass.setBounds(20, 513, 136, 15);
		contentPanel.add(lblClass);
		
		final JComboBox comboBox_Class = new JComboBox();
		comboBox_Class.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Class.setSelectedIndex(0);
		comboBox_Class.setBounds(174, 510, 83, 21);
		contentPanel.add(comboBox_Class);
		
		JLabel lblSmile = new JLabel("Smile");
		lblSmile.setBounds(20, 541, 136, 15);
		contentPanel.add(lblSmile);
		
		final JComboBox comboBox_Smile = new JComboBox();
		comboBox_Smile.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Smile.setSelectedIndex(0);
		comboBox_Smile.setBounds(174, 538, 83, 21);
		contentPanel.add(comboBox_Smile);
		
		JLabel lblHighRiskPopulation = new JLabel("ethnic");
		lblHighRiskPopulation.setBounds(20, 569, 136, 15);
		contentPanel.add(lblHighRiskPopulation);
		
		final JComboBox comboBox_Population = new JComboBox();
		comboBox_Population.setModel(new DefaultComboBoxModel(new String[] {"unlimited", "yes", "no"}));
		comboBox_Population.setSelectedIndex(0);
		comboBox_Population.setBounds(174, 566, 83, 21);
		contentPanel.add(comboBox_Population);
		
		JLabel m_picture1 = new JLabel("");
		m_picture1.setBorder(new LineBorder(new Color(0, 0, 0)));
		m_picture1.setBounds(314, 10, 254, 180);
		contentPanel.add(m_picture1);
		
		JLabel m_picture2 = new JLabel("");
		m_picture2.setBorder(new LineBorder(new Color(0, 0, 0)));
		m_picture2.setBounds(314, 223, 255, 166);

		
		contentPanel.add(m_picture2);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
		
		final JLabel arrlabel[]=new JLabel[2];
		arrlabel[0]=m_picture1;
	    arrlabel[1]=m_picture2;
			
		JButton btn_Search = new JButton("Search");
		btn_Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(routePic == ""){
					Object[] options = { "OK", "CANCEL" }; 
                    JOptionPane.showOptionDialog(null, "Please Select Image File!", "Warning", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                    null, options, options[0]);
                    return;
                }
                try {
                    JSONObject jsonTask = new JSONObject();
                    
                    String strStart = textField_StartTime.getText();
                    String strStop = textField_StopTime.getText();
                    jsonTask.put("startTime", strStart.replace(" ", "T")+"Z");
                    jsonTask.put("endTime",strStop.replace(" ", "T")+"Z");
                    JSONArray channel = new JSONArray();
                    JSONObject jsonChannel = new JSONObject();
                    jsonChannel.put("channelID","");
                    channel.put(0,jsonChannel);
                    jsonTask.put("choiceChannel",channel);
                    jsonTask.put("similarityMin",Double.parseDouble(textField_SimilarityB.getText()));
                    jsonTask.put("similarityMax",Double.parseDouble(textField_SimilarityE.getText()));
                   // jsonTask.put("age",Integer.parseInt(textField_Age.getText()));
                   
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
                   
                    
                    jsonTask.put("modelMaxNum",100);
                    jsonTask.put("targetModelData",modelData);

                    JSONObject jsonTaskID = new JSONObject(HTTPClientUtil.doPost(strTaskUrl,jsonTask.toString(),null));
                    
                    System.out.println("------------jsonTask.toString()="+jsonTask.toString());
                    taskID = jsonTaskID.getString("taskID");
                    //System.out.println(HTTPClientUtil.doGet(strTaskProgressUrl+taskID));
                    JSONObject jsonProgress = new JSONObject(HTTPClientUtil.doGet(strTaskProgressUrl+taskID,null));
                    String strProgress = jsonProgress.getString("progress");
                    JSONObject jsonQueryRes = new JSONObject();
                    jsonQueryRes.put("searchResultPosition",Integer.parseInt(textField_searchPostion.getText()));
                    jsonQueryRes.put("maxResults",Integer.parseInt(textField_maxResult.getText()));
                    JSONObject retData = new JSONObject(HTTPClientUtil.doPost(strSearchResUrl + taskID,jsonQueryRes.toString(),null));
                    System.out.println(retData);
                    //parse returned data
                    String strMatchNum = retData.getString("numOfMatches");
                    if(Integer.parseInt(strMatchNum) != 0)
                    {
                    	 JSONArray arrTargets = new JSONArray();
                         arrTargets = retData.getJSONArray("targets");
                         String strsubpicUrl = "";
                         for(int i = 0; i < arrTargets.length(); ++i)
                         {
                         	JSONObject singleTargets = arrTargets.getJSONObject(i);
                         	strsubpicUrl =  singleTargets.getString("subpicUrl");
                         	System.out.println(strsubpicUrl);
                         	if(i<2)
                             {
     							int width = arrlabel[i].getWidth();
     							int height = arrlabel[i].getHeight();

     							Image img = null;
     							try {
     								URL picurl = new URL(strsubpicUrl);
     								HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
     								conn.setConnectTimeout(6000);
     								conn.setDoInput(true);
     								conn.setUseCaches(false);
     								conn.connect();
     								InputStream is = conn.getInputStream();
     						
     								img = ImageIO.read(is);
     								is.close();
     							} catch (Exception e) {
     								
     								e.printStackTrace();
     							}
     							img = img.getScaledInstance(width, height, 0);
     							arrlabel[i].setIcon(new ImageIcon(img));
     							arrlabel[i].setVisible(true);
                             }
                         }
                    }
                    else 
                    {
                    	 JOptionPane.showMessageDialog(null, "NO MATCHES", "Info", JOptionPane.NO_OPTION);
                    }
                   
                }catch (Exception e){
                    e.printStackTrace();
                }
			}
		});
		btn_Search.setBounds(86, 607, 83, 23);
		contentPanel.add(btn_Search);
		
		
	}
}
