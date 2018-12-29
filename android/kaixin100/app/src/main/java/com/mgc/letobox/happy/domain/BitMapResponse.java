package com.mgc.letobox.happy.domain;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by DELL on 2018/8/15.
 */

public class BitMapResponse implements Comparable<BitMapResponse> {

    private int id;
    private Bitmap bitmap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int compareTo(@NonNull BitMapResponse bitMapResponse) {
        int i = this.getId() - bitMapResponse.getId();
        return i;
    }
}
