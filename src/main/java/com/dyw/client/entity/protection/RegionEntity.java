package com.dyw.client.entity.protection;

public class RegionEntity {
    private String regionName;//区域名称
    private String regionID;//区域 ID
    private String isChildrenNode;//当前区域下是否有子节点

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getIsChildrenNode() {
        return isChildrenNode;
    }

    public void setIsChildrenNode(String isChildrenNode) {
        this.isChildrenNode = isChildrenNode;
    }
}
