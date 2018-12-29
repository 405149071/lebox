package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TransferHistoryRequestBean extends BaseRequestBean {
    private String symbol;
    private int direction;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
