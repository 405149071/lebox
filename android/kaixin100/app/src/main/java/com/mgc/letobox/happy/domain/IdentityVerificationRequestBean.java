package com.mgc.letobox.happy.domain;


public class IdentityVerificationRequestBean extends BaseRequestBean {
    String cardno;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
