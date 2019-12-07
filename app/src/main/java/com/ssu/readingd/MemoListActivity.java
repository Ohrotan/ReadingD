package com.ssu.readingd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoDTO;

import java.util.Arrays;
import java.util.List;

public class MemoListActivity extends AppCompatActivity {

    MemoListAdapter adapter;
    RecyclerView recyclerView;
    Spinner sortMemoSpinner;
    ImageButton memoBtn;
    EditText memoListSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        sortMemoSpinner = (Spinner)findViewById(R.id.sortMemoSpinner);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        memoBtn = (ImageButton)findViewById(R.id.memoListBtn);
        memoListSearchText = (EditText)findViewById(R.id.memoListSearchText);

        init();



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


    public void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this);
        recyclerView.setAdapter(adapter);

        List<String> title = Arrays.asList("제목1", "제목2", "제목3", "제목4", "제목5");
        List<String> date = Arrays.asList("1111", "1112", "1113", "1114", "1115");
        List<Integer> page = Arrays.asList(222,433,333,1111,222);
        //List<String> image = Arrays.asList("이미지1", "이미지2", "이미지3", "이미지4", "이미지5");
        List<String> memo = Arrays.asList("내용내용내용내용내용내용1","내용내용내용내용내용내용2","내용내용내용내용내용내용내용내용3","내용내용내용내용내용내용내용내용내용내용내용내용4","내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용5");


        for (int i = 0; i < title.size(); i++) {

            MemoDTO data = new MemoDTO();
            data.setBook_name(title.get(i));
            data.setReg_date(date.get(i));
            data.setR_page(page.get(i));
            data.setMemo_text(memo.get(i));

            adapter.addItem(data);
        }


    }


}
