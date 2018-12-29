package com.mgc.letobox.happy.domain;

/**
 * Create by zhaozhihui on 2018/8/7
 **/
public class FollowBean {
    private String portrait;
    private int uid;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean getIsfollow() {
        return is_following;
    }

    public void setIsfollow(boolean isfollow) {
        this.is_following = isfollow;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private boolean is_following;
    private String signature;

    private String  nickname;

    public boolean isIs_following() {
        return is_following;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }

    public String getLevel_pic() {
        return level_pic;
    }

    public void setLevel_pic(String level_pic) {
        this.level_pic = level_pic;
    }

    private String  level_pic;
}
