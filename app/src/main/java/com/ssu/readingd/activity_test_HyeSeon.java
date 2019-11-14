package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

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

    }
}
