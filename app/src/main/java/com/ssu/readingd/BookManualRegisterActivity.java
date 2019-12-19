package com.ssu.readingd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.custom_enum.BookState;
import com.ssu.readingd.dto.BookDTO;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.util.DBUtil;
import com.ssu.readingd.util.ImageViewFromURL;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 책 정보 직접 입력 화면*/
public class BookManualRegisterActivity extends AppCompatActivity implements View.OnClickListener {
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

    BookSimpleDTO selecBook;
    BookDTO result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        start_date_tv.setText(year + "." + (month + 1) + "." + day);
        end_date_tv.setText(year + "." + (month + 1) + "." + day);

        if (getIntent().getStringExtra("mode") != null && getIntent().getStringExtra("mode").equals("edit")) {

            FirebaseFirestore.getInstance().collection("books")
                    .whereEqualTo("book_name", getIntent().getStringExtra("bookName"))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        result = doc.toObject(BookDTO.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setViews(result);
                            }
                        });
                    }
                }
            });


            getSupportActionBar().setTitle("책 수정");

        } else {
            getSupportActionBar().setTitle("책 등록");
            selecBook = getIntent().getParcelableExtra("book");
            result = new BookDTO(selecBook);
            setViews(result);
        }


        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);


        state = getResources().getStringArray(R.array.state);

        state_spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, state));
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (result != null) {
                    if (i == 0) {
                        result.setState(BookState.FUTURE);
                    } else if (i == 1) {
                        result.setState(BookState.NOW);
                    } else if (i == 2) {
                        result.setState(BookState.PAST);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (result != null)
                    result.setState(BookState.NOW);
            }
        });


    }

    public void setViews(BookDTO result) {
        ImageViewFromURL.setImageView(this, book_cover_img, result.getImg());
        if (result.getBook_name() != null && !result.getBook_name().trim().equals("")) {
            name_etv.setText(result.getBook_name());
            name_etv.setEnabled(false);
        }

        if (result.getAuthor() != null && !result.getAuthor().trim().equals("")) {
            author_etv.setText(result.getAuthor());
            author_etv.setEnabled(false);
        }
        if (result.getTranslator() != null && !result.getTranslator().trim().equals("")) {
            trans_etv.setText(result.getTranslator());
            trans_etv.setEnabled(false);
        }
        if (result.getPublisher() != null && !result.getPublisher().trim().equals("")) {
            publisher_etv.setText(result.getPublisher());
            publisher_etv.setEnabled(false);
        }
        if (result.getPub_date() != null && !result.getPub_date().trim().equals("")) {
            pub_date_etv.setText(result.getPub_date());
            pub_date_etv.setEnabled(false);
        }
        if (result.getW_page() != 0) {
            whole_p_etv.setText(result.getW_page() + "");
            whole_p_etv.setEnabled(false);
        }

        if (result.getR_page() != 0) {
            read_p_etv.setText(result.getR_page() + "");
        }
        if (result.getState() != null) {
            state_spinner.setSelection(result.getState().ordinal());
            Log.v("enum", result.getState().ordinal() + "/" + result.getState().name());
        }
        if (result.getStart_date() != null && !result.getStart_date().trim().equals("")) {
            start_date_tv.setText(result.getStart_date() + "");
        }
        if (result.getEnd_date() != null && !result.getEnd_date().trim().equals("")) {
            end_date_tv.setText(result.getEnd_date() + "");
        }
        if (result.getRating() != 0) {
            ratingBar.setRating(result.getRating());
        }
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
            // BookDTO result = new BookDTO((BookSimpleDTO) getIntent().getParcelableExtra("book"));
            result.setUser_id("admin");
            if (!read_p_etv.getText().toString().equals("")) {
                result.setR_page(Integer.parseInt(read_p_etv.getText().toString()));
            }
            //result.setState(state_spinner.getSe);
            result.setStart_date(start_date_tv.getText().toString());
            result.setEnd_date(end_date_tv.getText().toString());
            result.setRating(ratingBar.getRating());

            Toast.makeText(this, result.toString() + " \n저장", Toast.LENGTH_SHORT).show();


            if (getIntent().getStringExtra("mode") != null
                    && getIntent().getStringExtra("mode").equals("edit")) {
                new DBUtil().updateBook(result.getId(), result);
            } else {
                result.setReg_date(year + "." + (month + 1) + "." + day);
                new DBUtil().addBook("admin", result);
                startActivity(new Intent(this, TestRanActivity.class));
            }

        }
    }


}
