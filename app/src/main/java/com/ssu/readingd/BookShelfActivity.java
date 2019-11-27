package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

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


}
