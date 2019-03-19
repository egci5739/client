package com.dyw.client.controller;

public class TreeNode {
    private String nickName = "";
    private String name = "";
    private String phone = "";
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 重点在toString，节点的显示文本就是toString
    public String toString() {
        return name + "[" + nickName + "]";
    }
}
