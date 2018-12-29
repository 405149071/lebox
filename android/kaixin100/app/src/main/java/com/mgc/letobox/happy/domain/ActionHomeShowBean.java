package com.mgc.letobox.happy.domain;

public class ActionHomeShowBean extends BaseRequestBean {
    private String version;
    private int type;
    private String mark;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }
}
