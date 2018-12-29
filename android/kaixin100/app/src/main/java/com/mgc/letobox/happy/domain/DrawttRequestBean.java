package com.mgc.letobox.happy.domain;

public class DrawttRequestBean extends BaseRequestBean {
    private int type;
    private int page;
    private int offset;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
