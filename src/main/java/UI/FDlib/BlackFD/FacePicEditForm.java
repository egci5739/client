package UI.FDlib.BlackFD;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.json.JSONException;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;
import UI.FDlib.BlackFD.BlackFDlibManagement.FacePicDataStruct;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import ISAPI.HttpsClientUtil;
class PicFileFilter extends FileFilter {    
    public String getDescription() {    
        return "*.jpg;*.gif;*.bmp;*.png";    
    }    
    
    public boolean accept(File file) {    
        String name = file.getName();    
        return file.isDirectory() || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".gif")||name.toLowerCase().endsWith(".bmp")||name.toLowerCase().endsWith(".png");  // ����ʾĿ¼��bmp��png�ļ�  
    }    
}   

public class FacePicEditForm extends JDialog {
	private JTextField textFieldName;
	private JTextField textFieldBron;
	private JTextField textFieldCity;
	private JTextField textFieldCertificateType;
	private JTextField textFieldTag;
	private JTextField textFieldURL;
	private JTextField textFieldcno;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			FacePicEditForm dialog = new FacePicEditForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 * 
	 * 
	 */
	
	
	public static void showpic(String url, JLabel jlable)
	{

		try {
			URL picurl = new URL(url);
			Image img = null;
			HttpURLConnection conn;
			conn = (HttpURLConnection) picurl.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			InputStream is = conn.getInputStream();
			img = ImageIO.read(is);
			is.close();
			img = img.getScaledInstance(242, 274, 0);
			jlable.setIcon(new ImageIcon(img));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String filepath="";
	public FacePicEditForm(final String strIP, final String strPort, final FacePicDataStruct singletemp) {
		getContentPane().setBackground(Color.WHITE);
		final JCheckBox chckbxSendlocalPic = new JCheckBox("Send pic from local file");
		setModal(true);
		setBounds(100, 100, 532, 438);
		getContentPane().setLayout(null);
		{
			JLabel lblName = new JLabel("Name");
			lblName.setBounds(262, 13, 54, 15);
			getContentPane().add(lblName);
		}
		{
			JLabel lblGender = new JLabel("gender");
			lblGender.setBounds(262, 46, 54, 15);
			getContentPane().add(lblGender);
		}
		{
			JLabel lblBorntime = new JLabel("BornTime");
			lblBorntime.setBounds(262, 88, 83, 15);
			getContentPane().add(lblBorntime);
		}
		{
			JLabel lblCity = new JLabel("City");
			lblCity.setBounds(262, 133, 54, 15);
			getContentPane().add(lblCity);
		}
		{
			JLabel lblCertificatetype = new JLabel("CertificateType");
			lblCertificatetype.setBounds(262, 179, 102, 15);
			getContentPane().add(lblCertificatetype);
		}
		{
			JLabel lblTag = new JLabel("Tag");
			lblTag.setBounds(262, 227, 54, 15);
			getContentPane().add(lblTag);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(369, 10, 130, 21);
			textFieldName.setText(singletemp.name);
			getContentPane().add(textFieldName);
			textFieldName.setColumns(10);
		}
		
		final JComboBox comboBoxGender = new JComboBox();
		comboBoxGender.setToolTipText("");
		comboBoxGender.setEditable(true);
		comboBoxGender.setModel(new DefaultComboBoxModel(new String[] {"male", "female", "unknown"}));
		comboBoxGender.setBounds(369, 43, 130, 21);
		((JTextField)comboBoxGender.getEditor().getEditorComponent()).setText(singletemp.gender);
		getContentPane().add(comboBoxGender);
		
		textFieldBron = new JTextField();
		textFieldBron.setBounds(369, 85, 130, 21);
		textFieldBron.setText(singletemp.bornTime);
		getContentPane().add(textFieldBron);
		textFieldBron.setColumns(10);
		
		textFieldCity = new JTextField();
		textFieldCity.setBounds(369, 130, 130, 21);
		textFieldCity.setText(singletemp.city);
		getContentPane().add(textFieldCity);
		textFieldCity.setColumns(10);
		
		textFieldCertificateType = new JTextField();
		textFieldCertificateType.setColumns(10);
		textFieldCertificateType.setText(singletemp.certificateType);
		textFieldCertificateType.setBounds(369, 176, 130, 21);
		getContentPane().add(textFieldCertificateType);
		
		textFieldTag = new JTextField();
		textFieldTag.setColumns(10);
		textFieldTag.setBounds(369, 224, 130, 21);
		textFieldTag.setText(singletemp.tag);
		getContentPane().add(textFieldTag);
		
		{
			JLabel lblNewLabel = new JLabel("CertificateNo.");
			lblNewLabel.setBounds(262, 275, 102, 15);
			getContentPane().add(lblNewLabel);
		}
		{
			textFieldcno = new JTextField();
			textFieldcno.setBounds(369, 272, 130, 21);
			textFieldcno.setText(singletemp.certificateNumber);
			getContentPane().add(textFieldcno);
			textFieldcno.setColumns(10);
		}
		
		final JLabel FacePicBox = new JLabel("");
		FacePicBox.setBackground(Color.WHITE);
		FacePicBox.setBounds(10, 13, 242, 274);
		FacePicBox.setBorder(new LineBorder(Color.gray));
		getContentPane().add(FacePicBox);
		
		
		showpic(singletemp.faceURL,FacePicBox);
		
		{
			JLabel lblUrl = new JLabel("Url");
			lblUrl.setBounds(10, 326, 54, 15);
			getContentPane().add(lblUrl);
		}
		{
			textFieldURL = new JTextField();
			textFieldURL.setColumns(10);
			textFieldURL.setBounds(74, 323, 425, 21);
			textFieldURL.setText(singletemp.faceURL);
			getContentPane().add(textFieldURL);
		}
		{
			JButton btnChangePic = new JButton("Change Pic");
			//Change Pic
			btnChangePic.addActionListener(new ActionListener() {
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
						jsonStorageCloud.put("FDID", singletemp.FDID);
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
		                    	textFieldURL.setText(jsonRet.getString("URL"));
		                    }
						 
				    } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				    }
	                	                
				}
			});
			btnChangePic.setBackground(new Color(135, 206, 250));
			btnChangePic.setBounds(10, 367, 159, 23);
			getContentPane().add(btnChangePic);
		}
		{
			JButton btnMod = new JButton("Mod");
			//Modify Face Info
			btnMod.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JSONObject jsonSingleFDlib = new JSONObject();
					String strResult;
					String strUrl = "";
					if(HttpsClientUtil.bHttpsEnabled)
					{
						 strUrl= "https://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/FDLibEntity/FDSearch?format=json&FDID=" + singletemp.FDID + "&FPID=" + singletemp.FPID + "&faceLibType=blackFD";
					}
					else
					{
						 strUrl= "http://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/FDLibEntity/FDSearch?format=json&FDID=" + singletemp.FDID + "&FPID=" + singletemp.FPID + "&faceLibType=blackFD";
					}
					

					if(chckbxSendlocalPic.isSelected()==false)
					{
						try {																							
							jsonSingleFDlib.put("faceURL", textFieldURL.getText());
							jsonSingleFDlib.put("name", textFieldName.getText());
							jsonSingleFDlib.put("gender", ((JTextField)comboBoxGender.getEditor().getEditorComponent()).getText());
							jsonSingleFDlib.put("bornTime", textFieldBron.getText());
							jsonSingleFDlib.put("city", textFieldCity.getText());
							jsonSingleFDlib.put("certificateType", textFieldCertificateType.getText());
							jsonSingleFDlib.put("certificateNumber", textFieldcno.getText());
							jsonSingleFDlib.put("tag", textFieldTag.getText());
							
							String type= "application/x-www-form-urlencoded; charset=UTF-8";
				            JSONObject jsonRet = null;
				            if(HttpsClientUtil.bHttpsEnabled)
				            {
				            	 jsonRet = new JSONObject(HttpsClientUtil.doPutWithType(strUrl, jsonSingleFDlib.toString(),null,type));
				            	 
				            }
				            else
				            {
				            	 jsonRet = new JSONObject(HTTPClientUtil.doPutWithType(strUrl, jsonSingleFDlib.toString(),null,type));
				            }
							
							 if(jsonRet.getString("errorCode").compareTo("1") != 0)
			                    {
			                    	//error info
			                    	 JOptionPane.showMessageDialog(null, jsonRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
			                    }
			                    else
			                    {
			                    	JOptionPane.showMessageDialog(null, "Modify Face Data Success", "Success", JOptionPane.NO_OPTION);
			                    }

							setVisible(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						//�������ض�����ͼƬ
						try {
							jsonSingleFDlib.put("name", textFieldName.getText());
							jsonSingleFDlib.put("gender", ((JTextField)comboBoxGender.getEditor().getEditorComponent()).getText());
							jsonSingleFDlib.put("bornTime", textFieldBron.getText());
							jsonSingleFDlib.put("city", textFieldCity.getText());
							jsonSingleFDlib.put("certificateType", textFieldCertificateType.getText());
							jsonSingleFDlib.put("certificateNumber", textFieldcno.getText());
							jsonSingleFDlib.put("tag", textFieldTag.getText());
							
							File file = new File(filepath);
				            FileInputStream uploadPic = new FileInputStream(file);
				            byte[] bytePic = new byte[uploadPic.available()];
				            uploadPic.read(bytePic);
				            String strPic = new String(bytePic, "ISO-8859-1");
							
							
				            if(HttpsClientUtil.bHttpsEnabled)
				            {
				            	strResult = HttpsClientUtil.doModFacePicRecord(strUrl, jsonSingleFDlib.toString(),strPic,"---------------------------------7e13971310878");
				            }
				            else
				            {
				            	strResult = HTTPClientUtil.doModFacePicRecord(strUrl, jsonSingleFDlib.toString(),strPic,"---------------------------------7e13971310878");
				            }
							

							setVisible(false);
							
						} catch (JSONException | FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}

				}
			});
			btnMod.setBackground(new Color(135, 206, 250));
			btnMod.setBounds(406, 367, 93, 23);
			getContentPane().add(btnMod);
		}
		
		
		chckbxSendlocalPic.setBounds(175, 367, 203, 23);
		getContentPane().add(chckbxSendlocalPic);

	}
}
