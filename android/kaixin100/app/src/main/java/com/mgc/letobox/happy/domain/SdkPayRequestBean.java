package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class SdkPayRequestBean extends BaseRequestBean {
    private CustomPayParam orderinfo;
    private RoleInfo roleinfo =new RoleInfo();

    public CustomPayParam getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(CustomPayParam orderinfo) {
        this.orderinfo = orderinfo;
    }

    public RoleInfo getRoleinfo() {
        return roleinfo;
    }

    public void setRoleinfo(RoleInfo roleinfo) {
        this.roleinfo = roleinfo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String symbol;

    public String getSymbol_name() {
        return symbol_name;
    }

    public void setSymbol_name(String symbol_name) {
        this.symbol_name = symbol_name;
    }

    public String symbol_name;
}
