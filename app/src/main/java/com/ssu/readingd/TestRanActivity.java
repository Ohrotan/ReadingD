package com.ssu.readingd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestRanActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;

    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ran);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v == btn1){ //책추가 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View bookAddPopup = inflater.inflate(R.layout.layout_book_add_search,null);
            builder.setView(bookAddPopup);
            builder.setPositiveButton("확인",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(TestRanActivity.this,
                            "책 제목으로 검색 이동",Toast.LENGTH_SHORT).show();
                        //제목이나 저자로 책검색 이동

                }
            });

            builder.setNegativeButton("취소",null);


            dialog = builder.create();
            dialog.show();
            return;
        }else if(v == btn2){
             intent = new Intent(this,BookAddSearchResultActivity.class);
            startActivity(intent);

        }else if(v == btn3){
             intent = new Intent(this,BookManualRegisterActivity.class);
            startActivity(intent);

        }else if(v == btn4){
            intent = new Intent(this,BookRegisterActivity.class);
            startActivity(intent);
        }else if(v == btn5){
            intent = new Intent(this,FlashbackActivity.class);
            startActivity(intent);
        }else if(v == btn6){
            //디비 테스트

        }

    }
}
