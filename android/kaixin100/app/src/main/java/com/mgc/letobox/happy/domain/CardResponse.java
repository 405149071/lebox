package com.mgc.letobox.happy.domain;

/**
 * Created by DELL on 2018/8/9.
 */

public class CardResponse extends BaseResultBean {


    /**
     * title : 梦幻西游手游
     * gtitle : 测试
     * content : 测试发布帖子。。。。。。。
     * update_time : 1533802010
     * id : 259
     * releaseTime : 12分钟前
     */

    private String title;
    private String gtitle;
    private String content;
    private String update_time;
    private int id;
    private String releaseTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGtitle() {
        return gtitle;
    }

    public void setGtitle(String gtitle) {
        this.gtitle = gtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
