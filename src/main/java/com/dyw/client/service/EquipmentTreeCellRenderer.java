package com.dyw.client.service;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class EquipmentTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);
        if (node.getUserObject().toString().trim().contains("离线")) {
            setForeground(Color.RED);
            setTextSelectionColor(Color.BLUE);
            setBackgroundSelectionColor(Color.GREEN);
            setBackgroundNonSelectionColor(Color.WHITE);
        }
        return this;
    }
}
