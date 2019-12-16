package com.ssu.readingd;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssu.readingd.dto.UserDTO;

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


                db.collection("users").whereEqualTo("id", id)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.d("hs_test", "Listen failed.", e);
                                    return;
                                }
                                for (QueryDocumentSnapshot doc : value) {
                                    if (doc.get("id") != null) {
                                        UserDTO userDTO = doc.toObject(UserDTO.class);
                                        Log.d("hs_test", "DTO생성");
                                        if(userDTO.getId().equals("")){
                                            Toast.makeText(MemberRegisterActivity.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();
                                            Log.d("hs_test", "가입");
                                        }
                                        else{
                                            Toast.makeText(MemberRegisterActivity.this, "이미 가입", Toast.LENGTH_SHORT).show();
                                            Log.d("hs_test", "이미 존재");
                                        }
                                    }
                                    Log.d("hs_test", "doc.get(id)==null");
                                }

                            }
                        });


            }
        });

    }


}
