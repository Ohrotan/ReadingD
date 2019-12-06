package com.ssu.readingd.dto;


import android.os.Parcel;
import android.os.Parcelable;

public class MemoListDTO implements Parcelable {

    private int id;
    private String memoBookName;
    private String memoBookAuthor;
    private String memoRegDate;
    private String memoContent;
    private int memoPage;
    private String memoImage;

    public MemoListDTO(int id, String bookName, String author, int page, String regDate, String memo, String image){

        this.id = id;
        this.memoBookName = bookName;
        this.memoBookAuthor = author;
        this.memoPage = page;
        this.memoRegDate = regDate;
        this.memoContent = memo;
        this.memoImage = image;

    }

    public MemoListDTO(String bookName, String author, String memo){
        this.memoBookName = bookName;
        this.memoBookAuthor = author;
        this.memoContent = memo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMemoBookName(String memoBookName) {
        this.memoBookName = memoBookName;
    }

    public void setMemoBookAuthor(String memoBookAuthor) {
        this.memoBookAuthor = memoBookAuthor;
    }

    public void setMemoImage(String memoImage) {
        this.memoImage = memoImage;
    }

    public int getId() {
        return id;
    }

    public String getMemoBookName() {
        return memoBookName;
    }

    public String getMemoBookAuthor() {
        return memoBookAuthor;
    }

    public MemoListDTO(){
        super();
    }


    protected MemoListDTO(Parcel in) {
        memoRegDate = in.readString();
        memoContent = in.readString();
        memoPage = in.readInt();
        memoImage = in.readString();
    }

    public String getMemoImage() {
        return memoImage;
    }

    public void setMemoImages(String memoImage) {
        this.memoImage = memoImage;
    }

    public String getMemoRegDate() {
        return memoRegDate;
    }

    public String getMemoContent() {
        return memoContent;
    }

    public int getMemoPage() {
        return memoPage;
    }

    public void setMemoRegDate(String memoRegDate) {
        this.memoRegDate = memoRegDate;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public void setMemoPage(int memoPage) {
        this.memoPage = memoPage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memoRegDate);
        dest.writeString(memoContent);
        dest.writeInt(memoPage);
        dest.writeString(memoImage);
    }

    public static final Creator<MemoListDTO> CREATOR = new Creator<MemoListDTO>() {
        @Override
        public MemoListDTO createFromParcel(Parcel in) {
            return new MemoListDTO(in);
        }

        @Override
        public MemoListDTO[] newArray(int size) {
            return new MemoListDTO[size];
        }
    };
}
