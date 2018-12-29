package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TTResultBean {
    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance_rmb() {
        return balance_rmb;
    }

    public void setBalance_rmb(float balance_rmb) {
        this.balance_rmb = balance_rmb;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float balance;
    public float balance_rmb;
    public String symbol;

}
