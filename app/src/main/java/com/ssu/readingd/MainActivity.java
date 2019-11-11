package com.ssu.readingd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1;
    Button btn2;
    Button btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.park_btn);
        btn2 = findViewById(R.id.yim_btn);
        btn3 = findViewById(R.id.jo_btn);


    }

    @Override
    public void onClick(View v) {
        if(v== btn1){//박슬우

        }else if(v== btn2){//임혜선

        }else if(v== btn3){//조란
            Intent intent = new Intent(this,TestRanActivity.class);
            startActivity(intent);
        }
    }
}
