package com.ssu.readingd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MemoSearchResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_search_result);

        sortMemoSpinner = (Spinner)findViewById(R.id.sortMemoSpinner);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        memoBtn = (ImageButton)findViewById(R.id.memoListBtn);
        memoListSearchText = (EditText)findViewById(R.id.memoListSearchText);

        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        book_name = intent.getStringExtra("book_name");
        author = intent.getStringExtra("author");
        content = intent.getStringExtra("content");
        fromYear = intent.getIntExtra("fromYear",0);
        fromMonth = intent.getIntExtra("fromMonth", 0);
        fromDate = intent.getIntExtra("fromDate",0);
        toYear = intent.getIntExtra("toYear", 0);
        toMonth = intent.getIntExtra("toMonth", 0);
        toDate = intent.getIntExtra("toDate", 0);
        activity = intent.getStringExtra("Activity");


        sortMemoSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(activity.equals("BookMemoListActivity")){
            BookMemoSearchResult(position);
        }
        else if(activity.equals("CommunityActivity")){
            CommunitySearchResult(position);
        }
        else{
            MemoListSearchResult(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    public void AddMemo(View v){

        Intent intent = new Intent(this, MemoRegisterActivity.class);
        startActivity(intent);

    }

    public void MemoListSearchResult(int position){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        Query query = db.collection("memos").whereEqualTo("book_name",book_name);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this, arrayList,0);
        recyclerView.setAdapter(adapter);

        if(!book_name.equals("") && author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name);
        }
        else if(book_name.equals("") && !author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_author",author);
        }
        else if(book_name.equals("") && author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("content",content);
        }
        else if( !book_name.equals("") && !author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author);
        }
        else if( !book_name.equals("") && author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author);
        }

        else if( book_name.equals("") && !author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_author",author).whereEqualTo("content",content);
        }

        else if( !book_name.equals("") && !author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("content",content);
        }

        else if( book_name.equals("") && author.equals("") && content.equals("")){
            // 책제목, 작가, 내용 없을 때 -> 전체메모 보여주기
            if(position == 0){
                //최신순
                db.collection("memos")
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
                                        Log.d("hs_test", "메모 불러오기", e);
                                    }
                                }
                                //어답터 갱신
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
            else if(position == 1){
                //오래된순
                memoRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
            }
            else {
                memoRef.orderBy("Book_name").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        }

        if(position == 0){
            //최신순
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
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else if(position == 1){
            //오래된순
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
                    //어답터 갱신
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else {
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



    }

    public void BookMemoSearchResult(int position){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        Query query = db.collection("memos").whereEqualTo("book_name",book_name);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this,arrayList,1);
        recyclerView.setAdapter(adapter);


        if(!author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author);
        }
        else if( author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("memo_text",content);
        }

        else if( !author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("memo_text",content);
        }
        else{
            // 작가, 메모내용 없을 때
        }

        if(position == 0){
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
        }
        else if(position == 1){
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
        }
        else if(position == 2){
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
        Query query = db.collection("memos").whereEqualTo("share",true);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this,arrayList,2);
        recyclerView.setAdapter(adapter);

        if(!book_name.equals("") && author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("share",true);
        }
        else if(book_name.equals("") && !author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_author",author).whereEqualTo("share",true);
        }
        else if(book_name.equals("") && author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("content",content).whereEqualTo("share",true);
        }
        else if( !book_name.equals("") && !author.equals("") && content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("share",true);
        }
        else if( !book_name.equals("") && author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("share",true);
        }

        else if( book_name.equals("") && !author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_author",author).whereEqualTo("content",content).whereEqualTo("share",true);
        }
        else if( !book_name.equals("") && !author.equals("") && !content.equals("")){
            query = db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("content",content).
                    whereEqualTo("share",true);
        }
        else {
            // name, author, content 없을 때
        }


        if(position == 0){
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
        }
        else if(position == 1){
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
        }
        else if(position == 2){
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


}
