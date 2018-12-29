package com.mgc.letobox.happy.domain;


import java.util.List;

/**
 * Created by DELL on 2018/8/2.
 */

public class SignResponse extends BaseResultBean {


    /**
     * rules : [{"num":"1","award":"20"},{"num":"2","award":"20"},{"num":"3","award":"20"},{"num":"4","award":"20"},{"num":"5","award":"20"},{"num":"6","award":"20"},{"num":"7","award":"20"}]
     * sign_day : 1
     * sign_award : 20
     * is_sign : 1
     */

    private int sign_day;
    private int sign_award;
    private int is_sign;
    private List<RulesBean> rules;
    private int next_sign_award;

    public int getNext_sign_award() {
        return next_sign_award;
    }

    public void setNext_sign_award(int next_sign_award) {
        this.next_sign_award = next_sign_award;
    }

    public int getSign_day() {
        return sign_day;
    }

    public void setSign_day(int sign_day) {
        this.sign_day = sign_day;
    }

    public int getSign_award() {
        return sign_award;
    }

    public void setSign_award(int sign_award) {
        this.sign_award = sign_award;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }

    public List<RulesBean> getRules() {
        return rules;
    }

    public void setRules(List<RulesBean> rules) {
        this.rules = rules;
    }

    public static class RulesBean {
        /**
         * num : 1
         * award : 20
         */

        private int num;
        private int award;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getAward() {
            return award;
        }

        public void setAward(int award) {
            this.award = award;
        }

        public boolean isSign() {
            return isSign;
        }

        public void setSign(boolean sign) {
            isSign = sign;
        }

        public boolean isSign;

        public boolean isTaday;

        public boolean isTaday() {
            return isTaday;
        }

        public void setTaday(boolean taday) {
            isTaday = taday;
        }
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private String pic;

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    private float balance;
}
