package com.dyw.client.tool;

public class NameCode {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStringAll() {
        return "KeyValue [code=" + code + ", name=" + name + "]";
    }

    @Override
    public String toString() {
        return name;
    }

    public NameCode(String code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    public NameCode() {
        super();
    }

}
