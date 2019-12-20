package com.ssu.readingd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.zip.Inflater;

public class activity_test_HyeSeon extends AppCompatActivity implements View.OnClickListener{

    Button BtnMemoListActivity;
    Button BtnBookMemoListActivity;
    Button BtnMemoSearchLayout;
    Button BtnMemoSearchResultActivity;
    Button BtnBookShelfActivity;
    Button BtnBookShelfDeleteLayout;
    Button BtnLoginActivity;
    Button BtnMemberRegisterActivity;
    Button BtnFindPasswordLayout;
    Button BtnMemberOutLayout;
    Button BtnCommunityActivity;
    Button BtnCommunitySearch;
    Button BtnCommunitySearchResultActivity;



    Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__hye_seon);

        BtnMemoListActivity = (Button)findViewById(R.id.BtnMemoListActivity);
        BtnBookMemoListActivity = (Button)findViewById(R.id.BtnBookMemoListActivity);
        BtnMemoSearchLayout = (Button)findViewById(R.id.BtnMemoSearchLayout);
        BtnMemoSearchResultActivity = (Button)findViewById(R.id.BtnMemoSearchResultActivity);
        BtnBookShelfActivity = (Button)findViewById(R.id.BtnBookShelfActivity);
        BtnBookShelfDeleteLayout = (Button)findViewById(R.id.BtnBookShelfDeleteLayout);
        BtnLoginActivity = (Button)findViewById(R.id.BtnLoginActivity);
        BtnMemberRegisterActivity = (Button)findViewById(R.id.BtnMemberRegisterActivity);
        BtnFindPasswordLayout = (Button)findViewById(R.id.BtnFindPasswordLayout);
        BtnMemberOutLayout = (Button)findViewById(R.id.BtnMemberOutLayout);
        BtnCommunityActivity = (Button)findViewById(R.id.BtnCommunityActivity);
        BtnCommunitySearch = (Button)findViewById(R.id.BtnCommunitySearch);
        BtnCommunitySearchResultActivity = (Button)findViewById(R.id.BtnCommunitySearchResultActivity);



    }

    @Override
    public void onClick(View v) {
        Intent intent;

        alertDialog = new Dialog(activity_test_HyeSeon.this);



        if(v == BtnMemoListActivity){
            //메모리스트 액티비티
            intent = new Intent(this, MemoListActivity.class);
            startActivity(intent);
        }
        else if(v == BtnBookMemoListActivity){
            //책 메모리스트 액티비티
            intent = new Intent(this, BookMemoListActivity.class);
            startActivity(intent);
        }
        else if(v == BtnMemoSearchResultActivity){
            //메모 검색결과 액티비티
            intent = new Intent(this, MemoSearchResultActivity.class);
            startActivity(intent);
        }
        else if(v == BtnBookShelfActivity){
            //책장 메인 액티비티
            intent = new Intent(this, BookShelfActivity.class);
            startActivity(intent);
        }
        else if(v == BtnLoginActivity){
            //로그인 화면 액티비티
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if(v == BtnMemberRegisterActivity){
            //회원가입 액티비티
            intent = new Intent(this, MemberRegisterActivity.class);
            startActivity(intent);
        }
        else if(v == BtnCommunityActivity){
            //담벼락 메인 액티비티
            intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
        }
        else if(v == BtnCommunitySearchResultActivity){
            //담벼락 책 검색결과 액티비티
           // intent = new Intent(this, CommunitySearchResultActivity.class);
           // startActivity(intent);
        }
        else if(v == BtnMemoSearchLayout){
            //메모 검색 레이아웃

            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.memo_search_layout);

            Button cancelBtn = alertDialog.findViewById(R.id.searchCancelBtn);
            Button searchBtn = alertDialog.findViewById(R.id.searchBtn);

            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    alertDialog.dismiss();
                }
            });

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity_test_HyeSeon.this, "검색", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();


        }
        else if(v == BtnBookShelfDeleteLayout){
            //책 삭제 확인 팝업
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.layout_book_delete);

            Button cancelBtn = alertDialog.findViewById(R.id.bookDeleteCancelBtn);
            Button delBtn = alertDialog.findViewById(R.id.bookDeleteBtn);

            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    alertDialog.dismiss();
                }
            });

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity_test_HyeSeon.this, "책 삭제", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

        else if(v == BtnFindPasswordLayout){
            //비밀번호 찾기 레이아웃
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
                    Toast.makeText(activity_test_HyeSeon.this, "비밀번호 찾기", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

        else if(v == BtnMemberOutLayout){
            //회원탈퇴 레이아웃
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.layout_member_out);

            Button cancelBtn = alertDialog.findViewById(R.id.outCancelBtn);
            Button outBtn = alertDialog.findViewById(R.id.outBtn);

            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    alertDialog.dismiss();
                }
            });

            outBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity_test_HyeSeon.this, "회원탈퇴", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }
        else if(v== BtnCommunitySearch){
            //담벼락 검색 레이아웃 => 메모 검색 레이아웃 재활용
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("메모 검색");
            builder.setMessage("메모 검색");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
