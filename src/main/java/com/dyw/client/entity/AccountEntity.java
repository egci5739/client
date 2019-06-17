package com.dyw.client.entity;

public class AccountEntity {
    private String accountName;
    private String accountPass;
    private String accountFunction;
    private int accountPermission;

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

    public String getAccountFunction() {
        return accountFunction;
    }

    public void setAccountFunction(String accountFunction) {
        this.accountFunction = accountFunction;
    }

    public int getAccountPermission() {
        return accountPermission;
    }

    public void setAccountPermission(int accountPermission) {
        this.accountPermission = accountPermission;
    }
}
