package com.mgc.letobox.happy.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class ChargePoint implements Serializable {

    double rate;
    float rate_tt;
    float balance;
    float balance_rmb;
    String symbol;
    List<Point> points;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public float getRate_tt() {
        return rate_tt;
    }

    public void setRate_tt(float rate_tt) {
        this.rate_tt = rate_tt;
    }

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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public class Point implements Serializable{
        int amount;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public float getReal_amount() {
            return real_amount;
        }

        public void setReal_amount(float real_amount) {
            this.real_amount = real_amount;
        }

        public float getSymbol_amount() {
            return symbol_amount;
        }

        public void setSymbol_amount(float symbol_amount) {
            this.symbol_amount = symbol_amount;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        float real_amount;
        float symbol_amount;
        String symbol;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        boolean isSelected;

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
        }

        boolean isEnable;
    }
}
