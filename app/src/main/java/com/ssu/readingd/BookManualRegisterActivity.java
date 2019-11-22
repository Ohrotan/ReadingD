package com.ssu.readingd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 책 정보 직접 입력 화면*/
public class BookManualRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView book_cover_img;
    EditText name_etv;
    EditText author_etv;
    EditText trans_etv;
    EditText publisher_etv;
    EditText pub_date_etv;

    EditText read_p_etv;
    EditText whole_p_etv;
    Spinner state_spinner;
    TextView start_date_tv;
    TextView end_date_tv;
    RatingBar ratingBar;

    Button cancel_btn;
    Button save_btn;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manual_register);

        book_cover_img = findViewById(R.id.book_cover_img);
        name_etv = findViewById(R.id.name_etv);
        author_etv = findViewById(R.id.author_etv);
        trans_etv = findViewById(R.id.trans_etv);
        publisher_etv = findViewById(R.id.publisher_etv);
        pub_date_etv = findViewById(R.id.pub_date_etv);

        read_p_etv = findViewById(R.id.read_p_etv);
        whole_p_etv = findViewById(R.id.whole_p_etv);
        state_spinner = findViewById(R.id.state_spinner);
        start_date_tv = findViewById(R.id.start_date_tv);
        end_date_tv = findViewById(R.id.end_date_etv);
        ratingBar = findViewById(R.id.ratingBar);

        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);
    }

    @Override
    public void onClick(View v) {
        if(v==cancel_btn){
            Toast.makeText(this,"취소",Toast.LENGTH_SHORT).show();
        }else if(v==save_btn){
            Toast.makeText(this,"저장",Toast.LENGTH_SHORT).show();
        }
    }
}
