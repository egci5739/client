package com.dyw.client.form.guard;

import com.alibaba.fastjson.JSON;
import com.dyw.client.controller.Egci;
import com.dyw.client.entity.EquipmentEntity;
import com.dyw.client.service.EquipmentTreeCellRenderer;
import com.dyw.client.service.SendInfoSocketService;
import com.dyw.client.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EquipmentTreeForm {
    private Logger logger = LoggerFactory.getLogger(EquipmentTreeForm.class);

    public JPanel getEquipmentTreeForm() {
        return equipmentTreeForm;
    }

    private JPanel equipmentTreeForm;
    private JTree equipmentTree;
    private JPanel equipmentTreeTitlePanel;
    private JPanel equipmentTreeContentPanel;
    private JButton refreshButton;
    private JButton closeFaceButton;
    private JButton openFaceButton;
    private JFrame jFrame;

    public EquipmentTreeForm() {
        /*
         * 获取设备树
         * */
        getEquipmentStatus();
        /*
         * 刷新
         * */
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getEquipmentStatus();
            }
        });
        /*
         * 关闭人脸
         * */
        closeFaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode note = (DefaultMutableTreeNode) equipmentTree.getLastSelectedPathComponent();
                if (note == null || !note.toString().split("-")[0].equals("切换器")) {
                    Tool.showMessage("请先选择切换器", "提示", 1);
                } else if (note.toString().split("-")[2].equals("离线")) {
                    Tool.showMessage("切换器已离线", "提示", 1);
                } else {
                    closeFace(note.toString().split("-")[1]);
                }
            }
        });
        /*
         * 启用人脸
         * */
        openFaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode note = (DefaultMutableTreeNode) equipmentTree.getLastSelectedPathComponent();
                if (note == null || !note.toString().split("-")[0].equals("切换器")) {
                    Tool.showMessage("请先选择切换器", "提示", 1);
                } else if (note.toString().split("-")[2].equals("离线")) {
                    Tool.showMessage("切换器已离线", "提示", 1);
                } else {
                    runFace(note.toString().split("-")[1]);
                }
            }
        });
    }

    /*
     * 获取设备信息
     * */
    public void getEquipmentStatus() {
        try {
            equipmentTree.setModel(null);
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("设备树");
            DefaultTreeModel defaultTreeModel;
            defaultTreeModel = new DefaultTreeModel(root);
            equipmentTree.setModel(defaultTreeModel);
            EquipmentTreeCellRenderer equipmentTreeCellRenderer = new EquipmentTreeCellRenderer();
            equipmentTree.setCellRenderer(equipmentTreeCellRenderer);
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
            sendInfoSocketService.sendInfo("3#0");
            String receiveInfo = sendInfoSocketService.receiveInfoOnce();
            if (receiveInfo.equals("error")) {
                Tool.showMessage("设备信息正在完善，请稍后重试", "设备状态", 1);
            }
            List<EquipmentEntity> equipmentEntityList = JSON.parseArray(receiveInfo, EquipmentEntity.class);
            for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                if (equipmentEntity.getEquipmentType() == 4) {
                    for (EquipmentEntity equipmentEntity1 : equipmentEntityList) {
                        if (equipmentEntity.getRelativeEquipmentId() == equipmentEntity1.getEquipmentId()) {
                            equipmentEntity1.setEquipmentSwitchIp(equipmentEntity.getEquipmentIp());
                            equipmentEntity1.setEquipmentValidity(equipmentEntity.getEquipmentValidity());
                        }
                    }
                }
            }
            switch (Egci.accountEntity.getAccountPermission()) {
                case 1:
                    DefaultMutableTreeNode one1 = new DefaultMutableTreeNode("一核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 1) {
                            DefaultMutableTreeNode one2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            one2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            one2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            one1.add(one2);
                        }
                    }
                    root.add(one1);
                    break;
                case 2:
                    DefaultMutableTreeNode two1 = new DefaultMutableTreeNode("二核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 2) {
                            DefaultMutableTreeNode two2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            two2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            two2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            two1.add(two2);
                        }
                    }
                    root.add(two1);
                    break;
                case 3:
                    DefaultMutableTreeNode three1 = new DefaultMutableTreeNode("三核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 3) {
                            DefaultMutableTreeNode three2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            three2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            three2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            three1.add(three2);
                        }
                    }
                    root.add(three1);
                    break;
                default:
//                    DefaultMutableTreeNode zero1 = new DefaultMutableTreeNode("全厂");
//                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
//                        if (equipmentEntity.getEquipmentType() == 1) {
//                            DefaultMutableTreeNode zero2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
//                            zero2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
//                            zero2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
//                            zero1.add(zero2);
//                        }
//                    }
//                    root.add(zero1);
                    DefaultMutableTreeNode one11 = new DefaultMutableTreeNode("一核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 1) {
                            DefaultMutableTreeNode one2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            one2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            one2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            one11.add(one2);
                        }
                    }
                    root.add(one11);

                    DefaultMutableTreeNode two11 = new DefaultMutableTreeNode("二核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 2) {
                            DefaultMutableTreeNode two2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            two2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            two2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            two11.add(two2);
                        }
                    }
                    root.add(two11);

                    DefaultMutableTreeNode three11 = new DefaultMutableTreeNode("三核");
                    for (EquipmentEntity equipmentEntity : equipmentEntityList) {
                        if (equipmentEntity.getEquipmentType() == 1 && equipmentEntity.getEquipmentPermission() == 3) {
                            DefaultMutableTreeNode three2 = new DefaultMutableTreeNode(equipmentEntity.getEquipmentName());
                            three2.add(new DefaultMutableTreeNode("门禁设备-" + equipmentEntity.getEquipmentIp() + "-" + Tool.isLogin(equipmentEntity.getIsLogin()), true));
                            three2.add(new DefaultMutableTreeNode("切换器-" + equipmentEntity.getEquipmentSwitchIp() + "-" + Tool.switchStatus(equipmentEntity.getEquipmentValidity()), true));
                            three11.add(three2);
                        }
                    }
                    root.add(three11);
                    break;
            }
            expandAll(equipmentTree, new TreePath(root), true);
        } catch (Exception e) {
            logger.error("获取设备信息出错", e);
        }
    }

    public void init() {
        jFrame = new JFrame("设备状态");
        jFrame.setContentPane(this.equipmentTreeForm);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    // 展开树的所有节点的方法
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /*
     * 关闭人脸通行
     * */
    private void closeFace(String ip) {
        if (Tool.showConfirm("确定关闭该设备的人脸识别通行？", "提示")) {
            SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
            sendInfoSocketService.sendInfo("5#" + ip + "#0");
            sendInfoSocketService.receiveInfoOnce();
            Tool.showMessage("请在onGuard系统上开启该门禁的密码验证功能", "提示", 1);
        }
    }

    /*
     * 启用人脸识别
     * */
    private void runFace(String ip) {
        SendInfoSocketService sendInfoSocketService = new SendInfoSocketService(Egci.configEntity.getSocketIp(), Egci.configEntity.getSocketMonitorPort());
        sendInfoSocketService.sendInfo("5#" + ip + "#1");
        sendInfoSocketService.receiveInfoOnce();
    }
}
