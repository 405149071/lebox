package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TradeHistoryRequestBean extends ListBaseRequestBean {
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int status;   //资产状态 1:待审核 2:审核成功并上架 3：下架 4：预购 5：交易完成 6:审核失败

}
