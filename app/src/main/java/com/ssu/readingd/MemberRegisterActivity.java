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

                //DBUtil dbu = new DBUtil();



                DocumentReference docRef = db.collection("users").document(idText.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //Log.d("test", "DocumentSnapshot data: " + document.get("id"));

                                if(idText.getText().toString() == document.get("id")){
                                    Toast.makeText(getApplicationContext(), "이미 가입하셨습니다.", Toast.LENGTH_SHORT);
                                    Log.d("test", "이미 가입했음");
                                }


                            } else {
                                //Log.d("test", "No such document");
                                //Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT);
                                //Log.d("test","idText의 string : "+idText.getText().toString());
                                //Log.d("test","db의 string : "+document.getData());

                                if(pwText.getText().toString() != pwText2.getText().toString() ){
                                    Toast.makeText(getApplicationContext(), "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT);
                                    Log.d("test", "비번 틀림");
                                }
                                else if(pwText.getText().toString()== pwText2.getText().toString()&& idText.getText().toString()!=document.get("id")){
                                    new DBUtil().addUser(idText.getText().toString(), pwText.getText().toString());
                                    Toast.makeText(getApplicationContext(), "가입되었습니다.", Toast.LENGTH_SHORT);
                                    Log.d("test", "가입 됐음");
                                }

                            }
                        } else {
                            Log.d("test", "get failed with ", task.getException());
                            Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT);
                        }




                    }

                });



            }
        });

    }


}
