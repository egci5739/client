
package UI.FDlib;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

import javax.swing.ImageIcon;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.json.JSONObject;





import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ISAPI.HTTPClientUtil;
import ISAPI.HttpsClientUtil;

import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class PicFileFilter extends FileFilter {    
    public String getDescription() {    
        return "*.jpg;*.gif;*.bmp;*.png";    
    }    
    
    public boolean accept(File file) {    
        String name = file.getName();    
        return file.isDirectory() || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".gif")||name.toLowerCase().endsWith(".bmp")||name.toLowerCase().endsWith(".png");  // 仅显示目录和bmp、png文件  
    }    
}    



public class Compare1V1 extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel label_target = new JLabel("");
    private final JLabel label_constrant = new JLabel("");
	
	

    private final JLabel TargetImage = new JLabel("TargetImage");
    private final JLabel ConstrantImage = new JLabel("ConstrantImage");

    private final JLabel lblNewLabel = new JLabel("Compare Result");
    private final JLabel labelResult = new JLabel((String) null);

    private String routeTarget="";
    private int errCode = 0;
    private String errMsg = "";

    private String routeConstrast="";
    
	private String strUrl;
    private String strDeviceIP;
    private String strDevicePort;


	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			Compare1V1 dialog = new Compare1V1();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
	

	
	/**
	 * Create the dialog.
	 */
	
	public Compare1V1(String strIP,String strPort)
	{
		setTitle("Compare1V1");
		strDeviceIP = strIP;
		strDevicePort = strPort;
		
		InitDialog();
	}
	
	public void InitDialog() {
	
       strUrl = "https://" + strDeviceIP + ":" + strDevicePort + "/ISAPI/Intelligent/uploadStorageCloud?format=json";
     
		setBounds(100, 100, 812, 552);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		
		label_target = new JLabel("");
		label_target.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				PicFileFilter picFilter = new PicFileFilter(); //图片过滤器    
				fc.addChoosableFileFilter(picFilter);  
				fc.setFileFilter(picFilter);  
				String filepath="";
				  
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					 filepath = fc.getSelectedFile().getPath();  
					 String filename = fc.getSelectedFile().getName(); 
	                    
	                    filepath=filepath.replace("\\", "\\\\");
	                    
	                    int width = label_target.getWidth(); 
	                    int height = label_target.getHeight(); 
	                    
	                    
	                    Image image = new ImageIcon(filepath).getImage();
	                    image = image.getScaledInstance(width, height, 0);
	                    label_target.setIcon(new ImageIcon(image));
	                    	            		
	                    label_target.setVisible(true);
	                    	                	                                       
	                    
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
                String strPic = "";
                try{
                	 FileInputStream uploadPic = new FileInputStream(file);
     	            byte[] bytePic = new byte[uploadPic.available()];
     	            uploadPic.read(bytePic);
     	            strPic = new String(bytePic, "ISO-8859-1");
     	            
                    jsonData.put("FDID","1");
                    jsonData.put("storageType","dynamic");
                    
                    JSONObject jsonRet = null;
                    if(HttpsClientUtil.bHttpsEnabled)
            		{
            			
            			 jsonRet = new JSONObject(HttpsClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
            		}
            		else
            		{
            			 jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
            		}
                    //JSONObject jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
                    errCode = jsonRet.getInt("errorCode");
                    if(errCode != 1)
                    {
                    	errMsg = jsonRet.getString("errorMsg");
 	                	JOptionPane.showMessageDialog(null, errMsg, "Error Message", JOptionPane.ERROR_MESSAGE);
     	                return;
                    }
                    else
                    {
                    	routeTarget = jsonRet.getString("URL");
                    	JOptionPane.showMessageDialog(null, routeTarget, "Information Message", JOptionPane.INFORMATION_MESSAGE);
                    }                   
                }catch (Exception e){
                    e.printStackTrace();
                }               
                System.out.println(routeTarget);
			}
		});
		label_target.setBorder(new LineBorder(Color.BLACK));
		label_target.setBackground(Color.LIGHT_GRAY);
		
		label_target.setBounds(10, 33, 381, 389);
		contentPanel.add(label_target);
		
		TargetImage.setBounds(10, 10, 79, 15);
		
		contentPanel.add(TargetImage);
		ConstrantImage.setBounds(368, 10, 117, 15);
		
		contentPanel.add(ConstrantImage);
		label_constrant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fc = new JFileChooser();
				PicFileFilter picFilter = new PicFileFilter(); //图片过滤器    
				fc.addChoosableFileFilter(picFilter);  
				fc.setFileFilter(picFilter);  
				String filepath="";
				  
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					 filepath = fc.getSelectedFile().getPath();  
					 String filename = fc.getSelectedFile().getName(); 
	                    
	                    filepath=filepath.replace("\\", "\\\\");
	                    
	                    int width = label_constrant.getWidth(); 
	                    int height = label_constrant.getHeight(); 
	                    
	                    
	                    Image image = new ImageIcon(filepath).getImage();
	                    image = image.getScaledInstance(width, height, 0);
	                    label_constrant.setIcon(new ImageIcon(image));
	                    	            		
	                    label_constrant.setVisible(true);
	                    
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
                String strPic = "";
                try{
               	 FileInputStream uploadPic = new FileInputStream(file);
    	            byte[] bytePic = new byte[uploadPic.available()];
    	            uploadPic.read(bytePic);
    	            strPic = new String(bytePic, "ISO-8859-1");
    	            
                   jsonData.put("FDID","1");
                   jsonData.put("storageType","dynamic");
                   JSONObject jsonRet = null;
                   if(HttpsClientUtil.bHttpsEnabled)
	           		{
	           			
	           			 jsonRet = new JSONObject(HttpsClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
	           		}
	           		else
	           		{
	           			 jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));
	           		}
                  // jsonRet = new JSONObject(HTTPClientUtil.doPostStorageCloud(strUrl, jsonData.toString(),strPic,"---------------------------------7e13971310878"));                  
                   errCode = jsonRet.getInt("errorCode");
                   if(errCode != 1)
                   {
                   		errMsg = jsonRet.getString("errorMsg");
	                	JOptionPane.showMessageDialog(null, errMsg, "Error Message", JOptionPane.ERROR_MESSAGE);
    	                return;
                   }
                   else
                   {
                	   	routeConstrast = jsonRet.getString("URL");
                   		JOptionPane.showMessageDialog(null, routeTarget, "Information Message", JOptionPane.INFORMATION_MESSAGE);
                   }     
               }catch (Exception e){
                   e.printStackTrace();
               }                          
               System.out.println(routeConstrast);
			}
		});
		label_constrant.setBorder(new LineBorder(Color.BLACK));
		label_constrant.setBackground(Color.LIGHT_GRAY);
		label_constrant.setBounds(401, 33, 367, 389);
		
		contentPanel.add(label_constrant);
		
		JButton btnCompare = new JButton("Compare");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
                    if(routeTarget.length()==0 || routeConstrast.length()==0){                     
                        Object[] options = { "OK", "CANCEL" }; 
                        JOptionPane.showOptionDialog(null, "Please Select Image File!", "Warning", 
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                        null, options, options[0]);
                        return;
                    }

                    String url = "https://" + strDeviceIP + ":" + strDevicePort + "/ISAPI/Intelligent/imagesComparision/face";
                    JSONObject jsonCompare = new JSONObject();
                    JSONObject TargetImage = new JSONObject();
                    JSONObject ContrastImage = new JSONObject();
                    jsonCompare.put("dataType", "URL");
                    TargetImage.put("URL", routeTarget);                 
                    ContrastImage.put("URL", routeConstrast);                  
                    jsonCompare.put("TargetImage", TargetImage);
                    jsonCompare.put("ContrastImage", ContrastImage);
                    JSONObject resCompare = null;
                    if(HttpsClientUtil.bHttpsEnabled)
                    {
                    	 resCompare = new JSONObject(HttpsClientUtil.httpsPost(url,jsonCompare.toString()));
                    }
                    else
                    {
                    	 resCompare = new JSONObject(HTTPClientUtil.doPost(url,jsonCompare.toString(),null));
                    }
                    
                                       
                    System.out.println(resCompare);
                    if(resCompare.getInt("errorCode") != 1) {
                        JOptionPane.showMessageDialog(null, resCompare.getString("errorMsg"), "Error Message", JOptionPane.ERROR_MESSAGE);
                    }else{
                        labelResult.setText(String.format("%.2f",resCompare.getDouble("similarity")*100)+"%");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
			}
		});
		btnCompare.setBounds(438, 466, 93, 23);
		contentPanel.add(btnCompare);
		lblNewLabel.setBounds(28, 453, 93, 23);
		
		contentPanel.add(lblNewLabel);
		labelResult.setBounds(141, 457, 108, 15);
		
		contentPanel.add(labelResult);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
