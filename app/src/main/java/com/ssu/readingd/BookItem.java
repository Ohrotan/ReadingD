package com.ssu.readingd;

import android.widget.ImageView;

public class BookItem {
    private int image;

    public BookItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}