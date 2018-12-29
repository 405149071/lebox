package com.mgc.letobox.happy.domain;

/**
 * Created by DELL on 2018/8/9.
 */

public class FactoryResponse extends BaseResultBean {

    private int id;
    private String background;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
