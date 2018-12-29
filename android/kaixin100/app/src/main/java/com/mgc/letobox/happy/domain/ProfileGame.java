package com.mgc.letobox.happy.domain;


/**
 * Create by zhaozhihui on 2018/8/8
 **/
public class ProfileGame extends GameBean{
    private int game_id;
    private String publicity;

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_icon() {
        return game_icon;
    }

    public void setGame_icon(String game_icon) {
        this.game_icon = game_icon;
    }

    private String game_name;
    private String game_icon;

}
