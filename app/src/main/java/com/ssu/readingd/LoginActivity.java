package com.ssu.readingd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssu.readingd.dto.UserDTO;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginBtn;
    Button registerBtn;
    Button findPwBtn;
    EditText idText;
    EditText pwText;
    Intent intent;
    UserDTO user;
    Dialog alertDialog;
    Context context;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        findPwBtn = (Button)findViewById(R.id.findPwBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwText = (EditText)findViewById(R.id.pwText);
        pwText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        context = this;



    }

    @Override
    public void onClick(View v) {

        final String id = idText.getText().toString();
        final String pw1 = pwText.getText().toString();

        if(v == loginBtn){

            if( id.equals("")){
                Toast.makeText(LoginActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(pw1.equals("")){
                Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            DocumentReference docRef = db.collection("users").document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        UserDTO user = document.toObject(UserDTO.class);
                        if(document.exists()){

                            String loginPwd = Hashing.sha256().hashString(pw1, StandardCharsets.UTF_8).toString();
                            if(user.getPwd().equals(loginPwd)){
                                //Toast.makeText(LoginActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                                Log.d("hs_test", "user.getPwd() = " + user.getPwd());

                                // 인텐트 객체 생성해서 결과 담벼락으로 보내고 종료하기
                                Intent intent = new Intent(context, MemoListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else{
                            Toast.makeText(LoginActivity.this, "가입하지 않은 회원입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Log.d("hs_test", "get failed with", task.getException());
                    }
                }
            });


        }
        else if(v == registerBtn){
            intent = new Intent(this, MemberRegisterActivity.class);
            startActivity(intent);
        }
        else if(v == findPwBtn){
            alertDialog = new Dialog(LoginActivity.this);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.layout_find_password);

            Button cancelBtn = alertDialog.findViewById(R.id.findPWCancelBtn);
            Button findBtn = alertDialog.findViewById(R.id.findBtn);

            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    alertDialog.dismiss();
                }
            });

            findBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LoginActivity.this, "비밀번호 찾기", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

    }
}
