package com.mgc.letobox.happy.domain;

import java.util.List;

public class GuidanceResultBean {
    private int isSet;
    private List<LabelTag> tags;
    private List<LabelMems> mems;

    public void setIsSet(int isSet) {
        this.isSet = isSet;
    }

    public int getIsSet() {
        return isSet;
    }

    public void setTags(List<LabelTag> tags) {
        this.tags = tags;
    }

    public List<LabelTag> getTags() {
        return tags;
    }

    public void setMems(List<LabelMems> mems) {
        this.mems = mems;
    }

    public List<LabelMems> getMems() {
        return mems;
    }

    public class LabelTag {
        private String id;
        private String name;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public class LabelMems {
        private String id;
        private String nickname;
        private String portrait;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getPortrait() {
            return portrait;
        }
    }
}
