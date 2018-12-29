package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class LoginMobileRequestBean extends BaseRequestBean {
    private String mobile;//	是	STRING	玩家注册手机号
    private String smstype;//	是	STRING	短信类型 1 注册 2 登陆 3 修改密码 4 信息变更
    private String smscode;//	是	STRING	短信校验码
//    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getSmstype() {
        return smstype;
    }

    public void setSmstype(String smstype) {
        this.smstype = smstype;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
