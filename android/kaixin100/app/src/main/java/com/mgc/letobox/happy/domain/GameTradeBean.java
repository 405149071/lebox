package com.mgc.letobox.happy.domain;

import java.io.Serializable;

/**
 * Created by zzh on 2018/4/24.
 */

public    class GameTradeBean implements Serializable{
    //交易ID
    public String id;
    //游戏id
    public String gameid;
    //游戏名称
    public String game_name;
    //商品名称
    public String product_name;
    //商品价格
    public float price;
    //个人头像
    public String pic;
    //发布时间
    public String push_time;
    //结束时间
    public String end_time;
    //游戏分类 3：安卓 4 ios
    public String classify;
    //登录次数
    public int cnt;
    //充值金额
    public String recharge_amount;
    //在线时长
    public long time;
    //资产类型  0:游戏道具 1游戏内虚拟货币 2:游戏内账号
    public int type;
    //描述
    public String desc;

    //状态
    public String status;

    //角色
    public String role;
    //角色Id
    public String roleid;

    //区服
    public String server;
    //出售人手机号
    public String mobile;
    //小号id
    public String childMemId;

//    public String game_id;
//    public String game_type;
//    public String account_type;
//    public String account_server;
//    public String account_role;
//    public String account_tag;
//    public String account_online_time;
//    public String account_login_times;
//    public String account_balance;
//    public String trade_desc;
//    public String product_status;
//
//
//    public String trade_time;
//    public String start_time;
//    public String end_time;
//
//    public String icon;
//    public String tag;
//    public String title;
//    public long price;
//    public String symbol;
//    //发布者账号
//    public String from;
//    public String status;
}
