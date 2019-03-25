package com.dyw.client.entity.protection;

public class CtrlCenterEntity {
    private String ctrlCenterID;//根控制中心 ID
    private String ctrlCenterName;//根控制中心名称
    private String isChildrenNode;//当前控制中心下是否有子节点
    private String source;//

    public String getCtrlCenterID() {
        return ctrlCenterID;
    }

    public void setCtrlCenterID(String ctrlCenterID) {
        this.ctrlCenterID = ctrlCenterID;
    }

    public String getCtrlCenterName() {
        return ctrlCenterName;
    }

    public void setCtrlCenterName(String ctrlCenterName) {
        this.ctrlCenterName = ctrlCenterName;
    }

    public String getIsChildrenNode() {
        return isChildrenNode;
    }

    public void setIsChildrenNode(String isChildrenNode) {
        this.isChildrenNode = isChildrenNode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
