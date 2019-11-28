package com.ssu.readingd.util;

import android.graphics.drawable.Drawable;

public class CommunityItem {

    private String writer;
    private String date;
    private int page;
    private String email;
    private String content;
    private Drawable image;
    private String title;

    public Drawable getImage() {
        return this.image;
    }

    public String getWriter() {
        return writer;
    }

    public String getDate() {
        return date;
    }

    public int getPage() {
        return page;
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }



    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }







}
