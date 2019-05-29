package com.dyw.client.functionForm;

import com.dyw.client.entity.protection.SearchByPicEntity;
import com.dyw.client.service.SnapAlarmTableCellRenderer;
import com.dyw.client.tool.Tool;
import net.iharder.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.List;
import java.util.Vector;

public class SearchByPicForm {
    private Logger logger = LoggerFactory.getLogger(SearchByPicForm.class);
    private JPanel searchByPicForm;
    private JScrollPane searchByPicScroll;
    private JTable searchByPicTable;

    public SearchByPicForm(List<SearchByPicEntity> searchByPicEntityList) {
        String[] columnSearchByPicInfo = {"图片", "人员信息"};
        DefaultTableModel searchByPicTableModel = new DefaultTableModel();
        searchByPicTableModel.setColumnIdentifiers(columnSearchByPicInfo);
        TableCellRenderer searchByPicCellRenderer = new SnapAlarmTableCellRenderer();
        searchByPicTable.setDefaultRenderer(Object.class, searchByPicCellRenderer);
        searchByPicTable.setModel(searchByPicTableModel);
        try {
            for (SearchByPicEntity searchByPicEntity : searchByPicEntityList) {
                Vector vector = new Vector();
                vector.add(0, Base64.encodeBytes(Tool.getURLStream(searchByPicEntity.getFaceURL())));
                vector.add(1, "<html><body>姓名：" +
                        searchByPicEntity.getName() +
                        "<br>相似度值：" +
                        searchByPicEntity.getSimilarity() +
                        "</body></html>");
                searchByPicTableModel.addRow(vector);
            }
        } catch (Exception e) {
            logger.error("以图搜图结果出错", e);
        }
    }

    public void init() {
        JFrame frame = new JFrame("以图搜图");
        frame.setContentPane(searchByPicForm);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
