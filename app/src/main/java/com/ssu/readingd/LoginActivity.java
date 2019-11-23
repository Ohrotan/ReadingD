package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button loginBtn;
    Button registerBtn;
    Button findPwBtn;
    EditText idText;
    EditText pwText;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        findPwBtn = (Button)findViewById(R.id.findPwBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwText = (EditText)findViewById(R.id.pwText);

    }

    @Override
    public void onClick(View v) {

        if(v == loginBtn){
            Toast.makeText(LoginActivity.this, "로그인", Toast.LENGTH_SHORT).show();
        }
        else if(v == registerBtn){
            intent = new Intent(this, MemberRegisterActivity.class);
            startActivity(intent);
        }
        else if(v == findPwBtn){
            Toast.makeText(LoginActivity.this, "비밀번호찾기", Toast.LENGTH_SHORT).show();
        }

    }
}
