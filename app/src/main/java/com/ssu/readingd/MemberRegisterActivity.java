package com.ssu.readingd;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssu.readingd.util.DBUtil;

public class MemberRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button registerBtn;
    EditText idText;
    EditText pwText;
    EditText pwText2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        registerBtn = (Button)findViewById(R.id.registerBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwText = (EditText)findViewById(R.id.pwText);
        pwText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwText2 = (EditText)findViewById(R.id.pwText2);
        pwText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        registerBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        final String id = idText.getText().toString();
        final String pw1 = pwText.getText().toString();
        final String pw2 = pwText2.getText().toString();

        if( id.equals("")){
            Toast.makeText(MemberRegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(pw1.equals("") && pw2.equals("")){
            Toast.makeText(MemberRegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(!pw1.equals(pw2)){
            Toast.makeText(MemberRegisterActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
        }

        if(pw1.length() < 6){
            Toast.makeText(MemberRegisterActivity.this, "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
        }


        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(MemberRegisterActivity.this, "이미 가입한 유저입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MemberRegisterActivity.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();
                        new DBUtil().addUser(id, pw1);
                    }
                }
                else{
                    Log.d("hs_test", "get failed with", task.getException());
                }
            }
        });


    }
}
