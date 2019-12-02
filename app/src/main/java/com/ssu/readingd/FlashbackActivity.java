package com.ssu.readingd;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일내용: 랜덤으로 메모 1개 보여주는 화면*/
public class FlashbackActivity extends AppCompatActivity {

    TextView book_title_tv;
    TextView autour_tv;
    TextView page_tv;
    TextView date_tv;
    ImageSwitcher imgs;
    TextView memo_tv;
    Map<String,Object> memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("FLASH BACK");
        setContentView(R.layout.activity_flashback);
        book_title_tv = findViewById(R.id.fbook_title_tv);
        autour_tv = findViewById(R.id.fauthor_tv);
        page_tv = findViewById(R.id.fpage_tv);
        date_tv = findViewById(R.id.fdate_tv);
        imgs = findViewById(R.id.fimage_switcher);
        memo_tv = findViewById(R.id.fmemo_tv);

        final String TAG = "Async Task";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("memos").whereEqualTo("user_id", "admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int total = task.getResult().size();
                            Random r = new Random();
                            int i = r.nextInt(total);
                            memo = task.getResult().getDocuments().get(i).getData();
                            if (memo == null) {
                                Log.v(TAG, "memo null");
                            } else {
                                Log.v(TAG, "memo" + memo.get("book_name"));
                                book_title_tv.setText((String)memo.get("book_name"));
                                page_tv.setText(((long) memo.get("r_page"))+" p");
                                date_tv.setText((String) memo.get("reg_date"));
                                memo_tv.setText((String) memo.get("memo_text"));
                            }
                        } else {
                            Log.w(TAG, "Error getting memo documents.", task.getException());
                        }
                    }
                });
        /*
        DBUtil db = new DBUtil();
        db.getRandomMemo("admin");
        SystemClock.sleep(1500);
        Map<String, Object> result = db.getMemo();
        if (result == null) {
            Log.v("flashback", "null");
        } else {

            book_title_tv.setText((String) result.get("book_name"));
            page_tv.setText((String) result.get("r_page"));
            date_tv.setText((String) result.get("reg_date"));
            memo_tv.setText((String) result.get("memo_text"));
        }

         */
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



