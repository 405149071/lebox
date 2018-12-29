package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TransferDetailRequestBean extends BaseRequestBean {
    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public boolean isGet_block() {
        return get_block;
    }

    public void setGet_block(boolean get_block) {
        this.get_block = get_block;
    }

    private String transaction_id;
    private boolean get_block;

}
