package UI.FDlib.BlackFD;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import ISAPI.HTTPClientUtil;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FDlibAddForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JTextField textFieldCustom;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			FDlibAddForm dialog = new FDlibAddForm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	public FDlibAddForm(final String strIP, final String strPort) {
		setModal(true);
		setBounds(100, 100, 169, 177);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 10, 54, 15);
		contentPanel.add(lblName);
		
		JLabel lblCustominfo = new JLabel("CustomInfo");
		lblCustominfo.setBounds(10, 56, 71, 15);
		contentPanel.add(lblCustominfo);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(82, 7, 66, 21);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		
		textFieldCustom = new JTextField();
		textFieldCustom.setColumns(10);
		textFieldCustom.setBounds(82, 53, 66, 21);
		contentPanel.add(textFieldCustom);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String name=textFieldName.getText().toString();
				String customInfo=textFieldCustom.getText().toString();
				
				String strUrl= "http://" + strIP +  ":" + strPort + "/ISAPI/Intelligent/FDLibEntity?format=json";
				String strResult;
				try {
					
					JSONObject jsonSingleFDlib = new JSONObject();
					jsonSingleFDlib.put("faceLibType", "blackFD");
					jsonSingleFDlib.put("name", name);
					jsonSingleFDlib.put("customInfo", customInfo);
					
					
					strResult = HTTPClientUtil.doPost(strUrl, jsonSingleFDlib.toString(), null);
					setVisible(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		btnAdd.setBackground(new Color(135, 206, 250));
		btnAdd.setBounds(36, 106, 93, 23);
		contentPanel.add(btnAdd);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
