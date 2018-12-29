package com.mgc.letobox.happy.domain;

/**
 * Created by DELL on 2018/8/9.
 */

public class GameNewListRequest extends BaseRequestBean {

    private int type;
    private int category;
    private int cp_id;
    private int page;
    private int classify;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCp_id() {
        return cp_id;
    }

    public void setCp_id(int cp_id) {
        this.cp_id = cp_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }
}
