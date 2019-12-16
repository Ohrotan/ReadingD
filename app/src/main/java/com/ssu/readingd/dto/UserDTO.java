package com.ssu.readingd.dto;

public class UserDTO {

    String id;
    String pwd;

    public UserDTO(){

    }

    public UserDTO(String id, String pw){
        this.id = id;
        this.pwd = pw;
    }

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



}
