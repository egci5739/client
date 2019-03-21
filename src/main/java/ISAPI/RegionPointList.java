package ISAPI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ISAPI.VideoAnalysisTaskForm.maskRegion;
import ISAPI.VideoAnalysisTaskForm.point;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class RegionPointList extends JDialog {

	public List<point> pointlist = new ArrayList<point>();
	private final JPanel contentPanel = new JPanel();
	private JTextField x1;
	private JTextField y1;
	private JTextField x2;
	private JTextField y2;
	private JTextField x3;
	private JTextField y3;
	private JTextField x4;
	private JTextField y4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegionPointList dialog = new RegionPointList();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegionPointList() {
		setModal(true);
		setTitle("Region PointList");
		setBounds(100, 100, 308, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		JLabel lblPoint = new JLabel("Point1");
		lblPoint.setBounds(10, 35, 54, 15);
		contentPanel.add(lblPoint);
		
		JLabel lblPoint_1 = new JLabel("Point2");
		lblPoint_1.setBounds(10, 73, 54, 15);
		contentPanel.add(lblPoint_1);
		
		JLabel lblPoint_2 = new JLabel("Point3");
		lblPoint_2.setBounds(10, 116, 54, 15);
		contentPanel.add(lblPoint_2);
		
		JLabel lblPoint_3 = new JLabel("Point4");
		lblPoint_3.setBounds(10, 161, 54, 15);
		contentPanel.add(lblPoint_3);
		
		JLabel lblX = new JLabel("x");
		lblX.setBounds(95, 10, 54, 15);
		contentPanel.add(lblX);
		
		JLabel lblY = new JLabel("y");
		lblY.setBounds(190, 10, 54, 15);
		contentPanel.add(lblY);
		
		x1 = new JTextField();
		x1.setBounds(83, 32, 66, 21);
		contentPanel.add(x1);
		x1.setColumns(10);
		
		y1 = new JTextField();
		y1.setColumns(10);
		y1.setBounds(178, 32, 66, 21);
		contentPanel.add(y1);
		
		x2 = new JTextField();
		x2.setColumns(10);
		x2.setBounds(83, 70, 66, 21);
		contentPanel.add(x2);
		
		y2 = new JTextField();
		y2.setColumns(10);
		y2.setBounds(178, 70, 66, 21);
		contentPanel.add(y2);
		
		x3 = new JTextField();
		x3.setColumns(10);
		x3.setBounds(83, 113, 66, 21);
		contentPanel.add(x3);
		
		y3 = new JTextField();
		y3.setColumns(10);
		y3.setBounds(178, 113, 66, 21);
		contentPanel.add(y3);
		
		x4 = new JTextField();
		x4.setColumns(10);
		x4.setBounds(83, 158, 66, 21);
		contentPanel.add(x4);
		
		y4 = new JTextField();
		y4.setColumns(10);
		y4.setBounds(178, 158, 66, 21);
		contentPanel.add(y4);
		
		JButton btnFinish = new JButton("finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				for(int i=0;i<4;i++)
				{
					point singlepoint=new point();
					if(i==0)
					{
						singlepoint.x=Float.parseFloat(x1.getText());
						singlepoint.y=Float.parseFloat(y1.getText());
					}
					else if(i==1)
					{
						singlepoint.x=Float.parseFloat(x2.getText());
						singlepoint.y=Float.parseFloat(y2.getText());
					}
					else if(i==2)
					{
						singlepoint.x=Float.parseFloat(x3.getText());
						singlepoint.y=Float.parseFloat(y3.getText());
					}
					else if(i==3)
					{
						singlepoint.x=Float.parseFloat(x4.getText());
						singlepoint.y=Float.parseFloat(y4.getText());
					}
					pointlist.add(singlepoint);
				}
		
				setVisible(false);
			}
		});
		btnFinish.setBounds(123, 206, 93, 23);
		contentPanel.add(btnFinish);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
