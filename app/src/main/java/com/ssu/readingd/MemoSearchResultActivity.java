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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.adapter.MemoListAdapter;
import com.ssu.readingd.dto.MemoDTO;

import java.util.ArrayList;

public class MemoSearchResultActivity extends AppCompatActivity {

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


        if(activity == "BookMemoListActivity"){
            BookMemoSearchResult();
        }
        else{
            MemoListSearchResult();
        }

        MemoListSearchResult();

        sortMemoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //최신순 정렬
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
                                            Log.d("hs_test", "읽기 성공", e);
                                            MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                            arrayList.add(0, memoDTO);
                                        }
                                        Log.d("hs_test", "읽기 성공222", e);
                                    }
                                    //어답터 갱신
                                    adapter.notifyDataSetChanged();
                                }
                            });

                }
                else{
                    //책이름순
                    db.collection("memos").orderBy("book_name")
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

    public void MemoListSearchResult(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");


        arrayList = new ArrayList<>();



        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this,arrayList,1);
        recyclerView.setAdapter(adapter);




        if(!book_name.equals("") && author.equals("") && content.equals("")){
            db.collection("memos").whereEqualTo("book_name",book_name)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
        else if(book_name.equals("") && !author.equals("") && content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_author",author)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }
        else if(book_name.equals("") && author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("content",content)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }
        else if( !book_name.equals("") && !author.equals("") && content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }
        else if( !book_name.equals("") && author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        else if( book_name.equals("") && !author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_author",author).whereEqualTo("content",content)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        else if( !book_name.equals("") && !author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("content",content)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        else if( book_name.equals("") && author.equals("") && content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
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
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }










        /*
        List<String> title = Arrays.asList("제목1", "제목2", "제목3", "제목4", "제목5");
        List<String> author = Arrays.asList("저자1", "저자2", "저자3", "저자4", "저자5");
        List<String> date = Arrays.asList("1111", "1112", "1113", "1114", "1115");
        List<Integer> page = Arrays.asList(222,433,333,1111,222);
        //List<String> image = Arrays.asList("이미지1", "이미지2", "이미지3", "이미지4", "이미지5");
        List<String> memo = Arrays.asList("내용내용내용내용내용내용1","내용내용내용내용내용내용2","내용내용내용내용내용내용내용내용3","내용내용내용내용내용내용내용내용내용내용내용내용4","내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용5");


        for (int i = 0; i < title.size(); i++) {

            MemoDTO data = new MemoDTO();
            data.setBook_author(author.get(i));
            data.setBook_name(title.get(i));
            data.setReg_date(date.get(i));
            data.setR_page(page.get(i));
            data.setMemo_text(memo.get(i));

            adapter.addItem(data);
        }

        */


    }

    public void BookMemoSearchResult(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MemoListAdapter(this,arrayList,1);
        recyclerView.setAdapter(adapter);


        if(!author.equals("") && content.equals("")){
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
        else if( author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("memo_text",content)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        else if( !author.equals("") && !content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name).whereEqualTo("book_author",author).whereEqualTo("memo_text",content)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        else if( author.equals("") && content.equals("")){
            Log.d("hs_test", "author 만 검색한 경우");
            db.collection("memos").whereEqualTo("book_name",book_name)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.d("hs_test", "Listen failed.", e);
                                return;
                            }

                            int count = value.size();
                            arrayList.clear();//일딴 초기화 해줘야 한다. 안 그럼 기존 데이터에 반복해서 뒤에 추가된다.
                            for (QueryDocumentSnapshot doc : value) {
                                if (doc.get("book_name") != null) {
                                    Log.d("hs_test", "메모 검색 성공", e);

                                    MemoDTO memoDTO = doc.toObject(MemoDTO.class);
                                    arrayList.add(memoDTO);


                                }
                                Log.d("hs_test", "메모 검색 성공222", e);
                            }
                            //어답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                    });

        }



    }
}
