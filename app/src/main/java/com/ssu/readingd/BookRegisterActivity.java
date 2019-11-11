package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 책을 바코드나 제목으로 검색하여 선택 후 정보 입력 화면*/
public class BookRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_register);
    }
}
