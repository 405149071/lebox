package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class SignInRequestBean extends ListBaseRequestBean {

    public int getSign_day() {
        return sign_day;
    }

    public void setSign_day(int sign_day) {
        this.sign_day = sign_day;
    }

    private int sign_day;
}
