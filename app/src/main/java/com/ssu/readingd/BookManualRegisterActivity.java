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

import com.ssu.readingd.custom_enum.BookState;
import com.ssu.readingd.dto.BookDTO;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.DBUtil;
import com.ssu.readingd.util.ImageViewFromURL;

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
    BookDTO result;

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

        BookSimpleDTO selecBook = getIntent().getParcelableExtra("book");
        result = new BookDTO(selecBook);
        ImageViewFromURL.setImageView(this, book_cover_img, selecBook.getImg());
        if (selecBook.getBookName() != null && !selecBook.getBookName().trim().equals("")) {
            name_etv.setText(selecBook.getBookName());
            name_etv.setEnabled(false);
        }

        if (selecBook.getAuthor() != null && !selecBook.getAuthor().trim().equals("")) {
            author_etv.setText(selecBook.getAuthor());
            author_etv.setEnabled(false);
        }
        if (selecBook.getTranslator() != null && !selecBook.getTranslator().trim().equals("")) {
            trans_etv.setText(selecBook.getTranslator());
            trans_etv.setEnabled(false);
        }
        if (selecBook.getPublisher() != null && !selecBook.getPublisher().trim().equals("")) {
            publisher_etv.setText(selecBook.getPublisher());
            publisher_etv.setEnabled(false);
        }
        if (selecBook.getPubDate() != null && !selecBook.getPubDate().trim().equals("")) {
            pub_date_etv.setText(selecBook.getPubDate());
            pub_date_etv.setEnabled(false);
        }
        if (selecBook.getWPage() != 0) {
            whole_p_etv.setText(selecBook.getWPage() + "");
            whole_p_etv.setEnabled(false);
        }
        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);


        state = getResources().getStringArray(R.array.state);

        state_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, state));
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    result.setState(BookState.FUTURE);
                } else if (i == 1) {
                    result.setState(BookState.NOW);
                } else if (i == 2) {
                    result.setState(BookState.PAST);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                result.setState(BookState.NOW);
            }
        });

        start_date_tv.setText(year + "." + (month + 1) + "." + day);
        end_date_tv.setText(year + "." + (month + 1) + "." + day);

    }

    public void clickCalendar(View v) {
        final TextView dateTv = (TextView) v;

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
            BookDTO result = new BookDTO((BookSimpleDTO) getIntent().getParcelableExtra("book"));
            result.setUserId("admin");
            if (!read_p_etv.getText().toString().equals("")) {
                result.setRPage(Integer.parseInt(read_p_etv.getText().toString()));
            }
            //result.setState(state_spinner.getI);
            result.setStartDate(start_date_tv.getText().toString());
            result.setEndDate(end_date_tv.getText().toString());
            result.setRating(ratingBar.getRating());
            result.setReg_date(year + "." + (month + 1) + "." + day);
            Toast.makeText(this, result.toString() + " \n저장", Toast.LENGTH_SHORT).show();

            DBUtil.addBook("admin", result);


        }
    }


}
