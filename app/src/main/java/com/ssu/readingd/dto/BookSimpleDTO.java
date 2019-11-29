package com.ssu.readingd.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

public class BookSimpleDTO implements Parcelable {
    String id;
    String img;
    String book_name;
    String author;
    String translator;
    String publisher;
    String pub_date;
    String isbn;
    int w_page;

    public BookSimpleDTO() {
        super();
    }

    public BookSimpleDTO(String id, String img, String book_name) {
        super();
        this.id = id;
        this.img = img;
        this.book_name = book_name;
    }

    public BookSimpleDTO(String id, String img, String book_name, String author, String translator,
                         String publisher, String pub_date, String isbn, int w_page) {
        this.id = id;
        this.img = img;
        this.book_name = book_name;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.pub_date = pub_date;
        this.isbn = isbn;
        this.w_page = w_page;
    }

    public BookSimpleDTO(Parcel in) {
        this.id = in.readString();
        this.img = in.readString();
        this.book_name = in.readString();
        this.author = in.readString();
        this.translator = in.readString();
        this.publisher = in.readString();
        this.pub_date = in.readString();
        this.isbn = in.readString();
        this.w_page = in.readInt();
    }

    public BookSimpleDTO(BookSimpleDTO simple) {
        this.id = simple.id;
        this.img = simple.img;
        this.book_name = simple.book_name;
        this.author = simple.author;
        this.translator = simple.translator;
        this.publisher = simple.publisher;
        this.pub_date = simple.pub_date;
        this.isbn = simple.isbn;
        this.w_page = simple.w_page;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getWPage() {
        return w_page;
    }

    public void setWPage(int w_page) {
        this.w_page = w_page;
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
                ", isbn='" + isbn + '\'' +
                ", w_page='" + w_page + '\'' +
                '}';
    }

    public static BookSimpleDTO parse(JsonObject jo) {
        BookSimpleDTO dto = new BookSimpleDTO();
        String title = null;
        String author = null;
        String publisher = null;
        String date = null;
        String translator = null;
        String isbn = null;
        String pagestr = null;
        int wpage = 0;

        if (jo.get("TITLE") != null) {
            title = jo.get("TITLE").toString();
            title = title.substring(1, title.length() - 1);
        }
        if (jo.get("AUTHOR") != null) {
            author = jo.get("AUTHOR").toString();
            author = author.substring(1, author.length() - 1);

        }
        if (jo.get("PUBLISHER") != null) {
            publisher = jo.get("PUBLISHER").toString();
            publisher = publisher.substring(1, publisher.length() - 1);
        }
        if (jo.get("TRANSLATOR") != null) {
            translator = jo.get("TRANSLATOR").toString();
            translator = translator.substring(1, translator.length() - 1);
        }
        if (jo.get("PUBLISH_PREDATE") != null) {
            date = jo.get("PUBLISH_PREDATE").toString();

            if (date.length() > 8) {
                date = date.substring(1, 5) + "-" + date.substring(5, 7) + "-" + date.substring(7, 9);
            } else {
                date = null;
            }
        }
        if (jo.get("EA_ISBN") != null) {
            isbn = jo.get("EA_ISBN").toString();
            isbn = isbn.substring(1, isbn.length() - 1);
        }
        if (jo.get("PAGE") != null) {
            pagestr = jo.get("PAGE").toString();
            String pagetmp = new String();
            for (int i = 0; i < pagestr.length(); i++) {
                if (pagestr.charAt(i) <= '9' && pagestr.charAt(i) >= '0') {
                    pagetmp = pagetmp + pagestr.charAt(i);
                }
            }
            if (pagetmp.length() > 0) {
                wpage = Integer.parseInt(pagetmp);
            }
        }


        dto.setBookName(title);
        dto.setAuthor(author);
        dto.setTranslator(translator);
        dto.setPublisher(publisher);
        dto.setPubDate(date);

        String imgUrl = jo.get("TITLE_URL").toString();
        String[] temp = imgUrl.split("http");
        if (temp.length == 3) {
            imgUrl = "http" + temp[2].substring(0, temp[2].length() - 1);
        } else if (temp.length == 2) {
            imgUrl = "http" + temp[1].substring(0, temp[1].length() - 1);
            ;
        } else if (temp.length == 1) {
            imgUrl = null;
        }
        dto.setImg(imgUrl);
        dto.setIsbn(isbn);
        dto.setWPage(wpage);
        //Log.v("api", dto.getBookName() + " ===> " + dto.getImg());

        return dto;
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
    }

    public static final Creator<BookSimpleDTO> CREATOR = new Creator<BookSimpleDTO>() {
        @Override
        public BookSimpleDTO createFromParcel(Parcel source) {
            return new BookSimpleDTO(source);
        }

        @Override
        public BookSimpleDTO[] newArray(int size) {
            return new BookSimpleDTO[size];
        }
    };
}
