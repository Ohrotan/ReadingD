package com.ssu.readingd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CommunityActivity extends AppCompatActivity {

    ListView listview ;
    MemoListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        // Adapter 생성
        adapter = new MemoListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.communityListView);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("책제목", "저자", 342, "날짜", "이메일",ContextCompat.getDrawable(this,R.drawable.book_1), "내용");
        adapter.addItem("책제목", "저자", 342, "날짜", "이메일",ContextCompat.getDrawable(this,R.drawable.book_2), "내용");
        // 두 번째 아이템 추가.



    }

    public void clickTab(View v) {
        ImageView[] img = new ImageView[5];
        img[0] = findViewById(R.id.tab_flash_back);
        img[1] = findViewById(R.id.tab_memo);
        img[2] = findViewById(R.id.tab_book);
        img[3] = findViewById(R.id.tab_share);
        img[4] = findViewById(R.id.tab_setting);

        if (v == img[0]) {
            startActivity(new Intent(this, FlashbackActivity.class));
            overridePendingTransition(0, 0);
        }

    }
}
