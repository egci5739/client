package UI.FDlib.BlackFD;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import ISAPI.HTTPClientUtil;
import UI.FDlib.BlackFD.BlackFDlibManagement.FDlibListStruct;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FDlibEditForm extends JDialog {
	private JTextField textFieldName;
	private JTextField textFieldCustom;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			FDlibEditForm dialog = new FDlibEditForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public FDlibEditForm(final String strIP, final String strPort, final FDlibListStruct single) {
		setModal(true);
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 238, 217);
		getContentPane().setLayout(null);
		{
			JLabel lblN = new JLabel("Name");
			lblN.setBounds(10, 10, 54, 15);
			getContentPane().add(lblN);
		}
		{
			JLabel lblCustominfo = new JLabel("CustomInfo");
			lblCustominfo.setBounds(10, 49, 70, 15);
			getContentPane().add(lblCustominfo);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(91, 7, 110, 21);
			getContentPane().add(textFieldName);
			textFieldName.setColumns(10);
			textFieldName.setText(single.name);
		}
		{
			textFieldCustom = new JTextField();
			textFieldCustom.setBounds(90, 46, 111, 21);
			getContentPane().add(textFieldCustom);
			textFieldCustom.setColumns(10);
			textFieldCustom.setText(single.customInfo);
		}
		{
			JButton btnMod = new JButton("Mod");
			btnMod.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					String name=textFieldName.getText().toString();
					String customInfo=textFieldCustom.getText().toString();
					
					String strUrl= "http://" + strIP +  ":" + strPort +  "/ISAPI/Intelligent/FDLibEntity?format=json&FDID=" + single.FDID + "&faceLibType=blackFD";
					String strResult;
					try {
						
						JSONObject jsonSingleFDlib = new JSONObject();

						jsonSingleFDlib.put("name", name);
						jsonSingleFDlib.put("customInfo", customInfo);
						
						
						strResult = HTTPClientUtil.doPut(strUrl, jsonSingleFDlib.toString(), null);
						setVisible(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			btnMod.setBackground(new Color(135, 206, 250));
			btnMod.setBounds(60, 105, 93, 23);
			getContentPane().add(btnMod);
		}
	}

}
