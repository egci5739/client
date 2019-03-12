package com.dyw.client.entity;

public class AccountEntity {
    private String accountName;
    private String accountPass;
    private int accountPermission;
    private int accountRole;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPass() {
        return accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public int getAccountPermission() {
        return accountPermission;
    }

    public void setAccountPermission(int accountPermission) {
        this.accountPermission = accountPermission;
    }

    public int getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(int accountRole) {
        this.accountRole = accountRole;
    }
}
