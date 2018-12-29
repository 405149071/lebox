package com.mgc.letobox.happy.domain;


import java.io.Serializable;

public class DrawCashOrderInfoResultBean implements Serializable{
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIs_draw() {
        return is_draw;
    }

    public void setIs_draw(int is_draw) {
        this.is_draw = is_draw;
    }

    private int amount;
    private int is_draw;


}
