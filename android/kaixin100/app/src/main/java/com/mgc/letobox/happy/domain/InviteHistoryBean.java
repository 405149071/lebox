package com.mgc.letobox.happy.domain;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2018/8/30
 **/
public class InviteHistoryBean implements Serializable {

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private  long time;
    private String status;
}
