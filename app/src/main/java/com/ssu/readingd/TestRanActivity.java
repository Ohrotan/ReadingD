package com.ssu.readingd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssu.readingd.util.StillImageActivity;

import java.io.InputStream;

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

    final int PERMISSIONS_REQUEST_CAMERA = 900;
    final int REQUEST_CODE = 901;

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

    //카메라 퍼미션
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "카메라 승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "카메라 권한을 승인받지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btn1) { //책추가 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View bookAddPopup = inflater.inflate(R.layout.layout_book_add_search, null);
            Button barcode = bookAddPopup.findViewById(R.id.barcode_search_btn);
            barcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TestRanActivity.this, StillImageActivity.class));
                   /*
                    if (ContextCompat.checkSelfPermission(TestRanActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(TestRanActivity.this,
                                Manifest.permission.CAMERA)) {
                        } else {
                            ActivityCompat.requestPermissions(TestRanActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSIONS_REQUEST_CAMERA);
                        }
                    }


//앨범에서 이미지 가져오기
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE);

*/
                }

            });
            builder.setView(bookAddPopup);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    EditText keyword = bookAddPopup.findViewById(R.id.api_search_etv);

                    Intent intent = new Intent(TestRanActivity.this, BookAddSearchResultActivity.class);
                    intent.putExtra("keyword", keyword.getText().toString());

                    startActivity(intent);
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
/*
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait.....");
            progressDialog.show();
            tv = new TextView(this);

           // getJSON();
            BookAPITask rest = new BookAPITask("http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key=c594fa83326be40164ae013ab0a14ad8\n" +
                    "&category=[단행자료:dan]&kwd=[테스트]");
            String url = "https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg";
            url = "https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/%EC%A0%9C%EB%AA%A9%20%EC%97%86%EC%9D%8C.png";
             ImageViewFromURL.setImageView(this,img_v,url);
*/
            //  DBUtil.addUser("ygj02054", "123");


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("barcode", requestCode + "/" + resultCode + "/");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    Log.v("barcode", "img read end");


                    // FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromByteBuffer(data, );
                    //  imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }


}
