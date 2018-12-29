package com.mgc.letobox.happy.domain;

import java.util.List;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class TransferDetailResultBean extends BaseRequestBean{

    public String id;

    public TransactionDetail transaction;
    public Block block;

    public class TransactionDetail{
        public String transaction_id;
        public Transaction transaction;

    }

    public class Transaction{
        public String expiration;
        public int region;
        public int ref_block_num;
        public long ref_block_prefix;
        public List<Action> actions;
    }

    public class Action{
        public String account;
        public String name;
        public ActionData data;
    }
    public class ActionData{
        public String from;
        public String to;
        public String quantity;
        public String memo;
    }
    public class Remark{
        public String text;
    }

    public class Block{
        public String previous;
        public String timestamp;
        public String transaction_mroot;
        public String action_mroot;
        public String block_mroot;
        public String producer;
        public String producer_signature;
        public String id;
        public int block_num;
        public long ref_block_prefix;

    }
}
