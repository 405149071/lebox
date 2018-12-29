package com.mgc.letobox.happy.domain;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class InviteInfoBean implements Serializable {

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public int getCountMem() {
        return countMem;
    }

    public void setCountMem(int countMem) {
        this.countMem = countMem;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    private int award;  //累计奖励
    private int countMem;  //累计人数
    private String share_url;  //分享url

}
