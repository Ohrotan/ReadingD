package com.ssu.readingd.dto;

public class BookSimpleDTO {
    String id;
    String img;
    String book_name;
    String author;
    String translator;
    String publisher;
    String pub_date;

    public BookSimpleDTO() {
        super();
    }

    public BookSimpleDTO(String id, String img, String book_name) {
        super();
        this.id = id;
        this.img = img;
        this.book_name = book_name;
    }

    public BookSimpleDTO(String id, String img, String book_name, String author, String translator, String publisher, String pub_date) {
        this.id = id;
        this.img = img;
        this.book_name = book_name;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.pub_date = pub_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBookName() {
        return book_name;
    }

    public void setBookName(String bookName) {
        this.book_name = bookName;
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

    public String getPubDate() {
        return pub_date;
    }

    public void setPubDate(String pub_date) {
        this.pub_date = pub_date;
    }

    @Override
    public String toString() {
        return "BookSimpleDTO{" +
                "id='" + id + '\'' +
                ", img=" + img +
                ", book_name='" + book_name + '\'' +
                ", author='" + author + '\'' +
                ", translator='" + translator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pub_date='" + pub_date + '\'' +
                '}';
    }
}
