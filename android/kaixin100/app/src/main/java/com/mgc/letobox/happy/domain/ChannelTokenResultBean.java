package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class ChannelTokenResultBean {

    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public float getMgct_token() {
        return mgct_token;
    }

    public void setMgct_token(float mgct_token) {
        this.mgct_token = mgct_token;
    }

    public float getEth_mgct() {
        return eth_mgct;
    }

    public void setEth_mgct(float eth_mgct) {
        this.eth_mgct = eth_mgct;
    }

    float mgct_token;

    float eth_mgct;



}
