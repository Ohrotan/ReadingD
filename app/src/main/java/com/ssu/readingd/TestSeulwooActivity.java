package com.ssu.readingd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class TestSeulwooActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn101;
    Button btn102;
    Button btn103;
    Button btn104;
    Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_seulwoo);

        btn101 = findViewById(R.id.button101);
        btn102 = findViewById(R.id.button102);
        btn103 = findViewById(R.id.button103);
        btn104 = findViewById(R.id.button104);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v == btn101){
            AlertDialog customDialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View customDialogView =inflater.inflate(R.layout.layout_memo_delete,null);
            builder.setView(customDialogView);

//            builder.setPositiveButton("확인", null);
//            builder.setNegativeButton("취소", null);

            customDialog = builder.create();
            customDialog.show();


        } else if(v == btn102){

        }else if(v == btn103){
            intent = new Intent(this,MemoRegisterActivity.class);
            startActivity(intent);

        }else if(v == btn104) {
            intent = new Intent(this, MemoEditActivity.class);
            startActivity(intent);
        }

    }


}
