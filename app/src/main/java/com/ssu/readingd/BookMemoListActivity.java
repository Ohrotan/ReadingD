package com.ssu.readingd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.BookSimpleDTO;
import com.ssu.readingd.dto.MemoDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookMemoListActivity extends AppCompatActivity implements View.OnClickListener{

    MemoListAdapter adapter;
    RecyclerView recyclerView;
    Spinner sortMemoSpinner;
    ImageButton memoBtn;
    Button memoSearchBtn;
    ImageButton addMemoBtn;
    ImageButton bookEditBtn;
    TextView bookNameTitle;
    FirebaseFirestore db;
    BookSimpleDTO book;
    String strBookName;


    int fromYear;
    int fromMonth;
    int fromDate;
    int toYear;
    int toMonth;
    int toDate;
    final Calendar cal = Calendar.getInstance();
    Dialog alertDialog;

    MemoDTO memoDTO;
    ArrayList<MemoDTO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_memo_list);

        db = FirebaseFirestore.getInstance();
        sortMemoSpinner = (Spinner)findViewById(R.id.sortMemoSpinner);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        memoBtn = (ImageButton)findViewById(R.id.memoListBtn);
        memoSearchBtn = (Button)findViewById(R.id.memoSearchBtn);
        addMemoBtn = (ImageButton)findViewById(R.id.addBookBtn);
        bookEditBtn = (ImageButton)findViewById(R.id.book_edit_btn);
        bookNameTitle = findViewById(R.id.book_name_title);

        addMemoBtn.setOnClickListener(this);
        bookEditBtn.setOnClickListener(this);
        memoSearchBtn.setOnClickListener(this);
        memoBtn.setOnClickListener(this);


        // memoDTO 인텐트 받으면 여기에 넣기
        List<String> imgs = new ArrayList<>();
        Intent intent = getIntent();
        BookSimpleDTO book = intent.getParcelableExtra("book");
        strBookName = book.getBook_name();

        //memoDTO = new MemoDTO("책이름", "b", imgs, "내용", 231, "2019.12.13",false,"admin",5555);
        Log.d("hs_test","book : "+book.toString());
        bookNameTitle.setText(book.getBook_name());


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
            Intent intent = new Intent(this, FlashbackActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[1]) {
            Intent intent = new Intent(this, MemoListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[2]) {
            Intent intent = new Intent(this, BookShelfActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[3]) {
            Intent intent = new Intent(this, CommunityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[4]) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }


    public void init(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        adapter = new MemoListAdapter(this,arrayList,0);
        recyclerView.setAdapter(adapter);

        sortMemoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //최신순 정렬
                    db.collection("memos").whereEqualTo("book_name", strBookName)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.d("hs_test", "Listen failed.", e);
                                        return;
                                    }
                                    int count = value.size();
                                    arrayList.clear();
                                    for (QueryDocumentSnapshot doc : value) {
                                        if (doc.get("book_name") != null) {
                                            MemoDTO book = doc.toObject(MemoDTO.class);
                                            arrayList.add(book);

                                        }
                                    }
                                    //어답터 갱신
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
                else{
                    //오래된순
                    db.collection("memos").whereEqualTo("book_name", strBookName)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {

                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.d("hs_test", "Listen failed.", e);
                                        return;
                                    }
                                    int count = value.size();
                                    arrayList.clear();
                                    for (QueryDocumentSnapshot doc : value) {
                                        if (doc.get("book_name") != null) {
                                            MemoDTO book = doc.toObject(MemoDTO.class);
                                            arrayList.add(0, book);
                                        }
                                    }
                                    //어답터 갱신
                                    adapter.notifyDataSetChanged();
                                }
                            });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });









    }


    @Override
    public void onClick(View v) {
        if(v==addMemoBtn){
            Intent intent = new Intent(this, MemoRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if(v == bookEditBtn){
            Intent intent = new Intent(this, BookManualRegisterActivity.class);
            Log.v("bookname",strBookName);
            intent.putExtra("mode","edit");
            intent.putExtra("bookName",strBookName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if(v == memoSearchBtn || v==memoBtn){
            View dialogView = getLayoutInflater().inflate(R.layout.layout_book_memo_search, null);

            Button cancelBtn = dialogView.findViewById(R.id.searchCancelBtn_b);
            Button searchBtn = dialogView.findViewById(R.id.searchBtn_b);
            final EditText writerSearchTxt = dialogView.findViewById(R.id.writerSearchText_b);
            final EditText contentSearchTxt = dialogView.findViewById(R.id.contentSearchText_b);
            final Button startDate = dialogView.findViewById(R.id.startDate_b);
            final Button endDate = dialogView.findViewById(R.id.endDate_b);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);


            //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //alertDialog.setContentView(R.layout.memo_search_layout);




            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    alertDialog.dismiss();
                }
            });

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MemoListActivity.this, "검색", Toast.LENGTH_SHORT).show();

                    final String author = writerSearchTxt.getText().toString();
                    final String content = contentSearchTxt.getText().toString();

                    Intent intent = new Intent(v.getContext(), MemoSearchResultActivity.class);
                    intent.putExtra("book_name", book.getBook_name());
                    intent.putExtra("author", author);
                    intent.putExtra("content", content);
                    intent.putExtra("fromYear", fromYear);
                    intent.putExtra("fromMonth",fromMonth);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toYear",toYear);
                    intent.putExtra("toMonth",toMonth);
                    intent.putExtra("toDate",toDate);
                    intent.putExtra("Activity", "BookMemoListActivity");

                    startActivity(intent);


                    alertDialog.dismiss();
                }
            });

            Button.OnClickListener btnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    DatePickerDialog dialog = new DatePickerDialog(BookMemoListActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                            String msg = String.format("%d.%d.%d", year, month+1, date);

                            if(view==startDate){
                                startDate.setText(msg);
                                fromYear = year;
                                fromMonth = month+1;
                                fromDate = date;
                            }
                            else if(view == endDate){
                                endDate.setText(msg);
                                toYear = year;
                                toMonth = month+1;
                                toDate = date;
                            }

                            //Toast.makeText(MemoListActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                    dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                    dialog.show();


                }
            };

            startDate.setOnClickListener(btnListener);
            endDate.setOnClickListener(btnListener);


            alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
