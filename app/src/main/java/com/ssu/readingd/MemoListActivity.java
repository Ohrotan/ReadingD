package com.ssu.readingd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MemoListActivity extends AppCompatActivity implements View.OnClickListener {

    MemoListAdapter adapter;
    RecyclerView recyclerView;
    Spinner sortMemoSpinner;
    ImageButton memoBtn;
    Button memoSearchBtn;
    ImageButton addMemoTitleBtn;
    FirebaseFirestore db;
    StorageReference storageRef;
    LinearLayout searchBox;
    Spinner memoEditSpinner;
    String login_id;
    Context context;

    int fromYear;
    int fromMonth;
    int fromDate;
    int toYear;
    int toMonth;
    int toDate;
    final Calendar cal = Calendar.getInstance();
    Dialog alertDialog;


    ArrayList<MemoDTO> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        db = FirebaseFirestore.getInstance();

        //alertDialog = new Dialog(MemoListActivity.this);
        sortMemoSpinner = (Spinner) findViewById(R.id.sortMemoSpinner);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        memoBtn = (ImageButton) findViewById(R.id.memoListBtn);
        memoSearchBtn = (Button) findViewById(R.id.memoSearchBtn);
        addMemoTitleBtn = (ImageButton) findViewById(R.id.addBookBtn);
        searchBox = (LinearLayout) findViewById(R.id.searchBox);
        memoEditSpinner = findViewById(R.id.memoEditSpinner);
        context = this;

        memoSearchBtn.setOnClickListener(this);
        searchBox.setOnClickListener(this);
        memoBtn.setOnClickListener(this);

        init();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        login_id = sharedPref.getString("id", "none");
        if (login_id.equals("none")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

    }

    public void AddMemo(View v) {


        db.collection("memos").whereEqualTo("user_id", login_id)
                .orderBy("reg_date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Intent intent = new Intent(context, MemoRegisterActivity.class);
                                MemoDTO memo = document.toObject(MemoDTO.class);
                                intent.putExtra("memo", memo);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);

                                break;
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
          //  finish();
            overridePendingTransition(0, 0);
        } else if (v == img[1]) {
            Intent intent = new Intent(this, MemoListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
          //  finish();
            overridePendingTransition(0, 0);
        } else if (v == img[2]) {
            Intent intent = new Intent(this, BookShelfActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           // Toast.makeText(this,"bookshelf",Toast.LENGTH_SHORT).show();
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[3]) {
            Intent intent = new Intent(this, CommunityActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            //finish();
            overridePendingTransition(0, 0);
        } else if (v == img[4]) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

            overridePendingTransition(0, 0);
        }
        finish();
    }


    public void init() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        adapter = new MemoListAdapter(this, arrayList, 0);
        recyclerView.setAdapter(adapter);

        sortMemoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //최신순 정렬
                    db.collection("memos").whereEqualTo("user_id", login_id)
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
                } else if (position == 1) {
                    //오래된순
                    db.collection("memos").whereEqualTo("user_id", login_id)
                            .orderBy("reg_date", Query.Direction.ASCENDING).whereEqualTo("user_id", login_id)
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
                                            Log.d("hs_test", "읽기 성공", e);
                                            MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                            memoDTO.setMemo_id(doc.getId());
                                            arrayList.add(0, memoDTO);
                                        }
                                        Log.d("hs_test", "읽기 성공222", e);
                                    }
                                    //어답터 갱신
                                    adapter.notifyDataSetChanged();
                                }
                            });

                } else {
                    //책이름순
                    db.collection("memos")
                            .orderBy("book_name").whereEqualTo("user_id", login_id)
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == memoBtn || v == memoSearchBtn || v == searchBox) {
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
                    intent.putExtra("Activity", "MemoListActivity");

                    startActivity(intent);


                    alertDialog.dismiss();
                }
            });

            Button.OnClickListener btnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    DatePickerDialog dialog = new DatePickerDialog(MemoListActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                            String msg = String.format("%d.%d.%d", year, month + 1, date);

                            if (view == startDate) {
                                startDate.setText(msg);
                                fromYear = year;
                                fromMonth = month + 1;
                                fromDate = date;
                            } else if (view == endDate) {
                                endDate.setText(msg);
                                toYear = year;
                                toMonth = month + 1;
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
