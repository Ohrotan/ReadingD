package com.ssu.readingd.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDTO implements Parcelable {

    String id;
    String pwd;

    public UserDTO(){

    }

    public UserDTO(String id, String pw){
        this.id = id;
        this.pwd = pw;
    }

    protected UserDTO(Parcel in) {
        id = in.readString();
        pwd = in.readString();
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPwd(String pw) {
        this.pwd = pw;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pwd);
    }
}
