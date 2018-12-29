package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class UpdateUserInfoRequestBean extends BaseRequestBean {
    private String nicename;

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String signature;
}
