package com.ssu.readingd;

class BookResultListItem {
    String bookId;
    int bookImg;
    String bookName;
    String author;
    String translator;
    String publisher;
    String date;

    public BookResultListItem() {
    }

    public BookResultListItem(String bookId, int bookImg, String bookName) {
        this.bookId = bookId;
        this.bookImg = bookImg;
        this.bookName = bookName;
    }

    public BookResultListItem(String bookId, int bookImg, String bookName, String author, String translator, String publisher, String date) {
        this.bookId = bookId;
        this.bookImg = bookImg;
        this.bookName = bookName;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.date = date;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getBookImg() {
        return bookImg;
    }

    public void setBookImg(int bookImg) {
        this.bookImg = bookImg;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BookResultListItem{" +
                "bookId='" + bookId + '\'' +
                ", bookImg=" + bookImg +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
