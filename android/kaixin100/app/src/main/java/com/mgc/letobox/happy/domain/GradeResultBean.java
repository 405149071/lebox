package com.mgc.letobox.happy.domain;


public class GradeResultBean {
    private int level;  //等级
    //头衔
    private String rank;
    //达标规则
    private String rule;
    //等级图片
    private String pic;
    //累计金额
    private float score;
    //是否实名认证

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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getIs_have_idcard() {
        return is_have_idcard;
    }

    public void setIs_have_idcard(int is_have_idcard) {
        this.is_have_idcard = is_have_idcard;
    }

    private int is_have_idcard;

    public int getIs_face() {
        return is_face;
    }

    public void setIs_face(int is_face) {
        this.is_face = is_face;
    }

    private int is_face;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    private String  portrait;

}
