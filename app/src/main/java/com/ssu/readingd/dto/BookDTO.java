package com.ssu.readingd.dto;

import com.ssu.readingd.custom_enum.BookStatus;

public class BookDTO extends BookSimpleDTO {

    int r_page;
    int w_page;
    Enum<BookStatus> status;
    String start_date;
    String end_date;
    float rating;
    String user_id;

    public BookDTO() {
        super();
    }

    public BookDTO(String id, String img, String book_name) {
        super(id, img, book_name);
    }

    public BookDTO(String id, String img, String book_name, String author, String translator,
                   String publisher, String pub_date) {
        super(id, img, book_name, author, translator, publisher, pub_date);
    }


    public BookDTO(String id, String img, String book_name, String author, String translator,
                   String publisher, String pub_date, int r_page, int w_page, Enum<BookStatus> status,
                   String start_date, String end_date, float rating, String user_id) {
        super(id, img, book_name, author, translator, publisher, pub_date);
        this.status = status;
        this.r_page = r_page;
        this.w_page = w_page;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rating = rating;
        this.user_id = user_id;
    }

    public int getRPage() {
        return r_page;
    }

    public void setRPage(int r_page) {
        this.r_page = r_page;
    }

    public int getWPage() {
        return w_page;
    }

    public void setWPage(int w_page) {
        this.w_page = w_page;
    }

    public Enum<BookStatus> getStatus() {
        return status;
    }

    public void setStatus(Enum<BookStatus> status) {
        this.status = status;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String end_date) {
        this.end_date = end_date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id='" + id + '\'' +
                ", img='" + img + '\'' +
                ", book_name='" + book_name + '\'' +
                ", author='" + author + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pub_date='" + pub_date + '\'' +
                ", r_page=" + r_page +
                ", w_page=" + w_page +
                ", status=" + status +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", rating=" + rating +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
