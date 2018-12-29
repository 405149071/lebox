package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TradeGameServerRequestBean extends  BaseRequestBean {
    public String gameid;  //游戏ID（adid或者iosid，二者选一）

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

    public int type;  //类型（0 全部 1安卓 2 ios）

}
