package com.ssu.readingd.dto;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;


public class MemoDTO implements Parcelable {

    String book_name;
    String img[] = new String[10];
    String memo_text;
    int r_page;
    String reg_date;
    int w_page;
    String user_id;
    boolean share;

    public MemoDTO() {
        super();
    }

    public MemoDTO(String book_id) {
        super();
        this.book_name = book_name;
    }

    public MemoDTO(String book_name, int imgcnt, String[] img, String memo_text, int r_page, String reg_date, int w_page, String user_id, boolean share) {
        this.book_name = book_name;
       // this.imgcnt = imgcnt;
        this.img = img;
        this.memo_text = memo_text;
        this.r_page = r_page;
        this.reg_date = reg_date;
        this.w_page = w_page;
        this.user_id = user_id;
        this.share = share;
    }

    public MemoDTO(Parcel in) {
        this.book_name = in.readString();
     //   this.imgcnt = in.readInt();
      //  for (int i = 0; i < imgcnt; i++) {
      //      this.img[i] = in.readString();
      //}
   //     this.img = in.readStringArray();
        this.memo_text = in.readString();
        this.r_page = in.readInt();
        this.reg_date = in.readString();
        this.w_page = in.readInt();
        this.user_id = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.share = in.readBoolean();
        }
    }

    public String getBook_name() {
        return book_name;
    }
    public void setBook_name(String id) {
        this.book_name = book_name;
    }
  //  public int getImgcnt(){ return imgcnt;}
   // public void setImgcnt(int imgcnt){this.imgcnt = imgcnt;}
    public String[] getImg(){ return img;}
    public void setImg(String[] img){this.img = img;}
    public String getMemo_text(){return memo_text;}
    public void setMemo_text(String memo_text){this.memo_text = memo_text;}
    public int getR_page(){return r_page;}
    public void setR_page(int r_page){this.r_page = r_page;}
    public String getRegdate(){return reg_date;}
    public void setRegdate(String reg_date){this.reg_date = reg_date;}
    public int getW_page(){return w_page;}
    public void setW_page(int w_page){this.w_page = w_page;}
    public String getUser_id(){return user_id;}
    public void setUser_id(String user_id){this.user_id = user_id;}
    public boolean getShare(){return share;}
    public void setShare(boolean share){this.share = share;}

    public static MemoDTO parse(JsonObject jo) {
        MemoDTO dto = new MemoDTO();
        String book_name = null;
        int imgcnt = 0;
        String img[] = new String[10];
        String memo_text = null;
        int r_page = 0;
        String reg_date = null;
        int w_page = 0;
        String user_id = null;
        boolean share = false;


        return dto;
    }

    public static Creator<MemoDTO> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(book_name);
    //    dest.writeInt(imgcnt);
     //   for(int i =0; i<imgcnt; i++){
   //         dest.writeString(img[i]);
    //    }
        dest.writeStringArray(img);
        dest.writeString(memo_text);
        dest.writeInt(r_page);
        dest.writeString(reg_date);
        dest.writeInt(w_page);
        dest.writeString(user_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(share);
        }
    }


    public static final Creator<MemoDTO> CREATOR = new Creator<MemoDTO>() {
        @Override
        public MemoDTO createFromParcel(Parcel source) {
            return new MemoDTO(source);
        }

        @Override
        public MemoDTO[] newArray(int size) {
            return new MemoDTO[size];
        }
    };

}