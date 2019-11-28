package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.content.Context;
import android.widget.Gallery;
import android.widget.ImageView;

/* 작성자: 박슬우
최초 작성일: 2019.11.16
파일 내용: 메모를 등록할 때 화면 */
public class MemoRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageSwitcher imageSwitcher;
    Button btnPrev,btnNext;
    int imageIds[] = {R.drawable.book_1, R.drawable.book_2};
    int count = imageIds.length;
    int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_register);

        final ImageSwitcher imageSwitcher = findViewById(R.id.image_switcher);
        btnPrev = (Button)findViewById(R.id.cancel_btn);
        btnNext = findViewById(R.id.save_btn);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory(){
            public View makeView(){

                ImageView imageview = new ImageView(getApplicationContext());
                imageview.setScaleType(ImageView.ScaleType.FIT_XY);
                imageview.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                return imageview;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        btnPrev.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                currentIndex++;
                if(currentIndex>=count)
                    currentIndex=0;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                currentIndex--;
                if(currentIndex<=0)
                    currentIndex=count-1;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
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
            startActivity(new Intent(this, FlashbackActivity.class));
            overridePendingTransition(0, 0);
        }

    }

    @Override
    public void onClick(View v) {
    }

}

