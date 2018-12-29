package com.mgc.letobox.happy.domain;

/**
 * Created by DELL on 2018/7/19.
 */

public class CircleTieZiListRequest extends BaseRequestBean {

    private int group_id;
    private int page;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
