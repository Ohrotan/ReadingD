package com.ssu.readingd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ssu.readingd.dto.MemoDTO;
import com.ssu.readingd.util.DBUtil;

import java.io.File;
import java.io.IOException;
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
    String book_author;
    TextView TvBookname;
    String user_id;
    String memo_id;
    ImageView BtnAddphoto;
    SeekBar Seekbar;
    TextView TvCurpage;


    ImageSwitcher imageSwitcher;
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

    Context context = this;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference pathRefernece;
    Uri filePath;
    String path;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        TvBookname = findViewById(R.id.booktitle_tv);
        BtnAddphoto = findViewById(R.id.addphoto_bt);
        Seekbar = findViewById(R.id.page_seekbar);
        TvCurpage = findViewById(R.id.curpage_tv);
        BtnPrev = findViewById(R.id.prev_btn);
        BtnNext = findViewById(R.id.next_btn);
        BtnDelete = findViewById(R.id.imagedelete_btn);
        MemoEdit = findViewById(R.id.memoedit_et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ShareSwitch = findViewById(R.id.share_switch);
        BtnCancel = findViewById(R.id.cancel_btn);
        BtnSave = findViewById(R.id.save_btn);
        imageSwitcher = findViewById(R.id.image_switcher);

        SharedPreferences sharedPref= PreferenceManager. getDefaultSharedPreferences (this);
        user_id=sharedPref.getString("id", "none");
       Intent intent = getIntent();
       memoDTO = (MemoDTO)intent.getExtras().getSerializable("memo");
       memo_id = memoDTO.getMemo_id();

/*
        DocumentReference docRef = db.collection("memos").document(memo_id);
        Task<DocumentSnapshot> task = docRef.get();
        while(!task.isSuccessful()){;}
        DocumentSnapshot document = task.getResult();
        memoDTO = document.toObject(MemoDTO.class);
*/
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
                flag = 0;
                makeDialog();
                //while(flag != 10){;}
                setImageSwitcher(getApplicationContext(), imageSwitcher, imgIndex);
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
    @Override
    public void onClick(View v) {

        if(v==BtnCancel){
            onBackPressed();
        }
        else if(v==BtnSave) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String reg_date = dateformat.format(cal.getTime());
            MemoText = MemoEdit.getText().toString();
            memoDTO = new MemoDTO(book_name, book_author, Imgids2, MemoText, r_page, reg_date, share, user_id, w_page);
            new DBUtil().updateMemo(memo_id, memoDTO);

            Intent intent = new Intent(context, MemoListActivity.class);
            startActivity(intent);
            finish();
            //onBackPressed();
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
        this.book_author = memoDTO.getBook_author();

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

    public void setImageSwitcher(final Context con, final ImageSwitcher imageSwitcher, int imgIndex){
        String imgname = "default_image.jpg";
        if(imgcnt != 0)
            imgname = Imgids2.get(imgIndex);
        if(!imgname.contains("jpg"))
            imgname = imgname+".PNG";
        StorageReference httpsReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/" + imgname);

        httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageSwitcher temp = imageSwitcher;
                Glide.with(con).load(uri).into((ImageView)imageSwitcher.getCurrentView());
            }
        });
    }


    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(MemoEditActivity.this);

        alt_bld.setTitle("사진 업로드").setCancelable(false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        takePhoto();
                    }
                }).setNegativeButton("앨범선택",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        selectAlbum();
                    }
                }).setNeutralButton("취소   ",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File photoFile = null;
        try {
            photoFile = File.createTempFile("IMG",".jpg", dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(photoFile != null){
                filePath = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                startActivityForResult(takePictureIntent, 2001);
            }
        }

    public void selectAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2002 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
        }
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            //filePath = data.getData();
            uploadFile();
        }
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMHHmmss");
            String reg_date = dateformat.format(cal.getTime());
            final String filename = reg_date + ".jpg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/ssu-readingd.appspot.com/o/" + filename);

            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String fname = filename;
                    Imgids2.add(filename);
                    imgcnt++;
                    imgIndex = imgcnt -1;
                    setImageSwitcher(getApplicationContext(), imageSwitcher, imgIndex);
                }
            });
        } else
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
    }


}

