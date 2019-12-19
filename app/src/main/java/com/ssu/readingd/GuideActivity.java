package com.ssu.readingd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().setTitle("사용 설명");
    }
}
