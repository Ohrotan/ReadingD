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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.adapter.BookShelfAdapter;
import com.ssu.readingd.dto.BookSimpleDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookShelfActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner sortSpinner;
    Spinner bookKindSpinner;
    BookShelfAdapter adapter;
    ImageButton imageButton;
    Button bookSearchBtn;
    //GridView gridView;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton deleteBtn;
    Query query;

    int fromYear;
    int fromMonth;
    int fromDate;
    int toYear;
    int toMonth;
    int toDate;
    final Calendar cal = Calendar.getInstance();
    Dialog alertDialog;

    ArrayList<BookSimpleDTO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);

        sortSpinner = (Spinner)findViewById(R.id.sortBookSpinner);
        bookKindSpinner = (Spinner)findViewById(R.id.bookKindSpinner);
        imageButton = (ImageButton)findViewById(R.id.shelfSearchBtn);
        bookSearchBtn = (Button)findViewById(R.id.bookSearchBtn);
        //gridView = findViewById(R.id.gridView);
        recyclerView = findViewById(R.id.recyclerView);
        deleteBtn = findViewById(R.id.deleteBtn);

        deleteBtn.setOnClickListener(this);

        arrayList = new ArrayList<>();
        bookSearchBtn.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        init();


    }

    public void init(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(gridLayoutManager);
        arrayList = new ArrayList<>();
        adapter = new BookShelfAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);


        bookKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //읽은 책
                    query = db.collection("books").whereEqualTo("state", "DONE");
                }
                else if(position==1){
                    //읽고있는 책
                    query = db.collection("books").whereEqualTo("state", "NOW");

                }
                else if(position==2){
                    //읽고싶은책
                    query = db.collection("books").whereEqualTo("state", "FUTURE");
                }
                else{
                    //전체
                    db.collection("books")
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
                                            BookSimpleDTO book = doc.toObject(BookSimpleDTO.class);
                                            Log.d("hs_test", book.toString());
                                            arrayList.add(book);
                                        }
                                    }
                                    //어답터 갱신
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    return;
                }

                query
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
                                        BookSimpleDTO book = doc.toObject(BookSimpleDTO.class);
                                        Log.d("hs_test", book.toString());
                                        arrayList.add(book);
                                    }
                                }
                                //어답터 갱신
                                adapter.notifyDataSetChanged();
                            }
                        });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            startActivity(new Intent(this, FlashbackActivity.class));
            overridePendingTransition(0, 0);
        }

    }


    @Override
    public void onClick(View v) {

        if(v==deleteBtn){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

            recyclerView.setLayoutManager(gridLayoutManager);
            arrayList = new ArrayList<>();
            adapter = new BookShelfAdapter(this, arrayList, true);
            recyclerView.setAdapter(adapter);

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        //등록순 정렬
                        db.collection("books")
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
                                                BookSimpleDTO book = doc.toObject(BookSimpleDTO.class);
                                                Log.d("hs_test", book.toString());
                                                arrayList.add(book);
                                            }
                                        }
                                        //어답터 갱신
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                    else if(position == 1){
                        //제목순
                        db.collection("books").orderBy("book_name")
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
                                                BookSimpleDTO book = doc.toObject(BookSimpleDTO.class);
                                                Log.d("hs_test", book.toString());
                                                arrayList.add(book);
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

        else if(v==bookSearchBtn || v==imageButton){
            View dialogView = getLayoutInflater().inflate(R.layout.memo_search_layout, null);

            Button cancelBtn = dialogView.findViewById(R.id.searchCancelBtn);
            Button searchBtn = dialogView.findViewById(R.id.searchBtn);
            final EditText nameSearchTxt = dialogView.findViewById(R.id.nameSearchText);
            final EditText writerSearchTxt = dialogView.findViewById(R.id.writerSearchText);
            final EditText contentSearchTxt = dialogView.findViewById(R.id.contentSearchText);
            final Button startDate = dialogView.findViewById(R.id.startDate);
            final Button endDate = dialogView.findViewById(R.id.endDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);


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

                    final String book_name = nameSearchTxt.getText().toString();
                    final String author = writerSearchTxt.getText().toString();
                    final String content = contentSearchTxt.getText().toString();

                    Intent intent = new Intent(v.getContext(), MemoSearchResultActivity.class);
                    intent.putExtra("book_name", book_name);
                    intent.putExtra("author", author);
                    intent.putExtra("content", content);
                    intent.putExtra("fromYear", fromYear);
                    intent.putExtra("fromMonth",fromMonth);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toYear",toYear);
                    intent.putExtra("toMonth",toMonth);
                    intent.putExtra("toDate",toDate);
                    intent.putExtra("Activity", "BookShelfActivity");

                    startActivity(intent);


                    alertDialog.dismiss();
                }
            });

            Button.OnClickListener btnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    DatePickerDialog dialog = new DatePickerDialog(BookShelfActivity.this, new DatePickerDialog.OnDateSetListener() {
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
