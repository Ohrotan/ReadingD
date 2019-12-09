package com.ssu.readingd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssu.readingd.util.DBUtil;
import com.ssu.readingd.dto.MemoDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/* 작성자: 박슬우
최초 작성일: 2019.11.16
파일 내용: 메모를 등록할 때 화면 */
public class MemoEditActivity extends AppCompatActivity implements View.OnClickListener {

    MemoDTO memoDTO;
    String book_name;
    TextView TvBookname;
    String user_id;
    String memo_id = "McRGKlAF4hHQ55POQs0X";
    ImageView BtnAddphoto;
    SeekBar Seekbar;
    TextView TvCurpage;

    private ImageSwitcher imageSwitcher;
    ImageButton BtnPrev, BtnNext, BtnDelete;
    EditText MemoEdit;
    String MemoText;
    Switch ShareSwitch;
    Button BtnCancel, BtnSave;

    int r_page = 0;
    int w_page = 300;
    int[] Imgids = new int[10];
   // String[] Imgids2 = new String[10];
    List<String> Imgids2 = new ArrayList<String>();
    int imgcnt = 0;
    int imgIndex = 0;
    boolean share;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference pathRefernece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        TvBookname = findViewById(R.id.booktitle_tv);
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


        DocumentReference docRef = db.collection("memos").document(memo_id);
        Task<DocumentSnapshot> task = docRef.get();
        while(!task.isSuccessful()){;}
        DocumentSnapshot document = task.getResult();
        memoDTO = document.toObject(MemoDTO.class);

       ReadMemo(memoDTO);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory(){
            public View makeView(){

                ImageView imageview = new ImageView(getApplicationContext());
                imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageview.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                return imageview;
            }
        });

        imgcnt = Imgids2.size();
        imgIndex = 0;
        setImageSwitcher(getApplicationContext(), imageSwitcher, imgIndex);



     //   Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
     //   Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
     //   imageSwitcher.setInAnimation(in);
     //   imageSwitcher.setOutAnimation(out);
     //   imageSwitcher.setImageResource(Imgids[imgIndex]);

        BtnPrev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgIndex > 0)
                    imgIndex--;

                setImageSwitcher(getApplicationContext(),imageSwitcher, imgIndex);
               // imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });
        BtnNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgIndex < imgcnt -1)
                    imgIndex++;

                setImageSwitcher(getApplicationContext(),imageSwitcher, imgIndex);
                //imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imgcnt>0) {
                    Imgids2.remove(imgIndex);
                    imgcnt--;

                    if (imgIndex > imgcnt - 1)
                        imgIndex--;

                    setImageSwitcher(getApplicationContext(), imageSwitcher, imgIndex);
                    //imageSwitcher.setImageResource(Imgids[imgIndex]);
                }
            }
        });

        BtnAddphoto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Imgids2.add("memoimgadd");
                imgcnt++;
                //Imgids2.set(imgcnt++, "memoimgadd");
                //Imgids[imgcnt++] = R.drawable.memoimgadd;
                imgIndex = imgcnt-1;

                setImageSwitcher(getApplicationContext(),imageSwitcher, imgIndex);
                //imageSwitcher.setImageResource(Imgids[imgIndex]);
            }
        });


        Seekbar.setMax(w_page);
        Seekbar.setProgress(r_page);
        TvCurpage.setText(new StringBuilder().append(r_page));
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

        ShareSwitch.setChecked(share);
        ShareSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    share = true;
                else
                    share = false;
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
        else if(v==BtnSave) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String reg_date = dateformat.format(cal.getTime());
            MemoText = MemoEdit.getText().toString();
            memoDTO = new MemoDTO(book_name, Imgids2, MemoText, r_page, reg_date, share, user_id, w_page);
            new DBUtil().updateMemo(memo_id, memoDTO);
        }
    }

    public void ReadMemo(MemoDTO memoDTO){
        this.book_name = memoDTO.getBook_name();
        this.Imgids2 = memoDTO.getImg();
        this.MemoText = memoDTO.getMemo_text();
        this.r_page = memoDTO.getR_page();
        this.share = memoDTO.getShare();
        this.user_id = memoDTO.getUser_id();
        this.w_page = memoDTO.getW_page();

        TvBookname.setText(book_name);
        MemoEdit.setText(MemoText);
    }

    public void ReadMemo(Map<String, Object> memo){
        this.book_name = (String)memo.get("book_name");
        this.Imgids2 = (List <String>)memo.get("img");
        this.MemoText = (String)memo.get("memo_text");
        this.r_page = (int)memo.get("r_page");
        this.share = (boolean)memo.get("share");
        this.user_id = (String)memo.get("user_id");
        this.w_page = (int)memo.get("w_page");
        MemoEdit.setText(MemoText);
        TvBookname.setText(book_name);
    }

    public void setImageSwitcher(final Context con, ImageSwitcher imageSwitcher, int imgIndex){
        String imgname = "default_image.jpg";
        if(imgcnt != 0)
            imgname = Imgids2.get(imgIndex);
        if(!imgname.contains("jpg"))
            imgname = imgname+".PNG";
        StorageReference httpsReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/" + imgname);

        Task<Uri> uritask = httpsReference.getDownloadUrl();
        while(!uritask.isSuccessful()){;}
        Uri uri = uritask.getResult();
        Glide.with(con).load(uri).into((ImageView)imageSwitcher.getCurrentView());
    }
}

