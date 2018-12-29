package com.mgc.letobox.happy.domain;

import java.util.List;

public class MinigameResultBean {
    private List<BannerModel> banners;
    private List<GamesModel> games;

    public void setBanners(List<BannerModel> banners) {
        this.banners = banners;
    }

    public List<BannerModel> getBanners() {
        return banners;
    }

    public void setGames(List<GamesModel> games) {
        this.games = games;
    }

    public List<GamesModel> getGames() {
        return games;
    }

    public class BannerModel {
        private String title;
        private String pic;
        private String packageurl;
        private String gameid;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPic() {
            return pic;
        }

        public void setPackageurl(String packageurl) {
            this.packageurl = packageurl;
        }

        public String getPackageurl() {
            return packageurl;
        }

        public void setGameid(String gameid) {
            this.gameid = gameid;
        }

        public String getGameid() {
            return gameid;
        }
    }

    public class GamesModel {
        private String id;
        private String image;
        private String name;
        private int count;
        private String status;
        private String pid;
        private String parentid;
        private String listorder;
        private String classify;
        private String first_letter;
        private List<GameListModel> gameList;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setGameList(List<GameListModel> gameList) {
            this.gameList = gameList;
        }

        public List<GameListModel> getGameList() {
            return gameList;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPid() {
            return pid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getParentid() {
            return parentid;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getListorder() {
            return listorder;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getClassify() {
            return classify;
        }

        public void setFirst_letter(String first_letter) {
            this.first_letter = first_letter;
        }

        public String getFirst_letter() {
            return first_letter;
        }
    }

    public class GameListModel {
        private int id;
        private String name;
        private String play_num;
        private String icon;
        private String publicity;
        private String type_id;
        private String packageurl;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setPlay_num(String play_num) {
            this.play_num = play_num;
        }

        public String getPlay_num() {
            return play_num;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setPublicity(String publicity) {
            this.publicity = publicity;
        }

        public String getPublicity() {
            return publicity;
        }

        public void setPackageurl(String packageurl) {
            this.packageurl = packageurl;
        }

        public String getPackageurl() {
            return packageurl;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType_id() {
            return type_id;
        }
    }
}
