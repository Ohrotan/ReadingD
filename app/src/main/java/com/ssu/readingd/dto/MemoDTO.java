package com.ssu.readingd.dto;


import java.util.HashMap;
import java.util.List;


public class MemoDTO{

    String book_name;
    String book_author;
    List<String> img;
    String memo_text;
    int r_page;
    String reg_date;
    boolean share;
    String user_id;
    int w_page;

    public MemoDTO(){
        super();
    }

    public MemoDTO(String book_name, List<String> img, String memo_text, int r_page, String reg_date, boolean share, String user_id, int w_page){
        this.book_name = book_name;
        this.img = img;
        this.memo_text = memo_text;
        this.r_page = r_page;
        this.reg_date = reg_date;
        this.share = share;
        this.user_id = user_id;
        this.w_page = w_page;
    }

    public MemoDTO(String book_name,String book_author, List<String> img, String memo_text, int r_page, String reg_date, boolean share, String user_id, int w_page){
        this.book_name = book_name;
        this.book_author = book_author;
        this.img = img;
        this.memo_text = memo_text;
        this.r_page = r_page;
        this.reg_date = reg_date;
        this.share = share;
        this.user_id = user_id;
        this.w_page = w_page;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("book_name", book_name);
        result.put("book_author", book_author);
        result.put("img", img);
        result.put("memo_text", memo_text);
        result.put("r_page", r_page);
        result.put("reg_date",reg_date);
        result.put("share", share);
        result.put("user_id", user_id);
        result.put("w_page", w_page);

        return result;
    }

    public String getBook_name(){
        return book_name;
    }
    public List<String> getImgs(){
        return img;
    }
    public String getMemo_text(){
        return memo_text;
    }
    public int getR_page(){
        return r_page;
    }
    public String getReg_date(){
        return reg_date;
    }
    public boolean getShare(){
        return share;
    }
    public String getUser_id(){
        return user_id;
    }
    public int getW_page(){
        return w_page;
    }
    public void setBook_name(String book_name){
        this.book_name = book_name;
    }
    public void setImgs(List<String> img){
        this.img = img;
    }
    public void setMemo_text(String memo_text){
        this.memo_text = memo_text;
    }
    public void setR_page(int r_page){
        this.r_page = r_page;
    }
    public void setReg_date(String reg_date){
        this.reg_date = reg_date;
    }
    public void setShare(boolean share){
        this.share = share;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public void setW_page(int w_page){
        this.w_page = w_page;
    }
    public String getBook_author() { return book_author; }
    public void setBook_author(String book_author) { this.book_author = book_author; }


}