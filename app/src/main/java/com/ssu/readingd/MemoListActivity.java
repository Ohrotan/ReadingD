package com.ssu.readingd;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoListDTO;

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


    public void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this);
        recyclerView.setAdapter(adapter);

        List<String> memo = Arrays.asList("제목1", "제목2", "제목3", "제목4", "제목5");
        List<String> date = Arrays.asList("1111", "1112", "1113", "1114", "1115");
        List<Integer> page = Arrays.asList(222,433,333,1111,222);
        List<String> image = Arrays.asList("이미지1", "이미지2", "이미지3", "이미지4", "이미지5");

        for (int i = 0; i < memo.size(); i++) {

            MemoListDTO data = new MemoListDTO();
            data.setMemoBookName(memo.get(i));
            data.setMemoRegDate(date.get(i));
            data.setMemoPage(page.get(i));
            data.setMemoImages(image.get(i));

            adapter.addItem(data);
        }


    }


}
