package com.ssu.readingd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoDTO;

import java.util.ArrayList;

public class MemoSearchResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MemoListAdapter adapter;
    RecyclerView recyclerView;
    Spinner sortMemoSpinner;
    ImageButton memoBtn;
    EditText memoListSearchText;
    Intent intent;
    ArrayList<MemoDTO> arrayList;
    FirebaseFirestore db;

    String book_name;
    String author;
    String content;
    int fromYear;
    int fromMonth;
    int fromDate;
    int toYear;
    int toMonth;
    int toDate;
    String activity;
    String login_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_search_result);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        sortMemoSpinner = (Spinner) findViewById(R.id.sortMemoSpinner);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        memoBtn = (ImageButton) findViewById(R.id.memoListBtn);
        memoListSearchText = (EditText) findViewById(R.id.memoListSearchText);

        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        book_name = intent.getStringExtra("book_name");
        if (book_name.equals(""))
            book_name = null;
        author = intent.getStringExtra("author");
        if (author.equals(""))
            author = null;
        content = intent.getStringExtra("content");
        if (content.equals(""))
            content = null;
        fromYear = intent.getIntExtra("fromYear", 0);
        fromMonth = intent.getIntExtra("fromMonth", 0);
        fromDate = intent.getIntExtra("fromDate", 0);
        toYear = intent.getIntExtra("toYear", 0);
        toMonth = intent.getIntExtra("toMonth", 0);
        toDate = intent.getIntExtra("toDate", 0);
        activity = intent.getStringExtra("Activity");

        SharedPreferences sharedPref= PreferenceManager. getDefaultSharedPreferences (this);
        String login_id=sharedPref.getString("id", "none");
        if(login_id.equals("none")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        sortMemoSpinner.setOnItemSelectedListener(this);
        Log.v("search_test", book_name + "/" + author + "/" + content);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (activity.equals("BookMemoListActivity")) {
            BookMemoSearchResult(position);
        } else if (activity.equals("CommunityActivity")) {
            CommunitySearchResult(position);
        } else {
            MemoListSearchResult(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void AddMemo(View v) {

        Intent intent = new Intent(this, MemoRegisterActivity.class);
        startActivity(intent);

    }

    public void MemoListSearchResult(int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        //      Query query = db.collection("memos").whereEqualTo("book_name", book_name);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this, arrayList, 0);
        recyclerView.setAdapter(adapter);

/*
        if (!book_name.equals("") && author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name);
        } else if (book_name.equals("") && !author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_author", author);
        } else if (book_name.equals("") && author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("content", content);
        } else if (!book_name.equals("") && !author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author);
        } else if (!book_name.equals("") && author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author);
        } else if (book_name.equals("") && !author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_author", author).whereEqualTo("content", content);
        } else if (!book_name.equals("") && !author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("content", content);
        }
*/

        // 책제목, 작가, 내용 없을 때 -> 전체메모 보여주기
        if (position == 0) {
            //최신순
            db.collection("memos").orderBy("reg_date", Query.Direction.DESCENDING).whereEqualTo("user_id", login_id)
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
                                MemoDTO dto = doc.toObject(MemoDTO.class);
                                if (book_name != null && dto.getBook_name().contains(book_name)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (author != null && dto.getBook_author().contains(author)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (content != null && dto.getMemo_text().contains(content)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                }
                                Log.d("hs_test", "메모 불러오기", e);

                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else if (position == 1) {
            //오래된순
            memoRef.orderBy("reg_date").whereEqualTo("user_id", login_id)
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

                                MemoDTO dto = doc.toObject(MemoDTO.class);
                                if (book_name != null && dto.getBook_name().contains(book_name)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (author != null && dto.getBook_author().contains(author)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (content != null && dto.getMemo_text().contains(content)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                }
                                Log.d("hs_test", "메모 불러오기", e);

                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else {
            memoRef.orderBy("book_name").whereEqualTo("user_id", login_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        MemoDTO dto = doc.toObject(MemoDTO.class);
                        if (book_name != null && dto.getBook_name().contains(book_name)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (author != null && dto.getBook_author().contains(author)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (content != null && dto.getMemo_text().contains(content)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);

                        }
                        Log.d("hs_test", "메모 불러오기", e);
                    }
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });
        }

/*
        if (position == 0) {
            //최신순
            query.whereEqualTo("user_id", login_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (position == 1) {
            //오래된순
            query.whereEqualTo("user_id", login_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            arrayList.add(0, memoDTO);
                        }
                    }
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            query.orderBy("Book_name").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });
        }
        */


    }

    public void BookMemoSearchResult(int position) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        Query query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("user_id", login_id);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this, arrayList, 1);
        recyclerView.setAdapter(adapter);


        if (!author.equals("") && content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author);
        } else if (author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("memo_text", content);
        } else if (!author.equals("") && !content.equals("")) {
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("memo_text", content);
        } else {
            // 작가, 메모내용 없을 때
        }

        if (position == 0) {
            //최신순 정렬
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
        } else if (position == 1) {
            //오래된순 정렬
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
                            arrayList.add(0, memoDTO);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (position == 2) {
            //책제목순 정렬
            query.orderBy("book_name").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    }


    public void CommunitySearchResult(int position) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        Query query = db.collection("memos").whereEqualTo("share", true);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this, arrayList, 2);
        recyclerView.setAdapter(adapter);
/*
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
            query = db.collection("memos").whereEqualTo("book_name", book_name).whereEqualTo("book_author", author).whereEqualTo("content", content).
                    whereEqualTo("share", true);
        } else {
            // name, author, content 없을 때
        }
*/

        if (position == 0) {
            //최신순 정렬
            query.orderBy("reg_date", Query.Direction.DESCENDING)
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

                                MemoDTO dto = doc.toObject(MemoDTO.class);
                                if (book_name != null && dto.getBook_name().contains(book_name)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (author != null && dto.getBook_author().contains(author)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                } else if (content != null && dto.getMemo_text().contains(content)) {
                                    dto.setMemo_id(doc.getId());
                                    arrayList.add(dto);
                                }
                                Log.d("hs_test", "메모 불러오기", e);

                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else if (position == 1) {
            //오래된순 정렬
            query.orderBy("reg_date").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        MemoDTO dto = doc.toObject(MemoDTO.class);
                        if (book_name != null && dto.getBook_name().contains(book_name)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (author != null && dto.getBook_author().contains(author)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (content != null && dto.getMemo_text().contains(content)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        }
                        Log.d("hs_test", "메모 불러오기", e);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (position == 2) {
            //책제목순 정렬
            query.orderBy("book_name").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        MemoDTO dto = doc.toObject(MemoDTO.class);
                        if (book_name != null && dto.getBook_name().contains(book_name)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (author != null && dto.getBook_author().contains(author)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        } else if (content != null && dto.getMemo_text().contains(content)) {
                            dto.setMemo_id(doc.getId());
                            arrayList.add(dto);
                        }
                        Log.d("hs_test", "메모 불러오기", e);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }


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
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[1]) {
            Intent intent = new Intent(this, MemoListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[2]) {
            Intent intent = new Intent(this, BookShelfActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[3]) {
            Intent intent = new Intent(this, CommunityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (v == img[4]) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

    }


}
