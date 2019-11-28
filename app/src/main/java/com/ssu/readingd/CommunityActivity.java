package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ListView;

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
}
