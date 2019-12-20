package com.ssu.readingd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssu.readingd.dto.MemoDTO;
import com.ssu.readingd.util.DBUtil;

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
            String memo_id = "iNlSlWUg7kHRyhyR7zZt";
            CustomDialog customDialog = new CustomDialog(TestSeulwooActivity.this);
            customDialog.callFunction(memo_id);

        } else if(v == btn102){

        }else if(v == btn103){
            intent = new Intent(this,MemoRegisterActivity.class);
            startActivity(intent);

        }else if(v == btn104) {
            intent = new Intent(this, MemoEditActivity.class);
            startActivity(intent);
        }

    }

    public class CustomDialog{

        private Context context;

        public CustomDialog(Context context){
            this.context= context;
        }

        public void callFunction(final String memo_id){
            final Dialog dlg = new Dialog(context);

            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dlg.setContentView(R.layout.layout_memo_delete);
            dlg.show();

            final Button deleteBtn = (Button)dlg.findViewById(R.id.memoDeleteBtn);
            final Button cancelBtn = (Button)dlg.findViewById(R.id.memoDeleteCancelBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    new DBUtil().DeleteMemo(memo_id);
                    onBackPressed();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    onBackPressed();
                }
            });
        }

    }


}
