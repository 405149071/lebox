package com.mgc.letobox.happy.domain;

/**
 * Created by liu hong liang on 2016/11/14.
 */

public class GradeListResultBean {

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private int level;
    private String rank;
    private String rule;
    private String pic;

    public int getMinRule() {
        return minRule;
    }

    public void setMinRule(int minRule) {
        this.minRule = minRule;
    }

    private int minRule;

}
