package com.mgc.letobox.happy.util;

public class Util {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    private static int lastButtonId = -1;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastDoubleClick(int viewId) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (lastButtonId == viewId && lastClickTime > 0 && (curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        lastButtonId = viewId;
        return flag;
    }

    public static boolean isFastDoubleClick(int viewId, int delay) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (lastButtonId == viewId && lastClickTime > 0 && (curClickTime - lastClickTime) < delay) {
            flag = true;
        }
        lastClickTime = curClickTime;
        lastButtonId = viewId;
        return flag;
    }

    public static String getActionString(int actionValue) {
        String action;
        if (actionValue == 1) {
            action = "浏览奖励";
        } else if (actionValue == 6) {
            action = "分享成功";
        } else if (actionValue == 7) {
            action = "下载成功";
        } else if (actionValue == 8) {
            action = "加群成功";
        } else if (actionValue == 10) {
            action = "签到成功";
        } else {
            action = "操作成功";
        }
        return action;
    }
}
