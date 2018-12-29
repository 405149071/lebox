package com.mgc.letobox.happy.domain;

public class GuidanceRequestBean extends BaseRequestBean {
    private String tags;
    private String kols;

    public void setKols(String kols) {
        this.kols = kols;
    }

    public String getKols() {
        return kols;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }
}
