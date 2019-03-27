package com.dyw.client.entity.protection;

public class HttpHostNotificationEntity {
    private String id;//序号
    private String url;//上传的报警主机 URL

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
