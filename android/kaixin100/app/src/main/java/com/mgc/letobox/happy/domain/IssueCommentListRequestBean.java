package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class IssueCommentListRequestBean extends ListBaseRequestBean {

    public int getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
    }

    public int issue_id;
}
