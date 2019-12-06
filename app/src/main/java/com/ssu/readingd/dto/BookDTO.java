package com.ssu.readingd.dto;

import android.os.Parcel;

import com.ssu.readingd.custom_enum.BookState;

public class BookDTO extends BookSimpleDTO {

    int r_page;
    Enum<BookState> state;
    String start_date;
    String end_date;
    float rating;
    String user_id;
    String reg_date;


    public BookDTO() {
        super();
    }

    public BookDTO(String id, String img, String book_name) {
        super(id, img, book_name);
    }

    public BookDTO(String id, String img, String book_name, String author, String translator,
                   String publisher, String pub_date, String isbn, int w_page) {
        super(id, img, book_name, author, translator, publisher, pub_date, isbn, w_page);
    }

    public BookDTO(BookSimpleDTO simple) {
        super(simple);
    }

    public BookDTO(BookSimpleDTO simple, int r_page, Enum<BookState> state,
                   String start_date, String end_date, float rating, String user_id, String reg_date) {
        super(simple);
        this.state = state;
        this.r_page = r_page;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rating = rating;
        this.user_id = user_id;
        this.reg_date = reg_date;
    }

    public BookDTO(String id, String img, String book_name, String author, String translator,
                   String publisher, String pub_date, String isbn, int w_page, int r_page, Enum<BookState> state,
                   String start_date, String end_date, float rating, String user_id, String reg_date) {
        super(id, img, book_name, author, translator, publisher, pub_date, isbn, w_page);
        this.state = state;
        this.r_page = r_page;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rating = rating;
        this.user_id = user_id;
        this.reg_date = reg_date;
    }

    public BookDTO(Parcel in) {
        this.id = in.readString();
        this.img = in.readString();
        this.book_name = in.readString();
        this.author = in.readString();
        this.translator = in.readString();
        this.publisher = in.readString();
        this.pub_date = in.readString();
        this.isbn = in.readString();
        this.w_page = in.readInt();
        this.state = (Enum<BookState>) in.readValue(ClassLoader.getSystemClassLoader());
        this.r_page = in.readInt();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.rating = in.readFloat();
        this.user_id = in.readString();
        this.reg_date = in.readString();
    }

    public int getR_page() {
        return r_page;
    }

    public void setR_page(int r_page) {
        this.r_page = r_page;
    }

    public Enum<BookState> getState() {
        return state;
    }

    public void setState(Enum<BookState> state) {
        this.state = state;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
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
                ", state=" + state +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", rating=" + rating +
                ", user_id='" + user_id + '\'' +
                ", reg_date='" + reg_date + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(img);
        dest.writeString(book_name);
        dest.writeString(author);
        dest.writeString(translator);
        dest.writeString(publisher);
        dest.writeString(pub_date);
        dest.writeString(isbn);
        dest.writeInt(w_page);
        dest.writeValue(state);
        dest.writeInt(r_page);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeFloat(rating);
        dest.writeString(user_id);
        dest.writeString(reg_date);
    }

    public static final Creator<BookDTO> CREATOR = new Creator<BookDTO>() {
        @Override
        public BookDTO createFromParcel(Parcel source) {
            return new BookDTO(source);
        }

        @Override
        public BookDTO[] newArray(int size) {
            return new BookDTO[size];
        }
    };
}
