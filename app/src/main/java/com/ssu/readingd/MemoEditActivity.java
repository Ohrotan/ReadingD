package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.firestore.CollectionReference;
import com.ssu.readingd.util.DBUtil;
import com.ssu.readingd.dto.MemoDTO;
import java.util.Map;

/* 작성자: 박슬우
최초 작성일: 2019.11.16
파일 내용: 메모를 등록할 때 화면 */
public class MemoEditActivity extends AppCompatActivity implements View.OnClickListener {

//    MemoDTO result;
    MemoDTO memoDTO;
    Map<String, Object> memo;
    String id = "McRGKlAF4hHQ55POQs0X";
    ImageView BtnAddphoto;
    SeekBar Seekbar;
    TextView TvCurpage;

    private ImageSwitcher imageSwitcher;
    ImageButton BtnPrev, BtnNext, BtnDelete;
    EditText MemoEdit;
    Switch ShareSwitch;
    Button BtnCancel, BtnSave;

    int r_page = 0;
    int w_page = 300;
    int[] Imgids = new int[10];
    //String[] Imgids = new String[10];
    int imgcnt = 0;
    int imgIndex = 0;
    boolean share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        BtnAddphoto = findViewById(R.id.addphoto_bt);
        Seekbar = findViewById(R.id.page_seekbar);
        TvCurpage = findViewById(R.id.curpage_tv);
        final ImageSwitcher imageSwitcher = findViewById(R.id.image_switcher);
        BtnPrev = findViewById(R.id.prev_btn);
        BtnNext = findViewById(R.id.next_btn);
        BtnDelete = findViewById(R.id.imagedelete_btn);
        MemoEdit = findViewById(R.id.memoedit_et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ShareSwitch = findViewById(R.id.share_switch);
        BtnCancel = findViewById(R.id.cancel_btn);
        BtnSave = findViewById(R.id.save_btn);

        Imgids[0] = 0;


        memo = new DBUtil().getMemo("McRGKlAF4hHQ55POQs0X");

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory(){
            public View makeView(){

                ImageView imageview = new ImageView(getApplicationContext());
                imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageview.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                return imageview;
            }
        });

     //   Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
     //   Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
     //   imageSwitcher.setInAnimation(in);
     //   imageSwitcher.setOutAnimation(out);
        imageSwitcher.setImageResource(Imgids[imgIndex]);

        BtnPrev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgIndex > 0)
                    imgIndex--;

                imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });
        BtnNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgIndex < imgcnt -1)
                    imgIndex++;

                imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgcnt != 0){
                    for(int i = imgIndex; i < imgcnt; i++)
                        Imgids[i] = Imgids[i+1];

                    Imgids[--imgcnt] = 0;
                    if(imgIndex > imgcnt-1)
                        imgIndex--;

                    imageSwitcher.setImageResource(Imgids[imgIndex]);
                }
            }
        });

        BtnAddphoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Imgids[imgcnt++] = R.drawable.memoimgadd;
                imgIndex = imgcnt-1;
                imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });


        Seekbar.setMax(w_page);
        Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r_page = seekBar.getProgress();
                update();
            }

            public void update(){
                TvCurpage.setText(new StringBuilder().append(r_page));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                r_page = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                r_page = seekBar.getProgress();
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

        if(v==BtnCancel){
            onBackPressed();
        }
        else if(v==BtnSave){
            new DBUtil().updateMemo(id, memoDTO);
        }
    }

}

