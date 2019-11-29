package com.ssu.readingd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssu.readingd.adapter.BookResultListAdapter;
import com.ssu.readingd.dto.BookSimpleDTO;

import androidx.appcompat.app.AppCompatActivity;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일 내용: 책 바코드나 제목으로 검색했을 때 결과 화면 */
public class BookAddSearchResultActivity extends AppCompatActivity implements View.OnClickListener {
    TextView search_title_tv;
    ListView book_search_result_list;
    Button select_btn;

    BookSimpleDTO selecBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("도서 검색 결과");
        setContentView(R.layout.activity_book_add_search_result);
        search_title_tv = findViewById(R.id.search_title_tv);
        search_title_tv.setText(getIntent().getStringExtra("keyword") + search_title_tv.getText());
        select_btn = findViewById(R.id.select_btn);

        book_search_result_list = findViewById(R.id.book_search_result_list);

        book_search_result_list.setAdapter(new BookResultListAdapter(this));


        book_search_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            View lastView = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.WHITE);
                }
                lastView = view;
                view.setBackgroundColor(getColor(R.color.colorGray));
                selecBook = (BookSimpleDTO) parent.getAdapter().getItem(position);
            }
        });

    }

    public void clickTab(View v) {
        ImageView[] img = new ImageView[5];
        img[0] = findViewById(R.id.tab_flash_back);
        img[1] = findViewById(R.id.tab_memo);
        img[2] = findViewById(R.id.tab_book);
        img[3] = findViewById(R.id.tab_share);
        img[4] = findViewById(R.id.tab_setting);

        if (v == img[0]) {
            Intent intent = new Intent(this, FlashbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[1]) {
            Intent intent = new Intent(this, MemoListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[2]) {
            Intent intent = new Intent(this, BookShelfActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[3]) {
            Intent intent = new Intent(this, CommunityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[4]) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == select_btn) {
            Toast.makeText(this, selecBook.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
