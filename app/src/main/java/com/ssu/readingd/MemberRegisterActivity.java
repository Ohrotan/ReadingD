package com.ssu.readingd;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ssu.readingd.util.DBUtil;

public class MemberRegisterActivity extends AppCompatActivity {

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


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MemberRegisterActivity.this, "id : "+idText.getText().toString()+" / pw : "+pwText.getText().toString(), Toast.LENGTH_SHORT).show();

                DBUtil dbu = new DBUtil();

                final String id = idText.getText().toString();
                final String pw1 = pwText.getText().toString();
                final String pw2 = pwText2.getText().toString();



                if( !pw1.equals(pw2) ){
                    Toast.makeText(MemberRegisterActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                }
                else if( dbu.findUser(id) ){
                    Toast.makeText(MemberRegisterActivity.this,"가입된 회원입니다.", Toast.LENGTH_SHORT).show();
                }
                else if( pw1.equals(pw2) && !dbu.findUser(id) ){

                    dbu.addUser(id, pw1);
                    Toast.makeText(MemberRegisterActivity.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


}
