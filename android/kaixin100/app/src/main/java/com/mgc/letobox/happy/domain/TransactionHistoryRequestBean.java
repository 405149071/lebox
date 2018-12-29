package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TransactionHistoryRequestBean extends BaseRequestBean {
    private String symbol;
    private int ascending;
    private int page;
    private int limit;
    private int start;
    private int end;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int isAscending() {
        return ascending;
    }

    public void setAscending(int ascending) {
        this.ascending = ascending;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
