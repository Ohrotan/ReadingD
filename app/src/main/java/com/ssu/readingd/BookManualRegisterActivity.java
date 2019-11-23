package com.ssu.readingd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 책 정보 직접 입력 화면*/
public class BookManualRegisterActivity extends BookRegisterActivity {
    EditText read_p_etv;
    EditText whole_p_etv;
    Spinner state_spinner;
    TextView start_date_tv;
    TextView end_date_tv;
    RatingBar ratingBar;

    ImageView book_cover_img;
    EditText name_etv;
    EditText author_etv;
    EditText trans_etv;
    EditText publisher_etv;
    EditText pub_date_etv;

    Button cancel_btn;
    Button save_btn;

    String[] state;

    Calendar c;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("책 등록");
        setContentView(R.layout.activity_book_manual_register);

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

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


        state = getResources().getStringArray(R.array.state);

        state_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, state));
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BookManualRegisterActivity.this, state[i] + "가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        start_date_tv.setText(year + "." + (month + 1) + "." + day);
        end_date_tv.setText(year + "." + (month + 1) + "." + day);

    }
    public void clickCalendar(View v) {
        final TextView dateTv =(TextView)v;

        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTv.setText(year + "." + (month + 1) + "." + dayOfMonth);
            }
        }, year, month, day);

        dateDialog.show();

    }

    @Override
    public void onClick(View v) {
        if (v == cancel_btn) {
            onBackPressed();
        } else if (v == save_btn) {
            Toast.makeText(this, "저장", Toast.LENGTH_SHORT).show();
        }
    }


}
