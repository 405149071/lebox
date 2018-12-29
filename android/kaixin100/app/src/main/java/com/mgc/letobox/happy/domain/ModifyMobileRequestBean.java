package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class ModifyMobileRequestBean extends BaseRequestBean {
    private String mobile;//	是	STRING	新手机号

    public String getOld_mobile() {
        return old_mobile;
    }

    public void setOld_mobile(String old_mobile) {
        this.old_mobile = old_mobile;
    }

    private String old_mobile;//	是	STRING	旧手机号
    private String smstype;//	是	STRING	短信类型 1 注册 2 登陆 3 修改密码 4 信息变更
    private String smscode;//	是	STRING	短信校验码

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
}
