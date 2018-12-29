package com.mgc.letobox.happy.domain;

public class MiniMoreRequestBean extends BaseRequestBean {
    private int type_id;
    private int page;
    private int offset;

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getType_id() {
        return type_id;
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
