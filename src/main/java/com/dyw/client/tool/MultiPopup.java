package com.dyw.client.tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;


public class MultiPopup extends JPopupMenu {

    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    private List<NameCode> values;

    private List<NameCode> defaultValues;

    private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

    private JButton commitButton;

    private JButton cancelButton;

    public static final String COMMIT_EVENT = "commit";

    public static final String CANCEL_EVENT = "cancel";

    public MultiPopup(List<NameCode> value, List<NameCode> defaultValue) {
        super();
        values = value;
        defaultValues = defaultValue;
        initComponent();
    }

    public void addActionListener(ActionListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    private void initComponent() {
//		this.setBounds(100, 100, 450, 300);
        int height = 200;
        int width = 170;
        for (NameCode v : values) {
            JCheckBox temp = new JCheckBox(v.toString(), selected(v));
            checkBoxList.add(temp);
        }
        if (checkBoxList.size() > 6) {
            height = 200;
        } else {
            height = checkBoxList.size() * 31;
        }
        JScrollPane scrollPane;
        JPanel checkboxOutPane = new JPanel();
        checkboxOutPane.setPreferredSize(new Dimension(width, height));
        checkboxOutPane.setLayout(null);

        JPanel checkboxPane = new JPanel();
//		checkboxPane.setBackground(Color.WHITE);
        JPanel buttonPane = new JPanel();
        buttonPane.setSize(new Dimension(width, 10));
        this.setLayout(new BorderLayout());

		
		/*if(checkBoxList.size()>0){
			if (checkBoxList.get(0).getText().equals("Selected All")){
				checkBoxList.get(0).addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						System.out.println("被选中状态   " + checkBoxList.get(0).isSelected());
						if (checkBoxList.get(0).isSelected())// Select All 被选中
						{
							// 检查其他的是否被选中如果没有就选中他们
							for (int i = 1; i < checkBoxList.size(); i++) {
								if (!checkBoxList.get(i).isSelected())
									checkBoxList.get(i).setSelected(true);
							}
						} else {
							for (int i = 1; i < checkBoxList.size(); i++) {
								if (checkBoxList.get(i).isSelected())
									checkBoxList.get(i).setSelected(false);
							}
						}
					}
				});
			}
		}*/

        for (JCheckBox box : checkBoxList) {
            checkboxPane.add(box);
        }

        scrollPane = new JScrollPane(checkboxPane);

        checkboxPane.setPreferredSize(new Dimension(width, checkBoxList.size() * 25));
        checkboxPane.setLayout(new GridLayout(checkBoxList.size(), 1, 0, 3));
        scrollPane.setBounds(0, 0, width + 5, height);
        commitButton = new JButton("ok");
        commitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commit();
            }
        });

        cancelButton = new JButton("cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        buttonPane.add(commitButton);
        buttonPane.add(cancelButton);
        checkboxOutPane.add(scrollPane);
        this.add(checkboxOutPane, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.SOUTH);

    }

    private boolean selected(NameCode v) {
        for (NameCode dv : defaultValues) {
            if (dv.equals(v)) {
                return true;
            }
        }
        return false;
    }

    protected void fireActionPerformed(ActionEvent e) {
        System.out.println("148 listeners:" + listeners.size());
        for (ActionListener l : listeners) {
            System.out.println(l.toString());
            System.out.println(e.toString());
            l.actionPerformed(e);
            System.out.println("end-------");

        }
    }

    public List<NameCode> getSelectedValues() {
        List<NameCode> selectedValues = new ArrayList<NameCode>();

		/*if (checkBoxList.get(0).getText().equals("Selected All")) {
			if (checkBoxList.get(0).isSelected()) {
				for (int i = 1; i < checkBoxList.size(); i++) {
					selectedValues.add(values.get(i));
				}
			} else {
				for (int i = 1; i < checkBoxList.size(); i++) {

					if (checkBoxList.get(i).isSelected())
						selectedValues.add(values.get(i));
				}
			}
		} else*/
        for (int i = 0; i < checkBoxList.size(); i++) {

            if (checkBoxList.get(i).isSelected())
                selectedValues.add(values.get(i));
        }

//		return selectedValues.toArray(new Object[selectedValues.size()]);
        return selectedValues;
    }

    public void setDefaultValue(List<NameCode> defaultValue) {
        defaultValues = defaultValue;

    }

    public void commit() {
        fireActionPerformed(new ActionEvent(this, 0, COMMIT_EVENT));
    }

    public void cancel() {
        fireActionPerformed(new ActionEvent(this, 0, CANCEL_EVENT));
    }

}
