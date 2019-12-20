package com.ssu.readingd;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.ssu.readingd.adapter.BookShelfAdapter;
import com.ssu.readingd.dto.BookSimpleDTO;

import java.util.ArrayList;

public class BookShelfSearchActivity extends AppCompatActivity {

    Spinner sortSpinner;
    Spinner bookKindSpinner;
    BookShelfAdapter adapter;
    ImageButton imageButton;
    Button bookSearchBtn;
    ImageButton addBookBtn;
    //GridView gridView;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton deleteBtn;
    Query query;

    AlertDialog dialog;
    Dialog alertDialog;

    String book_name;
    String author;

    String login_id;

    boolean delete = false;

    ArrayList<BookSimpleDTO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf_search);

        //gridView = findViewById(R.id.gridView);
        recyclerView = findViewById(R.id.recyclerView);

        Intent intent = getIntent();
        book_name = intent.getStringExtra("book_name");
        author = intent.getStringExtra("author");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        login_id = sharedPref.getString("id", "none");
        if (login_id.equals("none")) {
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
        }


        init();

    }

    private void init() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference memoRef = db.collection("memos");
        //      Query query = db.collection("memos").whereEqualTo("book_name", book_name);

        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BookShelfAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);


        db.collection("books").whereEqualTo("user_id", login_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("hs_test", "Listen failed.", e);
                    return;
                }
                arrayList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    BookSimpleDTO dto = doc.toObject(BookSimpleDTO.class);
                    if ( !book_name.toString().equals("") && dto.getBook_name().contains(book_name)) {
                        //dto.set(doc.getId());
                        arrayList.add(dto);
                        Log.d("hs_test", "책 제목 검색");
                    } else if (!author.toString().equals("") && dto.getBook_name().contains(author)) {
                        //dto.setMemo_id(doc.getId());
                        arrayList.add(dto);
                        Log.d("hs_test", "메모 불러오기", e);
                    }
                }
                //어답터 갱신
                adapter.notifyDataSetChanged();
            }
        });


    }


}