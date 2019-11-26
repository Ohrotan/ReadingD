package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemberRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button registerBtn;
    EditText idText;
    EditText pwText;
    EditText pwText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        registerBtn = (Button)findViewById(R.id.registerBtn);
        idText = (EditText)findViewById(R.id.idText);
        pwText = (EditText)findViewById(R.id.pwText);
        pwText2 = (EditText)findViewById(R.id.pwText2);

    }

    @Override
    public void onClick(View v) {

        if (v==registerBtn){
            //회원가입 버튼

            Toast.makeText(MemberRegisterActivity.this, "id : "+idText.getText().toString()+" / pw : "+pwText.getText().toString(), Toast.LENGTH_SHORT).show();
        }



    }
}
