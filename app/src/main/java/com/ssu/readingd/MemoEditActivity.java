package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/* 메모 수정 */

public class MemoEditActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageSwitcher imageSwitcher;
    ImageButton btnPrev,btnNext, btnDelete;
    ImageView btnAddPhoto;
    SeekBar seekBar;
    TextView tvcurpage;

    int pagenumber;
    int imageIds[] = new int[10];
    int count = 0;
    int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_register);

        final ImageSwitcher imageSwitcher = findViewById(R.id.image_switcher);
        btnPrev =findViewById(R.id.prev_btn);
        btnNext = findViewById(R.id.next_btn);
        btnDelete = findViewById(R.id.imagedelete_btn);
        btnAddPhoto = findViewById(R.id.addphoto_bt);
        imageIds[count++] = R.drawable.memoimg1;
        imageIds[count++] = R.drawable.memoimg2;
        imageIds[count++] = R.drawable.memoimg3;
        imageIds[count++] = R.drawable.memoimg4;

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
        imageSwitcher.setImageResource(imageIds[currentIndex]);

        btnPrev.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                currentIndex--;
                if(currentIndex<=0)
                    currentIndex=count;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                currentIndex++;
                if(currentIndex>count-1)
                    currentIndex=0;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                for(int i = currentIndex; i < count; i++){
                    imageIds[i] = imageIds[i+1];
                }
                imageIds[--count] = 0;
                if(currentIndex >= count-1)
                    currentIndex--;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
            }
        });
        btnAddPhoto.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                imageIds[count++] = R.drawable.memoimgadd;
                currentIndex = count-1;
                imageSwitcher.setImageResource(imageIds[currentIndex]);
            }
        });

        seekBar = findViewById(R.id.page_seekbar);
        tvcurpage = findViewById(R.id.cur_page_tv);
        pagenumber = 0;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pagenumber = seekBar.getProgress();
                update();
            }

            public void update(){
                tvcurpage.setText(new StringBuilder().append(pagenumber));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pagenumber = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pagenumber = seekBar.getProgress();
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
