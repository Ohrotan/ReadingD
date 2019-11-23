package com.ssu.readingd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

public class BookShelfActivity extends AppCompatActivity {

    Spinner sortSpinner;
    ImageButton imageButton;
    TableRow firstRow;
    TableRow secondRow;
    TableRow thirdRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);

        sortSpinner = (Spinner)findViewById(R.id.sortBookSpinner);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        firstRow = (TableRow)findViewById(R.id.firstRow);
        secondRow = (TableRow)findViewById(R.id.secondRow);
        thirdRow = (TableRow)findViewById(R.id.thirdRow);

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
