package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TradeAccountRequestBean extends ListBaseRequestBean {
    public String gameid;  // id

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int sort;   //1 时间倒叙 2 时间正序 默认 1

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int type;   //1 时间倒叙 2 时间正序 默认 1


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String server;

}
