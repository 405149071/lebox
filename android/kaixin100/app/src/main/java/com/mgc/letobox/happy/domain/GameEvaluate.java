package com.mgc.letobox.happy.domain;

import java.util.List;

/**
 * Create by zhaozhihui on 2018/8/8
 **/
public class GameEvaluate {
    String icon;
    String name;
    double star_cnt;
    String content;
    String create_time;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStar_cnt() {
        return star_cnt;
    }

    public void setStar_cnt(double star_cnt) {
        this.star_cnt = star_cnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    String releaseTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;


    public List<CommentBean> getContent_new() {
        return content_new;
    }

    public void setContent_new(List<CommentBean> content_new) {
        this.content_new = content_new;
    }

    private List<CommentBean> content_new;


}
