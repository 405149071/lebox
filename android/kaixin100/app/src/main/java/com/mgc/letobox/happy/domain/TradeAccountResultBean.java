package com.mgc.letobox.happy.domain;

import java.io.Serializable;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TradeAccountResultBean implements Serializable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String id;  // id

    public String nickname;   //1 时间倒叙 2 时间正序 默认 1


}
