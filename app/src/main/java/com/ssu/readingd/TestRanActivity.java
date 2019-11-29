package com.ssu.readingd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssu.readingd.util.BookAPITask;
import com.ssu.readingd.util.DBUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TestRanActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;

    AlertDialog dialog;

    private static final String TAG = "api call";
    public static final int LOAD_SUCCESS = 101;
    ProgressDialog progressDialog;

    private final String SEARCH_URL = "http://seoji.nl.go.kr/landingPage/SearchApi.do";
    private final String API_KEY = "?cert_key=c594fa83326be40164ae013ab0a14ad8";
    private final String RESULT_STYLE = "&result_style=json";
    private String PAGE_NO = "&page_no=1";
    private String PAGE_SIZE = "&page_size=1";
    private String ISBN = "&isbn=";
    private String TITLE = "&title=";
    private String PUBLISHER = "&publisher=";
    private String AUTHOR = "&author=이병률";
    private String SORT = "&sort=";
    private String REQUEST_URL = SEARCH_URL + API_KEY + RESULT_STYLE + PAGE_NO + PAGE_SIZE + ISBN + TITLE +
            PUBLISHER + AUTHOR + SORT;

    private TextView tv;
    private ImageView img_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //img 받아오기
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ran);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        tv = findViewById(R.id.tv);
        img_v = findViewById(R.id.img_v);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btn1) { //책추가 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View bookAddPopup = inflater.inflate(R.layout.layout_book_add_search, null);
            builder.setView(bookAddPopup);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(TestRanActivity.this,
                            "책 제목으로 검색 이동", Toast.LENGTH_SHORT).show();
                    //제목이나 저자로 책검색 이동

                }
            });

            builder.setNegativeButton("취소", null);

            dialog = builder.create();
            dialog.show();
            return;
        } else if (v == btn2) {
            intent = new Intent(this, BookAddSearchResultActivity.class);
            startActivity(intent);

        } else if (v == btn3) {
            intent = new Intent(this, BookManualRegisterActivity.class);
            startActivity(intent);

        } else if (v == btn4) {
            intent = new Intent(this, BookRegisterActivity.class);
            startActivity(intent);
        } else if (v == btn5) {
            intent = new Intent(this, FlashbackActivity.class);
            startActivity(intent);
        } else if (v == btn6) {
            //디비 테스트

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait.....");
            progressDialog.show();
            tv = new TextView(this);

              getJSON();
            BookAPITask rest = new BookAPITask("http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key=c594fa83326be40164ae013ab0a14ad8\n" +
                    "&category=[단행자료:dan]&kwd=[테스트]");
            String url = "https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg";
            url = "https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C.png";
            // ImageViewFromURL.setImageView(this,img_v,url);

          //  DBUtil.addUser("ygj02054", "123");

        }

    }


    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<TestRanActivity> weakReference;

        public MyHandler(TestRanActivity mainactivity) {
            weakReference = new WeakReference<TestRanActivity>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            TestRanActivity mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainactivity.progressDialog.dismiss();

                        String jsonString = (String) msg.obj;
                        Log.v(TAG, jsonString);
                        mainactivity.tv.setText(jsonString);
                        Toast.makeText(mainactivity, jsonString, Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        }
    }

    public void getJSON() {

        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;

                try {

                    Log.d(TAG, REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();

                } catch (Exception e) {
                    result = e.toString();
                }

                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                mHandler.sendMessage(message);
            }

        });
        thread.start();
    }

}
