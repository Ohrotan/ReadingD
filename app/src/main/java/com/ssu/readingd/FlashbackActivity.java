package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 랜덤으로 메모 1개 보여주는 화면*/
public class FlashbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("FLASH BACK");
        setContentView(R.layout.activity_flashback);
    }
    public void clickTab(View v) {
        ImageView[] img = new ImageView[5];
        img[0] = findViewById(R.id.tab_flash_back);
        img[1] = findViewById(R.id.tab_memo);
        img[2] = findViewById(R.id.tab_book);
        img[3] = findViewById(R.id.tab_share);
        img[4] = findViewById(R.id.tab_setting);

        if(v==img[0]){
            startActivity(new Intent(this,FlashbackActivity.class));
            overridePendingTransition(0, 0);
        }
        if(v==img[2]){
            startActivity(new Intent(this,BookAddSearchResultActivity.class));
            overridePendingTransition(0, 0);
        }

    }
}