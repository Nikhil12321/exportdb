package com.example.nikhil.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class EnterValue extends AppCompatActivity {

    LinearLayout linearLayout;
    int columnCount = 0;
    int textViewId = 12321;
    int editTextID = 32123;
    int textSize = 20;
    Vector<Integer> list = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);

        //SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });

        //get the columns of the table
        Intent intent = getIntent();
        String[] columns = intent.getStringArrayExtra(MainActivity.send_columns);
        createFields(columns);



    }

    //function to create text areas and editText fields
    public void createFields(String[] columns){

        for (String column : columns) {

            createTextField(column);
            createEditText(column);
        }

    }

    public void createTextField(String name_of_column){


        final TextView textView = new TextView(this);
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);
        linearLayout.addView(textView);
        textView.setText(name_of_column);
        textView.setGravity(Gravity.START);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(layoutParams);
        //textView.setId(textViewId + columnCount);
        textView.setTextSize(textSize);
        textView.setPadding(0, 20, 0, 0);
        //columnCount++;
    }

    public void createEditText(String name_of_column){

        final EditText editText = new EditText(this);
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);
        int id = editTextID+columnCount;
        linearLayout.addView(editText);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)editText.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        editText.setLayoutParams(layoutParams);
        list.add(id);
        editText.setId(id);
        columnCount++;
    }


}
