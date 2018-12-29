package com.mgc.letobox.happy.ui.signin;


import com.mgc.letobox.happy.domain.SignResponse;

/**
 * Create by zhaozhihui on 2018/8/14
 **/

public interface ISignInLayout {
    void setSignInDay(SignResponse.RulesBean issue);
    SignResponse.RulesBean getSignInDay();

    void dayClick();
}
