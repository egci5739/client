package UI.FDlib.BlackFD;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.border.LineBorder;

import org.json.JSONException;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;
import ISAPI.HttpsClientUtil;
public class FaceAddForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldBron;
	private JTextField textFieldCity;
	private JTextField textFieldTag;
	private JTextField textFieldcno;
	private JTextField textFieldURL;
	public static String filepath="";
	private JSONObject jsonAddDataRet;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			FaceAddForm dialog = new FaceAddForm();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public FaceAddForm(final String strIP, final String strPort, final String strFDID) {
		setBounds(100, 100, 576, 505);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		final JLabel FacePicBox = new JLabel("");
		FacePicBox.setBorder(new LineBorder(Color.gray));
		FacePicBox.setBackground(Color.WHITE);
		FacePicBox.setBounds(20, 13, 242, 274);
		contentPanel.add(FacePicBox);
		
		JLabel label_1 = new JLabel("Name");
		label_1.setBounds(272, 13, 54, 15);
		contentPanel.add(label_1);
		
		textFieldName = new JTextField();
		textFieldName.setText((String) null);
		textFieldName.setColumns(10);
		textFieldName.setBounds(379, 10, 130, 21);
		contentPanel.add(textFieldName);
		
		JLabel label_2 = new JLabel("gender");
		label_2.setBounds(272, 46, 54, 15);
		contentPanel.add(label_2);
		
		final JComboBox comboBoxGender = new JComboBox();
		comboBoxGender.setToolTipText("");
		comboBoxGender.setEditable(true);
		comboBoxGender.setBounds(379, 43, 130, 21);
		comboBoxGender.setModel(new DefaultComboBoxModel(new String[] {"male", "female", "unknown"}));
		contentPanel.add(comboBoxGender);
		
		
		final JComboBox comboBoxCertType = new JComboBox();
		comboBoxCertType.setToolTipText("");
		comboBoxCertType.setEditable(true);
		comboBoxCertType.setBounds(379, 176, 130, 21);
		comboBoxCertType.setModel(new DefaultComboBoxModel(new String[] {"officerID","ID"}));
		contentPanel.add(comboBoxCertType);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
		
		JLabel label_3 = new JLabel("BornTime");
		label_3.setBounds(272, 88, 83, 15);
		contentPanel.add(label_3);
		
		textFieldBron = new JTextField();
		textFieldBron.setText((String) null);
		textFieldBron.setColumns(10);
		textFieldBron.setBounds(379, 85, 130, 21);
		contentPanel.add(textFieldBron);
		
		JLabel label_4 = new JLabel("City");
		label_4.setBounds(272, 133, 54, 15);
		contentPanel.add(label_4);
		
		textFieldCity = new JTextField();
		textFieldCity.setText((String) null);
		textFieldCity.setColumns(10);
		textFieldCity.setBounds(379, 130, 130, 21);
		contentPanel.add(textFieldCity);
		
		JLabel label_5 = new JLabel("CertificateType");
		label_5.setBounds(272, 179, 102, 15);
		contentPanel.add(label_5);
		
		JLabel label_6 = new JLabel("Tag");
		label_6.setBounds(272, 227, 54, 15);
		contentPanel.add(label_6);
		
		textFieldTag = new JTextField();
		textFieldTag.setText((String) null);
		textFieldTag.setColumns(10);
		textFieldTag.setBounds(379, 224, 130, 21);
		contentPanel.add(textFieldTag);
		
		JLabel label_7 = new JLabel("CertificateNo.");
		label_7.setBounds(272, 275, 102, 15);
		contentPanel.add(label_7);
		
		textFieldcno = new JTextField();
		textFieldcno.setText((String) null);
		textFieldcno.setColumns(10);
		textFieldcno.setBounds(379, 272, 130, 21);
		contentPanel.add(textFieldcno);
		
		JLabel label_8 = new JLabel("Url");
		label_8.setBounds(20, 326, 54, 15);
		contentPanel.add(label_8);
		
		textFieldURL = new JTextField();
		textFieldURL.setText((String) null);
		textFieldURL.setColumns(10);
		textFieldURL.setBounds(84, 323, 425, 21);
		contentPanel.add(textFieldURL);
		
		JButton btnAddPic = new JButton("Add Pic");
		//Add Face Pic
		btnAddPic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String strUrl = "";
				if(HttpsClientUtil.bHttpsEnabled)
				{
					 strUrl= "https://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/uploadStorageCloud?format=json";
				}
				else
				{
					 strUrl= "http://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/uploadStorageCloud?format=json";
				}
				
				
				JFileChooser fc = new JFileChooser();
				PicFileFilter picFilter = new PicFileFilter(); //ͼƬ������    
				fc.addChoosableFileFilter(picFilter);  
				fc.setFileFilter(picFilter);  
								  
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					 filepath = fc.getSelectedFile().getPath();  
					 String filename = fc.getSelectedFile().getName(); 
	                    
	                  filepath=filepath.replace("\\", "\\\\");
	                  Image img = new ImageIcon(filepath).getImage();
	                  
	      			  img = img.getScaledInstance(242, 274, 0);
	      			  FacePicBox.setIcon(new ImageIcon(img));		      			  
				}  
				else
				{
					return;
				}
				
				//build json data
				JSONObject jsonStorageCloud = new JSONObject();
				try
				{
					jsonStorageCloud.put("FDID", strFDID);
					jsonStorageCloud.put("storageType", "dynamic");
					
					File file = new File(filepath);
		            FileInputStream uploadPic = new FileInputStream(file);
		            byte[] bytePic = new byte[uploadPic.available()];
		            uploadPic.read(bytePic);
		           
		            String strPic = new String(bytePic, "ISO-8859-1");
		            JSONObject jsonRet = null;
		            if(HttpsClientUtil.bHttpsEnabled)
		            {
		            	  jsonRet = new JSONObject(HttpsClientUtil.doPostStorageCloud(strUrl, jsonStorageCloud.toString(),strPic,"---------------------------------7e13971310878"));
		            }
		            else
		            {
		            	  jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonStorageCloud.toString(),strPic,"---------------------------------7e13971310878"));
		            }
					
		           
				    // parse url
					 if(jsonRet.getString("errorCode").compareTo("1") != 0)
	                    {
	                    	//error info
	                    	 JOptionPane.showMessageDialog(null, jsonRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                    else
	                    {
	                    	System.out.println(jsonRet.getString("URL"));
	                    	textFieldURL.setText(jsonRet.getString("URL"));
	                    }
					 
			    } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    }
			}
		});
		btnAddPic.setBackground(new Color(135, 206, 250));
		btnAddPic.setBounds(20, 367, 159, 23);
		contentPanel.add(btnAddPic);
		
		JButton btnAdd = new JButton("Add");
		//Add face Info
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject jsonSingleFDlib = new JSONObject();
				String strResult;
				String strUrl = "";
				if(HttpsClientUtil.bHttpsEnabled)
				{
					 strUrl= "https://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
				}
				else
				{
					 strUrl= "http://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/FDLib/FaceDataRecord?format=json";
				}
				
							
				try {																							
					jsonSingleFDlib.put("faceURL", textFieldURL.getText());
					jsonSingleFDlib.put("faceLibType", "blackFD");
					jsonSingleFDlib.put("FDID", strFDID);
					jsonSingleFDlib.put("name", textFieldName.getText());
					jsonSingleFDlib.put("gender", ((JTextField)comboBoxGender.getEditor().getEditorComponent()).getText());
					jsonSingleFDlib.put("bornTime", textFieldBron.getText());
					jsonSingleFDlib.put("city", textFieldCity.getText());
					jsonSingleFDlib.put("certificateType", ((JTextField)comboBoxCertType.getEditor().getEditorComponent()).getText());
					jsonSingleFDlib.put("certificateNumber", textFieldcno.getText());
					jsonSingleFDlib.put("tag", textFieldTag.getText());
					
					String type= "application/x-www-form-urlencoded; charset=UTF-8";
					 jsonAddDataRet = new JSONObject(HTTPClientUtil.doPostWithType(strUrl, jsonSingleFDlib.toString(),null,type));
					 if(jsonAddDataRet.getString("errorCode").compareTo("1") != 0)
	                    {
	                    	//error info
	                    	 JOptionPane.showMessageDialog(null, jsonAddDataRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                    else
	                    {
	                    	JOptionPane.showMessageDialog(null, "Add Face Data Success", "Success", JOptionPane.NO_OPTION);
	                    }

					setVisible(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// JOptionPane.showMessageDialog(null, jsonAddDataRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			


			
			}
		});
		btnAdd.setBackground(new Color(135, 206, 250));
		btnAdd.setBounds(416, 367, 93, 23);
		contentPanel.add(btnAdd);

	}
}
