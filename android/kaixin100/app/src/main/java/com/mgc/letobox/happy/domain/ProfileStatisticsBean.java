package com.mgc.letobox.happy.domain;

/**
 * Create by zhaozhihui on 2018/8/9
 **/
public class ProfileStatisticsBean extends BaseRequestBean {

    private int who_follows;
    private int follows_who;
    private int group_posts;

    public int getWho_follows() {
        return who_follows;
    }

    public void setWho_follows(int who_follows) {
        this.who_follows = who_follows;
    }

    public int getFollows_who() {
        return follows_who;
    }

    public void setFollows_who(int follow_who) {
        this.follows_who = follow_who;
    }

    public int getGroup_posts() {
        return group_posts;
    }

    public void setGroup_posts(int group_posts) {
        this.group_posts = group_posts;
    }

    public float getProperty() {
        return property;
    }

    public void setProperty(float property) {
        this.property = property;
    }

    private float property;


}
