package com.mgc.letobox.happy.domain;


public class DrawCashApplyRequestBean extends BaseRequestBean {
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private int amount;
}
