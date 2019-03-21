package UI.FDlib.AlarmHost;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.auth.Credentials;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.awt.event.ActionEvent;

public class AlarmHost extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField m_url;
	private JTextField m_protocolType;
	private JTextField m_ipAddress;
	private JTextField m_portNo;
	private JTextField m_format;
	private JTextField m_eventType;

	private String strID;
	
    private String strDeviceIP="";
    private String strDevicePort="";
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			AlarmHost dialog = new AlarmHost();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AlarmHost(String strIP,String strPort)
	{
		strDeviceIP = strIP;
		strDevicePort = strPort;
		
		InitDialog();
	}
	public void InitDialog() {

		
		 String strPrefix = "http://" + strDeviceIP + ":" + strDevicePort;
	     final String strGetUrl = strPrefix + "/ISAPI/Event/notification/httpHosts?format=json";
	     final String strSetUrl = strPrefix + "/ISAPI/Event/notification/httpHosts?format=json&ID=";
	 
	        
		setBounds(100, 100, 381, 320);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("url");
			lblNewLabel.setBounds(10, 10, 54, 15);
			contentPanel.add(lblNewLabel);
		}
		
		m_url = new JTextField();
		m_url.setBounds(114, 7, 193, 21);
		contentPanel.add(m_url);
		m_url.setColumns(10);
		
		JLabel lblProtocoltype = new JLabel("protocolType");
		lblProtocoltype.setBounds(10, 38, 94, 15);
		contentPanel.add(lblProtocoltype);
		
		m_protocolType = new JTextField();
		m_protocolType.setColumns(10);
		m_protocolType.setBounds(114, 35, 193, 21);
		contentPanel.add(m_protocolType);
		
		JLabel lblIpaddress = new JLabel("ipAddress");
		lblIpaddress.setBounds(10, 72, 54, 15);
		contentPanel.add(lblIpaddress);
		
		m_ipAddress = new JTextField();
		m_ipAddress.setColumns(10);
		m_ipAddress.setBounds(114, 69, 193, 21);
		contentPanel.add(m_ipAddress);
		
		JLabel lblPortno = new JLabel("portNo");
		lblPortno.setBounds(10, 109, 54, 15);
		contentPanel.add(lblPortno);
		
		m_portNo = new JTextField();
		m_portNo.setColumns(10);
		m_portNo.setBounds(114, 106, 193, 21);
		contentPanel.add(m_portNo);
		
		JLabel lblFormat = new JLabel("format");
		lblFormat.setBounds(10, 148, 54, 15);
		contentPanel.add(lblFormat);
		
		m_format = new JTextField();
		m_format.setColumns(10);
		m_format.setBounds(114, 145, 193, 21);
		contentPanel.add(m_format);
		
		JLabel lblEventtype = new JLabel("eventType");
		lblEventtype.setBounds(10, 179, 54, 15);
		contentPanel.add(lblEventtype);
		
		m_eventType = new JTextField();
		m_eventType.setColumns(10);
		m_eventType.setBounds(114, 176, 193, 21);
		contentPanel.add(m_eventType);
		JButton m_btnGet = new JButton("Get");
		m_btnGet.setBounds(133, 237, 80, 23);
		contentPanel.add(m_btnGet);
		m_btnGet.addActionListener(new ActionListener() {
			//get Alarm Host info
			public void actionPerformed(ActionEvent arg0) {
				try{
					JSONObject jsonAlarmHost = new JSONObject(HTTPClientUtil.doGet(strGetUrl,null));
					if(jsonAlarmHost.getString("errorMsg") != "ok")
					{
						//Error info 
					}
					 JSONArray arrTargets = new JSONArray();
			                 arrTargets = jsonAlarmHost.getJSONArray("HttpHostNotification");
			                 JSONObject singleTargets = arrTargets.getJSONObject(0);
			                 strID = singleTargets.getString("id");
			                 m_url.setText(singleTargets.getString("url"));
			                 m_protocolType.setText(singleTargets.getString("protocolType"));
			                 m_ipAddress.setText(singleTargets.getString("ipAddress"));
			                 m_portNo.setText(singleTargets.getString("portNo"));
			                 m_format.setText(singleTargets.getString("format"));
			                 m_eventType.setText(singleTargets.getString("eventType"));
				
				 }catch (Exception e){
			                    e.printStackTrace();
			                }
				 
			}
		});
		m_btnGet.setActionCommand("OK");
		getRootPane().setDefaultButton(m_btnGet);
		{
			JButton m_btnSet = new JButton("Set");
			m_btnSet.setBounds(223, 237, 84, 23);
			contentPanel.add(m_btnSet);
			m_btnSet.addActionListener(new ActionListener() {
				//set Alarm Host info
				public void actionPerformed(ActionEvent arg0) {
					 JSONObject jsonAlarmHost = new JSONObject();
					 JSONObject jsonHttpsNotifion = new JSONObject();
		                try{
		                	
		                	
		                	jsonAlarmHost.put("id",strID);
		                	jsonAlarmHost.put("url",m_url.getText());
		                	jsonAlarmHost.put("protocolType",m_protocolType.getText());
		                	jsonAlarmHost.put("ipAddress",m_ipAddress.getText());
		                	jsonAlarmHost.put("portNo",Integer.parseInt(m_portNo.getText()));
		                	jsonAlarmHost.put("format",m_format.getText());
		                	jsonAlarmHost.put("eventType",m_eventType.getText());
		                    
		                  	jsonAlarmHost.put("parameterFormatType","json");
		                	jsonAlarmHost.put("addressingFormatType","ipaddress");
		                	jsonAlarmHost.put("httpAuthenticationMethod","MD5digest");
		                	jsonAlarmHost.put("uploadImagesDataType","URL");
		           
		                	jsonHttpsNotifion.put("HttpHostNotification", jsonAlarmHost);
		                
		                	String strTemp = jsonHttpsNotifion.toString();
		                	System.out.println(strTemp);
		                	JSONObject jsonRet = new JSONObject(HTTPClientUtil.doPut(strSetUrl + strID,jsonHttpsNotifion.toString(),null));
		                   			        
		                    if(jsonRet.getString("errorCode").compareTo("1") != 0)
		                    {
		                    	//error info
		                    	 JOptionPane.showMessageDialog(null, jsonRet.getString("errorMsg"), "Error", JOptionPane.ERROR_MESSAGE);
		                    }
		                    else
		                    {
		                    	JOptionPane.showMessageDialog(null, "Set Alarm Host Success", "Success", JOptionPane.NO_OPTION);
		                    }
		                    
		                }catch (Exception e){
		                	 JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		                    e.printStackTrace();
		                }
				}
			});
			m_btnSet.setActionCommand("Cancel");
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				buttonPane.setLayout(null);
			}
		}
	}
}
