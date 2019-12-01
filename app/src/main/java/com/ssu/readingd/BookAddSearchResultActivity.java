package com.ssu.readingd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssu.readingd.adapter.BookResultListAdapter;
import com.ssu.readingd.dto.BookSimpleDTO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import androidx.appcompat.app.AppCompatActivity;

/* 작성자: 조란
최초 작성일: 2019.11.11
파일 내용: 책 바코드나 제목으로 검색했을 때 결과 화면 */
public class BookAddSearchResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "api call";
    public static final int LOAD_SUCCESS = 101;
    ProgressDialog progressDialog;

    private final String SEARCH_URL = "http://seoji.nl.go.kr/landingPage/SearchApi.do";
    private final String API_KEY = "?cert_key=c594fa83326be40164ae013ab0a14ad8";
    private final String RESULT_STYLE = "&result_style=json";
    private String PAGE_NO = "&page_no=1";
    private String PAGE_SIZE = "&page_size=100";
    private String ISBN = "&isbn=";
    private String TITLE = "&title=";
    private String PUBLISHER = "&publisher=";
    private String AUTHOR = "&author=";
    private String SORT = "&sort=";
    private String REQUEST_URL = SEARCH_URL + API_KEY + RESULT_STYLE + PAGE_NO + PAGE_SIZE + ISBN + TITLE +
            PUBLISHER + AUTHOR + SORT;

    final ArrayList<BookSimpleDTO> resultList = new ArrayList<>();


    TextView search_title_tv;
    TextView result_count_tv;
    ListView book_search_result_list;
    Button select_btn;
    String keyword;
    BookSimpleDTO selecBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("도서 검색 결과");
        setContentView(R.layout.activity_book_add_search_result);
        search_title_tv = findViewById(R.id.search_title_tv);
        keyword = getIntent().getStringExtra("keyword");
        search_title_tv.setText("'" + keyword + "' " + search_title_tv.getText());
        result_count_tv = findViewById(R.id.result_count_tv);

        select_btn = findViewById(R.id.select_btn);

        TITLE = TITLE + keyword;
        setREQUEST_URL();
        getJSON();
        book_search_result_list = findViewById(R.id.book_search_result_list);

        book_search_result_list.setAdapter(new BookResultListAdapter(this, resultList));


        book_search_result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            View lastView = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.WHITE);
                }
                lastView = view;
                view.setBackgroundColor(getColor(R.color.colorGray));
                selecBook = (BookSimpleDTO) parent.getAdapter().getItem(position);
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

    @Override
    public void onClick(View v) {
        if (v == select_btn) {

            if (selecBook != null) {
                Intent intent = new Intent(this, BookManualRegisterActivity.class);
                intent.putExtra("book", selecBook);
                startActivity(intent);
            } else {
                Toast.makeText(this, "책을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setREQUEST_URL() {
        REQUEST_URL = SEARCH_URL + API_KEY + RESULT_STYLE + PAGE_NO + PAGE_SIZE + ISBN + TITLE +
                PUBLISHER + AUTHOR + SORT;
    }

    public void setPAGE_NO(String num) {
        PAGE_NO = "&page_no=" + num;
    }

    public void setPAGE_SIZE(String num) {
        PAGE_SIZE = "&page_size=" + num;
    }

    public void sortResult() {
        ArrayList<BookSimpleDTO> first = new ArrayList<>();
        ArrayList<BookSimpleDTO> second = new ArrayList<>();
        Iterator<BookSimpleDTO> iterator = resultList.iterator();
        BookSimpleDTO dto;
        while (iterator.hasNext()) {
            dto = iterator.next();
            if (dto.getBookName().equals(keyword) || dto.getBookName().trim().equals(keyword.trim())) {
                iterator.remove();
                first.add(dto);
            } else if (dto.getBookName().startsWith(keyword) ||
                    dto.getBookName().trim().startsWith(keyword.trim())) {
                iterator.remove();
                second.add(dto);

            }
        }

        resultList.addAll(0, second);
        resultList.addAll(0, first);
    }


    private final MyHandler mHandler = new MyHandler(this);


    static class MyHandler extends Handler {
        private final WeakReference<BookAddSearchResultActivity> weakReference;

        public MyHandler(BookAddSearchResultActivity mainactivity) {
            weakReference = new WeakReference<BookAddSearchResultActivity>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {

            BookAddSearchResultActivity mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        // mainactivity.progressDialog.dismiss();

                        String jsonString = (String) msg.obj;
                        Log.v(TAG, jsonString);
                        JsonParser jp = new JsonParser();
                        JsonElement je = jp.parse(jsonString);
                        String count = je.getAsJsonObject().get("TOTAL_COUNT").getAsString();
                        if (false) {
                            //!count.equals(mainactivity.PAGE_SIZE.substring(11)) ||
                            //!"999".equals(mainactivity.PAGE_SIZE.substring(11))) {
                            if (Integer.parseInt(count) >= 1000) {
                                mainactivity.setPAGE_SIZE("999");
                            } else {
                                mainactivity.setPAGE_SIZE(count);
                            }
                            mainactivity.setREQUEST_URL();
                            mainactivity.getJSON();
                        } else {
                            mainactivity.result_count_tv.setText(50 + " 건");
                            JsonArray ja = je.getAsJsonObject().getAsJsonArray("docs");
                            for (int i = 0; i < ja.size(); i++) {
                                mainactivity.resultList.add(BookSimpleDTO.parse(ja.get(i).getAsJsonObject()));
                            }
                            mainactivity.sortResult();
                        }
                        // Toast.makeText(mainactivity, jsonString, Toast.LENGTH_SHORT).show();

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
