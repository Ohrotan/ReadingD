package com.ssu.readingd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    AlertDialog alertDialog;

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
            intent = new Intent(this, CommunitySearchResultActivity.class);
            startActivity(intent);
        }
        else if(v == BtnMemoSearchLayout){
            //메모 검색 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("메모 검색");
            builder.setMessage("메모 검색");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
            alertDialog.show();

        }
        else if(v == BtnBookShelfDeleteLayout){
            //책 삭제 확인 팝업
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("책 삭제");
            builder.setMessage("정말 삭제하시겠습니까?");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
            alertDialog.show();
        }
        else if(v == BtnFindPasswordLayout){
            //비밀번호 찾기 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("비밀번호 찾기");
            builder.setMessage("비밀번호 찾기");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
            alertDialog.show();
        }
        else if(v == BtnMemberOutLayout){
            //회원탈퇴 레이아웃
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("회원탈퇴");
            builder.setMessage("회원탈퇴");
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
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
