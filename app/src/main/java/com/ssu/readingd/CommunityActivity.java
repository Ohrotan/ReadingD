package com.ssu.readingd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoDTO;
import com.ssu.readingd.dto.UserDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    MemoListAdapter adapter;
    ImageButton memoBtn;
    Button memoSearchBtn;
    FirebaseFirestore db;
    ArrayList<MemoDTO> arrayList;

    int fromYear;
    int fromMonth;
    int fromDate;
    int toYear;
    int toMonth;
    int toDate;
    final Calendar cal = Calendar.getInstance();
    Dialog alertDialog;

    String from;
    String to;

    UserDTO user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.communityRecycler);
        memoBtn = findViewById(R.id.imageButton);
        memoSearchBtn = findViewById(R.id.searchBtn);

        memoBtn.setOnClickListener(this);
        memoSearchBtn.setOnClickListener(this);
        init();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String login_id = sharedPref.getString("id", "none");
        if (login_id.equals("none")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

//        if(user == null){
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivityForResult(intent, 1);
//        }
        init();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
//            user = data.getParcelableExtra("user");
//            init();
//        }
//        else if(resultCode == Activity.RESULT_CANCELED){
//            finish();
//        }
//
//    }


    public void init() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        adapter = new MemoListAdapter(this, arrayList, 2);
        recyclerView.setAdapter(adapter);

        db.collection("memos").whereEqualTo("share", true)
                .orderBy("reg_date", Query.Direction.DESCENDING)
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
                                MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                memoDTO.setMemo_id(doc.getId());
                                arrayList.add(memoDTO);
                            }
                        }
                        //어답터 갱신
                        adapter.notifyDataSetChanged();
                    }
                });


    }


    @Override
    public void onClick(View v) {

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


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                CommunitySearchResult(book_name, author, content, from, to);
                Intent intent = new Intent(v.getContext(), MemoSearchResultActivity.class);
                intent.putExtra("book_name", book_name);
                intent.putExtra("author", author);
                intent.putExtra("content", content);
                intent.putExtra("fromYear", fromYear);
                intent.putExtra("fromMonth", fromMonth);
                intent.putExtra("fromDate", fromDate);
                intent.putExtra("toYear", toYear);
                intent.putExtra("toMonth", toMonth);
                intent.putExtra("toDate", toDate);
                intent.putExtra("Activity", "CommunityActivity");

                startActivity(intent);


                alertDialog.dismiss();
            }
        });

        Button.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                DatePickerDialog dialog = new DatePickerDialog(CommunityActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String msg = String.format("%d.%d.%d", year, month + 1, date);

                        if (view == startDate) {
                            startDate.setText(msg);
                            fromYear = year;
                            fromMonth = month + 1;
                            fromDate = date;
                            from = fromYear + "-" + fromMonth + "-" + fromDate + " " + "00:00";
                        } else if (view == endDate) {
                            endDate.setText(msg);
                            toYear = year;
                            toMonth = month + 1;
                            toDate = date;
                            to = toYear + "-" + toMonth + "-" + toDate + " " + "23:59";
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


    public void CommunitySearchResult(String book_name, String author, String content, String from, String to) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        Query query = db.collection("memos").whereEqualTo("share", true);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this, arrayList, 2);
        recyclerView.setAdapter(adapter);

        if (!book_name.equals("") && author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("share", true);
        } else if (book_name.equals("") && !author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_author", author).whereEqualTo("share", true);
        } else if (book_name.equals("") && author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("content", content).whereEqualTo("share", true);
        } else if (!book_name.equals("") && !author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("share", true);
        } else if (!book_name.equals("") && author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("share", true);
        } else if (book_name.equals("") && !author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_author", author).whereEqualTo("content", content).whereEqualTo("share", true);
        } else if (!book_name.equals("") && !author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("content", content);
        } else {
            // name, author, content 없을 때
        }


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                        arrayList.add(memoDTO);
                    }
                }
                adapter.notifyDataSetChanged();
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
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            // finish();
        } else if (v == img[1]) {
            Intent intent = new Intent(this, MemoListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //  finish();
        } else if (v == img[2]) {
            Intent intent = new Intent(this, BookShelfActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //  finish();
        } else if (v == img[3]) {
            Intent intent = new Intent(this, CommunityActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //   finish();
        } else if (v == img[4]) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            //   finish();
        }
        //finish();
    }

}
