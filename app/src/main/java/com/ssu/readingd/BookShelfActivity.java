package com.ssu.readingd;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ssu.readingd.adapter.BookShelfAdapter;
import com.ssu.readingd.dto.BookItem;

public class BookShelfActivity extends AppCompatActivity {

    Spinner sortSpinner;
    ImageButton imageButton;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);

        sortSpinner = (Spinner)findViewById(R.id.sortBookSpinner);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        gridView = findViewById(R.id.gridView);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;


        BookShelfAdapter gridAdapter = new BookShelfAdapter(this, displayWidth);
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_2));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));
        gridAdapter.addItem(new BookItem(R.drawable.book_1));




        gridView.setAdapter(gridAdapter);




        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BookShelfActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void clickTab(View v) {
        ImageView[] img = new ImageView[5];
        img[0] = findViewById(R.id.tab_flash_back);
        img[1] = findViewById(R.id.tab_memo);
        img[2] = findViewById(R.id.tab_book);
        img[3] = findViewById(R.id.tab_share);
        img[4] = findViewById(R.id.tab_setting);

        if (v == img[0]) {
            startActivity(new Intent(this, FlashbackActivity.class));
            overridePendingTransition(0, 0);
        }

    }


}
