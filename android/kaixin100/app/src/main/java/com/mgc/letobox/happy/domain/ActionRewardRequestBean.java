package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class ActionRewardRequestBean extends ListBaseRequestBean {

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int action;

    public int getAction_object() {
        return action_object;
    }

    public void setAction_object(int action_object) {
        this.action_object = action_object;
    }

    public int action_object;
}
