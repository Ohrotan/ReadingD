package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestSeulwooActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn101;
    Button btn102;
    Button btn103;
    Button btn104;

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
          //  intent = new Intent(this,MemoListActivity.class);
           // startActivity(intent);

        } else if(v == btn102){
           // intent = new Intent(this,BookMemoListActivity.class);
           // startActivity(intent);

        }else if(v == btn103){
            intent = new Intent(this,MemoRegisterActivity.class);
            startActivity(intent);

        }else if(v == btn104) {
            intent = new Intent(this, MemoEditActivity.class);
            startActivity(intent);
        }

    }


}
